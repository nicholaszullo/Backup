/*
    FUSE: Filesystem in Userspace
    Copyright (C) 2001-2007  Miklos Szeredi <miklos@szeredi.hu>

    This program can be distributed under the terms of the GNU GPL.
    See the file COPYING.
*/

#define	FUSE_USE_VERSION 26

#include <fuse.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>

//size of a disk block
#define	BLOCK_SIZE 512

//we'll use 8.3 filenames
#define	MAX_FILENAME 8
#define	MAX_EXTENSION 3

//How many files can there be in one directory?
#define MAX_FILES_IN_DIR (BLOCK_SIZE - sizeof(int)) / ((MAX_FILENAME + 1) + (MAX_EXTENSION + 1) + sizeof(size_t) + sizeof(long))

//The attribute packed means to not align these things
struct cs1550_directory_entry
{
    int nFiles;	//How many files are in this directory.
                //Needs to be less than MAX_FILES_IN_DIR

    struct cs1550_file_directory
    {
        char fname[MAX_FILENAME + 1];	//filename (plus space for nul)
        char fext[MAX_EXTENSION + 1];	//extension (plus space for nul)
        size_t fsize;					//file size
        long nIndexBlock;				//where the index block is on disk
    } __attribute__((packed)) files[MAX_FILES_IN_DIR];	//There is an array of these

    //This is some space to get this to be exactly the size of the disk block.
    //Don't use it for anything.
    char padding[BLOCK_SIZE - MAX_FILES_IN_DIR * sizeof(struct cs1550_file_directory) - sizeof(int)];
} ;

typedef struct cs1550_root_directory cs1550_root_directory;

#define MAX_DIRS_IN_ROOT (BLOCK_SIZE - sizeof(int)) / ((MAX_FILENAME + 1) + sizeof(long))

struct cs1550_root_directory
{
    int nDirectories;	//How many subdirectories are in the root
                        //Needs to be less than MAX_DIRS_IN_ROOT
    struct cs1550_directory
    {
        char dname[MAX_FILENAME + 1];	//directory name (plus space for nul)
        long nStartBlock;				//where the directory block is on disk
    } __attribute__((packed)) directories[MAX_DIRS_IN_ROOT];	//There is an array of these

    //This is some space to get this to be exactly the size of the disk block.
    //Don't use it for anything.
    char padding[BLOCK_SIZE - MAX_DIRS_IN_ROOT * sizeof(struct cs1550_directory) - sizeof(int)];
} ;


typedef struct cs1550_directory_entry cs1550_directory_entry;

//How many entries can one index block hold?
#define	MAX_ENTRIES_IN_INDEX_BLOCK (BLOCK_SIZE/sizeof(long))

struct cs1550_index_block
{
      //All the space in the index block can be used for index entries.
            // Each index entry is a data block number.
      long entries[MAX_ENTRIES_IN_INDEX_BLOCK];
};

typedef struct cs1550_index_block cs1550_index_block;

//How much data can one block hold?
#define	MAX_DATA_IN_BLOCK (BLOCK_SIZE)

struct cs1550_disk_block
{
    //All of the space in the block can be used for actual data
    //storage.
    char data[MAX_DATA_IN_BLOCK];
};

typedef struct cs1550_disk_block cs1550_disk_block;

FILE *fid = 0;	//FID OF DISK, set in init()
cs1550_root_directory *ROOT = 0;	//ROOT DIRECTORY STRUCT global declaration to use everywhere and store in .data
cs1550_disk_block *bitmap;			//Bitmap, needs accessed in any method so store in .data	
//Quick macros to move current pointer in disk
#define GOTO_BITMAP fseek(fid, -(3*BLOCK_SIZE), SEEK_END);
#define GOTO_BEGIN fseek(fid, 0, SEEK_SET);

/**
 *	Will return the next free block number
 */
static int findFreeDiskBlock(void){
    int i = 0;
    int offset = 11;        //The offset starts as anything other than 0-7
    int bitmapnum = 0;      //The current block of bitmap
    int total = 0;          //The total disk block position
    for (bitmapnum = 0; bitmapnum < 3 && offset == 11; bitmapnum++){        //Check the 3 disk blocks of bitmap	
        for (i = 0; i < BLOCK_SIZE; i++,total++){		                    //Check the current byte in the block
            if (bitmap[bitmapnum].data[i] == (signed char)0xFF)             //Check if full first, don't waste operations if no change to the byte is needed
                continue;
            else if (bitmap[bitmapnum].data[i] == (signed char)0x00){       //Bits should be added from left to right
                bitmap[bitmapnum].data[i] = 0x80;                           //10000000
                offset = 0;                                                 //Offset is 0 since all the way at the left
                break;
            } else if (bitmap[bitmapnum].data[i] == (signed char)0x80){
                bitmap[bitmapnum].data[i] = 0xC0;                           //11000000
                offset = 1;                                                 //Offset is now 1 because 1 from the left
                break;
            } else if (bitmap[bitmapnum].data[i] == (signed char)0xC0){
                bitmap[bitmapnum].data[i] = 0xE0;                           //11100000
                offset = 2;
                break;
            } else if (bitmap[bitmapnum].data[i] == (signed char)0xE0){
                bitmap[bitmapnum].data[i] = 0xF0;                           //11110000
                offset = 3; 
                break;
            } else if (bitmap[bitmapnum].data[i] == (signed char)0xF0){
                offset = 4;
                bitmap[bitmapnum].data[i] = 0xF8;                           //11111000
                break;
            } else if (bitmap[bitmapnum].data[i] == (signed char)0xF8){
                bitmap[bitmapnum].data[i] = 0xFC;                           //11111100
                offset = 5;
                break;
            } else if (bitmap[bitmapnum].data[i] == (signed char)0xFC){
                bitmap[bitmapnum].data[i] = 0xFE;                           //11111110
                offset = 6;
                break;
            } else if (bitmap[bitmapnum].data[i] == (signed char)0xFE){
                bitmap[bitmapnum].data[i] = 0xFF;                           //11111111  This byte is now filled
                offset = 7;
                break;
            } else if (bitmapnum == 2){                                     //When on the last block of the bitmap, if none of the above are true that means only 1 byte is left that isnt 0xFF 
                if (bitmap[bitmapnum].data[i] == (signed char)0x07){        //Bitmap initialized last 3 bits to 1 so start checking from there
                    bitmap[bitmapnum].data[i] = 0x0F;                       //00001111  Its fine to fill the last byte from right to left because no removal operations are implemented so once a block is taken it is gone forever
                    offset = 4;                                             //4 from left was just taken so offset is 4
                    break;
                } else if (bitmap[bitmapnum].data[i] == (signed char)0x0F){
                    bitmap[bitmapnum].data[i] = 0x1F;                       //00011111
                    offset = 3;
                    break;
                } else if (bitmap[bitmapnum].data[i] == (signed char)0x1F){
                    bitmap[bitmapnum].data[i] = 0x3F;                       //00111111
                    offset = 2;
                    break;
                } else if(bitmap[bitmapnum].data[i] == (signed char)0x3F){
                    bitmap[bitmapnum].data[i] = 0x7F;                       //01111111
                    offset = 1;
                    break;
                } else if (bitmap[bitmapnum].data[i] == (signed char)0x7F){
                    bitmap[bitmapnum].data[i] = 0xFF;                       //11111111
                    offset = 0;
                    printf("THIS IS THE LAST FREE BLOCK!!\n");
                    break;
                }else {
                    printf("-------PANIC----PANIC----PANIC----PANIC-------\n");
                    printf("-------PANIC----PANIC----PANIC----PANIC-------\n");
                    printf("-------PANIC----PANIC----PANIC----PANIC-------\n");
                }
            } 
            else {                                                          //If the byte is not in the above form, the disk is corrupted. Removals are not supported so a case like 11011111 is not needed to be supported
                printf("-------PANIC----PANIC----PANIC----PANIC-------\n");
                printf("-------PANIC----PANIC----PANIC----PANIC-------\n");
                printf("-------PANIC----PANIC----PANIC----PANIC-------\n");
            }
        }
    }
    return ((8*total)+offset);                                              //The block position is 8*freebyte + specific block in that group of 8
}

/*
 * Called whenever the system wants to know the file attributes, including
 * simply whether the file exists or not.
 *
 * man -s 2 stat will show the fields of a stat structure
 */
static int cs1550_getattr(const char *path, struct stat *stbuf)
{
    int res = 0;

    memset(stbuf, 0, sizeof(struct stat));                                  //Set the buf to 0 before filling it
    //is path the root dir?
    if (strcmp(path, "/") == 0) {                                           //Root directory has these properties, check it first
        stbuf->st_mode = S_IFDIR | 0755;
        stbuf->st_nlink = 2;
    } else {
        char* directory = (char*)malloc(MAX_FILENAME +1);                   //Holds the directory
        char* filename = (char*)malloc(MAX_FILENAME+1);                     //Holds the file name
        char* extension = (char*)malloc(MAX_EXTENSION+1);                   //Holds the file extension
        //Check if name is subdirectory
        strcpy(filename,"0");                                               //Clear data 
        strcpy(extension,"0");                                              //Clear data
        sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);    //Fill strings with values from path
        if (*filename == '0'){				                                //If the file name wasnt changed it is a directory path
            int i = 0;
            while (i < ROOT->nDirectories){                                 //Find directory from array of directories
                if (strcmp(ROOT->directories[i].dname,directory) == 0){
                    //Might want to return a structure with these fields
                    stbuf->st_mode = S_IFDIR | 0755;                        //Set its values
                    stbuf->st_nlink = 2;
                    return 0; //no error
                }
                i++;
            }
            return -ENOENT;                                                 //If control gets here, no directory name matched from the array so no directory found
        }else{
           //Check if name is a regular file
            if (*filename !='0' && *extension != '0'){                      //File must have a name and an extension 
                int i;
                for (i =0; strcmp(ROOT->directories[i].dname, directory) != 0 && i < ROOT->nDirectories; i++);		//Find the directory from array of directories in root directory
                cs1550_directory_entry *currDir = (cs1550_directory_entry *)malloc(sizeof(cs1550_directory_entry));	//Allocate space for current directory
                fseek(fid, ROOT->directories[i].nStartBlock*BLOCK_SIZE,SEEK_SET);
                fread(currDir,sizeof(cs1550_directory_entry),1,fid);		//Initialize current directory
                int filenum = 0;                                            //Define scope outside for 
                for (i = 0; i < currDir->nFiles; i++){		//Find file in current directory
                    if (strcmp(currDir->files[i].fname,filename) == 0){
                        if (strcmp(currDir->files[i].fext,extension) == 0){
                            filenum = i;                                    //File found matches filename and extension of path
                            break;
                        }
                    }
                }
                if (i == currDir->nFiles){                                  //If i gets to the end of the array, no file matched the name and extension of path
                    return -ENOENT;
                }
                stbuf->st_mode = S_IFREG | 0666;                            //Set values for file
                stbuf->st_nlink = 1;
                stbuf->st_size = currDir->files[filenum].fsize; //file size 
                res = 0; // no error
            } else {
                //Else return that path doesn't exist
                res = -ENOENT;
            }
        }
        free(directory);
        free(filename);
        free(extension);	
    }
    return res;
}

/*
 * Called whenever the contents of a directory are desired. Could be from an 'ls'
 * or could even be when a user hits TAB to do autocompletion
 */
static int cs1550_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
             off_t offset, struct fuse_file_info *fi)
{
    //Since we're building with -Wall (all warnings reported) we need
    //to "use" every parameter, so let's just cast them to void to
    //satisfy the compiler
    (void) offset;
    (void) fi;
    //Is the current location in the root?
    if (strcmp(path, "/") == 0){
        //the filler function allows us to add entries to the listing
        //read the fuse.h file for a description (in the ../include dir)
        filler(buf, ".", NULL, 0);
        filler(buf, "..", NULL, 0);
        int i;
        for (i = 0; i < ROOT->nDirectories; i++){                           //Add all directories in directory array to the filler 
            filler(buf,ROOT->directories[i].dname,NULL,0);
        }

    }else {
        char* directory = (char*)malloc(MAX_FILENAME +1);
        char* filename = (char*)malloc(MAX_FILENAME+1);
        char* extension = (char *)malloc(MAX_EXTENSION+1);
        strcpy(filename,"0");
        strcpy(extension,"0");
        sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);    //Get values from path
        int i = 0;
        for (i =0; strcmp(ROOT->directories[i].dname, directory) != 0 && i < ROOT->nDirectories; i++);     //Find the directory from array of directories in root directory
        if (i == ROOT->nDirectories)                                        //Directory not found in array
            return -ENOENT;
        filler(buf, ".",NULL,0);
        filler(buf, "..", NULL, 0);
        cs1550_directory_entry *currDir = (cs1550_directory_entry *)malloc(sizeof(cs1550_directory_entry)); //Allocate space for current directory
        fseek(fid, ROOT->directories[i].nStartBlock*BLOCK_SIZE,SEEK_SET);
        fread(currDir,sizeof(cs1550_directory_entry),1,fid);
        char *newpath = (char *)malloc(MAX_EXTENSION+MAX_FILENAME+1);            
        for (i = 0; i < currDir->nFiles; i++){                              //For every file in this directory
            strcpy(newpath,"");                                             //Build the path of the file
            strcat(newpath,currDir->files[i].fname);
            strcat(newpath, ".");
            strcat(newpath, currDir->files[i].fext);
            filler(buf,newpath, NULL,0);                                    //Add path to filler
        }
        free(directory);
        free(filename);
        free(extension);
        
    }
    
    return 0;
}

/*
 * Creates a directory. We can ignore mode since we're not dealing with
 * permissions, as long as getattr returns appropriate ones for us.
 */
static int cs1550_mkdir(const char *path, mode_t mode)
{
    (void) path;
    (void) mode;
    if (ROOT->nDirectories == MAX_DIRS_IN_ROOT)                             //If no space left for directory, immediately stop
        return -ENOSPC;

    struct cs1550_directory *new = (struct cs1550_directory *)malloc(sizeof(struct cs1550_directory));  //New array object for ROOT
    memset(new,0,sizeof(struct cs1550_directory));
    char *name = malloc(MAX_FILENAME+1);
    char *junk = malloc(32);
    strcpy(junk, "");
    sscanf(path,"/%[^/]/%[^/]", name, junk);	                            //Get directory name. If anything else in path not a valid place for a directory
    if (strlen(name) > MAX_FILENAME)                                        //Length of name must be less than 8
        return -ENAMETOOLONG;
    if (strlen(junk) != 0){                                                 //If anything in this then not a valid path
        return -EPERM;
    }
    int k;
    for (k = 0; k < ROOT->nDirectories; k++){                               //Check for a directory with that name is already created
        if (strcmp(ROOT->directories[k].dname,name) == 0){
            return -EEXIST;
        }
    }
    strcpy(new->dname, name);					                            //Update name of directory in array entry
    ROOT->directories[ROOT->nDirectories] = *new;                           //Add entry to array
    cs1550_directory_entry *alsonew = (cs1550_directory_entry*)malloc(sizeof(cs1550_directory_entry));
    memset(alsonew,0,sizeof(cs1550_directory_entry));                       //New directory block that holds the info from files
    int blocknum = findFreeDiskBlock();                                     //Position to put new directory block
    GOTO_BEGIN
    ROOT->directories[ROOT->nDirectories].nStartBlock = blocknum;           //Update location of directory in array entry
    ROOT->nDirectories++;                                                   //Update number of directories
    fwrite(ROOT,sizeof(cs1550_root_directory),1,fid);                       //Update the root in the disk file
    fseek(fid, blocknum*BLOCK_SIZE,SEEK_SET);                               //Move to free disk block
    fwrite(alsonew, sizeof(cs1550_directory_entry),1,fid);                  //Add the new directory to the disk file
    return 0;
}

/*
 * Removes a directory.
 */
static int cs1550_rmdir(const char *path)
{
    (void) path;
    return 0;
}

/*
 * Does the actual creation of a file. Mode and dev can be ignored.
 *
 */
static int cs1550_mknod(const char *path, mode_t mode, dev_t dev)
{
    (void) mode;
    (void) dev;
    (void) path;
    if (strcmp(path,"/") ==0)                                               //Cannot create a file in the root
        return -EPERM;

    char* directory = (char*)malloc(MAX_FILENAME +1);
    char* filename = (char*)malloc(MAX_FILENAME+1);
    char* extension = (char*)malloc(MAX_EXTENSION+1);
    sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
    if (strlen(filename) > MAX_FILENAME)
        return -ENAMETOOLONG;
    int i = 0;
    for (i =0; strcmp(ROOT->directories[i].dname, directory) != 0 && i < ROOT->nDirectories; i++);      //Find postion in array of current directory. Must exist if this is called
    cs1550_directory_entry *currDir = (cs1550_directory_entry *)malloc(sizeof(cs1550_directory_entry)); //Allocate space for current directory
    fseek(fid, ROOT->directories[i].nStartBlock*BLOCK_SIZE,SEEK_SET);
    fread(currDir,sizeof(cs1550_directory_entry),1,fid);                    //Read in current information on directory block
    if (currDir->nFiles == MAX_FILES_IN_DIR){                               //If this directory has maximum files, stop now 
        return -ENOSPC;
    }
    int dnum = i;                                                           //Store the position of directory
    for (i = 0; i < currDir->nFiles; i++){		                            //Check for same names in current directory
        if (strcmp(currDir->files[i].fname,filename) == 0){
            if (strcmp(currDir->files[i].fext,extension) == 0){
                return -ENOENT;                                             //File found matches filename and extension of path
            }
        }
    }

    strcpy(currDir->files[i].fname, filename);                              //Update name
    strcpy(currDir->files[i].fext, extension);                              //Update extension
    currDir->files[i].nIndexBlock = findFreeDiskBlock();                    //File needs an index block to store data block locations
    cs1550_index_block *currIndex = (cs1550_index_block *)malloc(sizeof(cs1550_index_block));
    memset(currIndex,0,sizeof(cs1550_index_block));                         //Ensure it starts empty
    currIndex->entries[0] = findFreeDiskBlock();                            //First data block of file
    currDir->files[i].fsize = 0;                                            //File starts with no data
    currDir->nFiles++;
    fseek(fid, ROOT->directories[dnum].nStartBlock*BLOCK_SIZE,SEEK_SET);    //Move to directory block
    fwrite(currDir,sizeof(cs1550_directory_entry), 1, fid);                 //Update directory on disk with new file information
    fseek(fid,currDir->files[i].nIndexBlock*BLOCK_SIZE,SEEK_SET);           //Move to index block location
    fwrite(currIndex,sizeof(cs1550_index_block),1,fid);                     //Write index block data
    return 0;
}

/*
 * Deletes a file
 */
static int cs1550_unlink(const char *path)
{
    (void) path;

    return 0;
}

/*
 * Read size bytes from file into buf starting from offset
 *
 */
static int cs1550_read(const char *path, char *buf, size_t size, off_t offset,
              struct fuse_file_info *fi)
{
    //The size variable is always a multiple of 4096. Our block size is 512. Is this size using the Linux block size?  
    (void) buf;
    (void) offset;
    (void) fi;
    (void) path;
    memset(buf,0,size);                                                     //Ensure buffer is empty
    char* directory = (char*)malloc(MAX_FILENAME +1);
    char* filename = (char*)malloc(MAX_FILENAME+1);
    char* extension = (char*)malloc(MAX_EXTENSION+1);
    //Check if name is subdirectory
    strcpy(filename,"0");
    strcpy(extension,"0");
    sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
    if (*filename !='0' && *extension != '0'){ 
        int i;  
        for (i =0; strcmp(ROOT->directories[i].dname, directory) != 0 && i < ROOT->nDirectories; i++);		//Find the directory from array of directories in root directory
        cs1550_directory_entry *currDir = (cs1550_directory_entry *)malloc(sizeof(cs1550_directory_entry));	//Allocate space for current directory
        fseek(fid, ROOT->directories[i].nStartBlock*BLOCK_SIZE,SEEK_SET);
        fread(currDir,sizeof(cs1550_directory_entry),1,fid);		//Initialize current directory
        int filenum = 0;
        for (i = 0; i < currDir->nFiles; i++){		//Find file in current directory
            if (strcmp(currDir->files[i].fname,filename) == 0){
                if (strcmp(currDir->files[i].fext,extension) == 0){
                    filenum = i;
                    break;
                }
            }
        }
        fseek(fid, currDir->files[filenum].nIndexBlock*BLOCK_SIZE,SEEK_SET);
        cs1550_index_block *currIndex = (cs1550_index_block *)malloc(sizeof(cs1550_index_block));
        memset(currIndex,0,sizeof(cs1550_index_block));                     //Ensure malloc returned 0s
        fread(currIndex,sizeof(cs1550_index_block),1,fid);                  //Optain index block
        int startBlock = offset/BLOCK_SIZE;                                 //Integer division. Finds the array position in index block. Ex: 1000/512 = 1. File covers 1 full block and % 512 is bytes used of other block
        int filesize = currDir->files[filenum].fsize;                       //Copy file size for easier access
        offset = offset % BLOCK_SIZE;                                       //Move the offset to be inside the block level. The extra offset value is handled by startBlock
        fseek(fid,currIndex->entries[startBlock]*BLOCK_SIZE+offset,SEEK_SET);   //Move to the starting postion of read
        
        if (size < BLOCK_SIZE - offset){                                    //If reading within the current block, simply do that
            fread(buf,size,1,fid);
            return size;
        } else if (filesize < BLOCK_SIZE){                                  //If reading entire file from inside 1 block, simply do that
            fread(buf,filesize,1,fid);
            return size;
        }else {
            int bytesRead = 0;                                              //Tracks how many bytes have been read into buf
            fread(buf,BLOCK_SIZE-offset,1,fid);                             //Read the rest of current block
            bytesRead += BLOCK_SIZE-offset;
            int k;
            for (k = startBlock+1;bytesRead < size; k++){                   //While there are still bytes to read from size
                if (bytesRead >= filesize)                                  //If we somehow read more bytes than filesize, stop immediately
                    return size;
                
                fseek(fid,currIndex->entries[k]*BLOCK_SIZE,SEEK_SET);       //Move to next block
                if (filesize - bytesRead < BLOCK_SIZE){                     //If file ends in this block, read the rest of the filesize
                    fread(buf+bytesRead,filesize-bytesRead,1,fid);		    //Finish the rest of the file; buf + bytes is pointer arthemetic to get to next read location
                    bytesRead += (filesize-bytesRead);
                } else if (size - bytesRead < BLOCK_SIZE){                  //If the size ends in this block
                    fread(buf+bytesRead,(size-bytesRead),1,fid);            //Finish the size
                    bytesRead += (size - bytesRead); 
                } else {                                                    //Otherwise read the whole block
                    fread(buf+bytesRead,BLOCK_SIZE,1,fid);
                    bytesRead += BLOCK_SIZE;
                }
            }

        }

    } else {
        return -EISDIR;
    }
    //check to make sure path exists
    //check that size is > 0
    //check that offset is <= to the file size
    //read in data
    //set size and return, or error

    return size;
}

/*
 * Write size bytes from buf into file starting from offset
 *
 */
static int cs1550_write(const char *path, const char *buf, size_t size,
              off_t offset, struct fuse_file_info *fi)
{
    (void) buf;
    (void) offset;
    (void) fi;
    (void) path;
    char* directory = (char*)malloc(MAX_FILENAME +1);
    char* filename = (char*)malloc(MAX_FILENAME+1);
    char* extension = (char*)malloc(MAX_EXTENSION+1);
    //Check if name is subdirectory
    strcpy(filename,"0");
    strcpy(extension,"0");
    sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
    if (*filename !='0' && *extension != '0'){
        int i;
        for (i =0; strcmp(ROOT->directories[i].dname, directory) != 0 && i < ROOT->nDirectories; i++);		//Find the directory from array of directories in root directory
        cs1550_directory_entry *currDir = (cs1550_directory_entry *)malloc(sizeof(cs1550_directory_entry));	//Allocate space for current directory
        fseek(fid, ROOT->directories[i].nStartBlock*BLOCK_SIZE,SEEK_SET);
        fread(currDir,sizeof(cs1550_directory_entry),1,fid);		//Initialize current directory
        int filenum = 0;
        int j = 0;
        for (j = 0; j < currDir->nFiles; j++){		//Find file in current directory
            if (strcmp(currDir->files[j].fname,filename) == 0){
                if (strcmp(currDir->files[j].fext,extension) == 0){
                    filenum = j;
                    break;
                }
            }
        }
        if (offset > currDir->files[filenum].fsize)                         //Max offset size is offset == filesize
            return -EFBIG;
        
        
        fseek(fid,currDir->files[filenum].nIndexBlock*BLOCK_SIZE,SEEK_SET);
        int freeFileBlock = currDir->files[filenum].fsize/BLOCK_SIZE;       //Find the open block in the file
        int blockBytesUsed = currDir->files[filenum].fsize % BLOCK_SIZE;    //Find where to continue write from
        currDir->files[filenum].fsize = offset + size;                      //In order to support overwrites, the new size cant add on to old size. New size is the starting distance from begining + size of write
        offset = offset % BLOCK_SIZE;                                       //Move offset onto block level
        cs1550_index_block *currIndex = (cs1550_index_block *)malloc(sizeof(cs1550_index_block));
        memset(currIndex,0,sizeof(cs1550_index_block));                     //Ensure index is empty before using
        fread(currIndex,sizeof(cs1550_index_block),1,fid);
        if (blockBytesUsed == 0){//In the case the write finishes and uses exactly the last byte in the block, a new block is not allocated at the end. Allocate the new block here
            currIndex->entries[freeFileBlock] = findFreeDiskBlock();
        }
        fseek(fid,currIndex->entries[freeFileBlock]*BLOCK_SIZE + offset,SEEK_SET);  //Move to first write location
        if (size < BLOCK_SIZE -offset){                                     //If the write is smaller than a block, simply do that
            fwrite(buf,size,1,fid);
        } else {
            int bytesWritten = 0;
            fwrite(buf,BLOCK_SIZE-offset,1,fid);                            //Fill the rest of the block
            bytesWritten += BLOCK_SIZE -offset;
            int k = 0;
            for (k = freeFileBlock+1; bytesWritten < size ; k++){
                if (k == MAX_ENTRIES_IN_INDEX_BLOCK){                       //If the file reaches maximum data blocks, stop immediately 
                    return -ENOSPC;
                }
                currIndex->entries[k] = findFreeDiskBlock();                //Allocate new data block
                fseek(fid,currIndex->entries[k]*BLOCK_SIZE,SEEK_SET);       //Move to new data block
                if ((size - bytesWritten) < BLOCK_SIZE){                    //Less than a block left to write
                    fwrite(buf+bytesWritten,size-bytesWritten,1,fid);       //buf+bytes is pointer arithmetic to find next bytes to write
                    bytesWritten += (size - bytesWritten);
                } else {                                                    //Write one block
                    fwrite(buf+bytesWritten,BLOCK_SIZE,1,fid);
                    bytesWritten += BLOCK_SIZE;
                }
            }
        }
        
        fseek(fid,currDir->files[filenum].nIndexBlock*BLOCK_SIZE,SEEK_SET); //Update disk 
        fwrite(currIndex,sizeof(cs1550_index_block),1,fid);
        fseek(fid, ROOT->directories[i].nStartBlock*BLOCK_SIZE,SEEK_SET);
        fwrite(currDir,sizeof(cs1550_directory_entry),1,fid);
    }
    //check to make sure path exists
    //check that size is > 0
    //check that offset is <= to the file size
    //write data
    //set size (should be same as input) and return, or error

    return size;
}

/*
 * truncate is called when a new file is created (with a 0 size) or when an
 * existing file is made shorter. We're not handling deleting files or
 * truncating existing ones, so all we need to do here is to initialize
 * the appropriate directory entry.
 *
 */
static int cs1550_truncate(const char *path, off_t size)
{
    (void) path;
    (void) size;

    return 0;
}


/*
 * Called when we open a file
 *
 */
static int cs1550_open(const char *path, struct fuse_file_info *fi)
{
    (void) path;
    (void) fi;
    /*
        //if we can't find the desired file, return an error
        return -ENOENT;
    */

    //It's not really necessary for this project to anything in open

    /* We're not going to worry about permissions for this project, but
       if we were and we don't have them to the file we should return an error

        return -EACCES;
    */

    return 0; //success!
}

/*
 * Called when close is called on a file descriptor, but because it might
 * have been dup'ed, this isn't a guarantee we won't ever need the file
 * again. For us, return success simply to avoid the unimplemented error
 * in the debug log.
 */
static int cs1550_flush (const char *path , struct fuse_file_info *fi)
{
    (void) path;
    (void) fi;

    return 0; //success!
}

/* Thanks to Mohammad Hasanzadeh Mofrad (@moh18) for these
   two functions */
static void * cs1550_init(struct fuse_conn_info* fi)
{
      (void) fi;
    printf("We're all gonna live from here ....\n");
    fid = fopen( ".disk", "rb+");	
    ROOT = (cs1550_root_directory *)malloc(sizeof(cs1550_root_directory));	//Allocate space for root pointers
    fread(ROOT, sizeof(cs1550_root_directory), 1, fid);                     //Read the root into the program, update struct
    GOTO_BITMAP
    bitmap = (cs1550_disk_block *)malloc(sizeof(cs1550_disk_block)*3);      //Allocate space for three disk blocks to serve as the bitmap
    fread(bitmap,sizeof(cs1550_disk_block)*3,1,fid);                        //Read in the bitmap
    if (bitmap[0].data[0] == (signed char)0x00)				                //Very first time, set 1 for root   
        bitmap[0].data[0] = 0x80;
    if (bitmap[2].data[MAX_DATA_IN_BLOCK-1] != (signed char)0x07)	        //and set last 3 for bitmap
        bitmap[2].data[MAX_DATA_IN_BLOCK-1] = 0x07;
    
    return NULL;
}

static void cs1550_destroy(void* args)
{
        (void) args;
    printf("... and die like a boss here\n");
    GOTO_BEGIN
    fwrite(ROOT,sizeof(cs1550_root_directory),1,fid);                       //Update any changes to the root
    GOTO_BITMAP
    fwrite(bitmap,sizeof(cs1550_disk_block)*3,1,fid);                       //Update changes to the bitmap
    
    fclose(fid);
}


//register our new functions as the implementations of the syscalls
static struct fuse_operations hello_oper = {
    .getattr	= cs1550_getattr,
    .readdir	= cs1550_readdir,
    .mkdir	= cs1550_mkdir,
        .rmdir = cs1550_rmdir,
    .read	= cs1550_read,
    .write	= cs1550_write,
    .mknod	= cs1550_mknod,
        .unlink = cs1550_unlink,
        .truncate = cs1550_truncate,
        .flush = cs1550_flush,
        .open	= cs1550_open,
    .init = cs1550_init,
    .destroy = cs1550_destroy,
};

//Don't change this.
int main(int argc, char *argv[])
{
    return fuse_main(argc, argv, &hello_oper, NULL);
}
