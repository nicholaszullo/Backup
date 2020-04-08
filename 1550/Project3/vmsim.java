import java.io.*;
import java.util.*;

public class vmsim {
/**
 *   TODO:
 *      Change Second to array for better performance (Optional)
 */

    static Scanner scan;   
    static HashMap<Long, Page> table;
    static int framenum;                //Number of frames used
    static int accesses;                //Number of memory accesses 
    static int pagefaults;              //Number of page faults
    static int writes;                  //Number of writes back to disk
    public static void main(String[] args) {                //-n framenum -a algorithm input.txt
        framenum = Integer.parseInt(args[1]);               //Assume user knows how to give input, dont check for errors 
        String algorithm = args[3].toLowerCase();   
        try {
            scan = new Scanner(new File(args[4]));
        } catch (FileNotFoundException e) {
            System.out.println("PANIC: FILE NOT FOUND!");
            System.exit(0);
        }


        if (algorithm.equals("lru"))
            LRU();
        else if (algorithm.equals("opt")){
            fillHash(args[4]);                              //Fill hash table with values before OPT
            OPT();
        }else if (algorithm.equals("second"))
            SCU();

        //Print results in autograder format
        System.out.println("Algorithm: " + algorithm.toUpperCase() + "\nNumber of frames: "+framenum);
        System.out.println("Total memory accesses: " + accesses + "\nTotal page faults: " + pagefaults + "\nTotal writes to disk: " + writes);
        
    }

    public static void LRU(){
        LinkedList<Page> pages = new LinkedList<Page>();    //LinkedList used to store page table. First is most recently used, last is least recently used 
        while (scan.hasNext()){
            accesses++;
            char mode = scan.next().charAt(0);              //Mode is a single char but scanner reads as string
            long address = Long.decode(scan.next());        //Hex to long
            address = address >>> 12;                       //Remove offset bits
            boolean hit = false;                            //Reset a hit each loop 

            for (int i = 0; i < pages.size(); i++){         //Look in page table for page requested
                if (address == pages.get(i).address){       //Found page
                    hit = true;
                    if (mode == 's'){                       //Set dirty bit when a store
                        pages.get(i).dirty = true;
                    }
                    pages.addFirst(pages.remove(i));        //Move used page to front of list
                    break;                                  //No need to search rest of list once page is found
                }
            }
            if (hit){                                       //Don't do any page removal on a hit
                continue;
            }
            pagefaults++;
            if (pages.size() < framenum){                   //Room in the page table
                pages.addFirst(new Page(address));
                if (mode == 's')
                    pages.getFirst().dirty = true;                    
              
            } else if (pages.size() == framenum){           //Page table is full
                pages.addFirst(new Page(address));
                if (mode == 's')
                    pages.getFirst().dirty = true; 
                
                Page holder = pages.removeLast();           //Remove Least Recently Used page
                if (holder.dirty){                          //Write to disk on removal
                    writes++;
                }
            } else {                                        //More frames in page table than specified
                System.out.println("PANIC: MORE FRAMES USED THAN GIVEN");
                System.exit(0);
            }

        }  

    }

    public static void fillHash(String input){
        Scanner read = null;                                //Initalize for compiler but would exit if not initialied before while()
        try {
            read = new Scanner(new File(input));
        } catch (FileNotFoundException e) {
            System.out.println("PANIC: FILE NOT FOUND!");
            System.exit(0);
        }

        table = new HashMap<Long, Page>();
        accesses = 0;                                       //If another algorithm was somehow used before this, clear accesses
        while (read.hasNext()){
            accesses++;
            char mode = read.next().charAt(0);
            long address = Long.decode(read.next());
            address = address >>> 12;
            
            if (!table.containsKey(address)){               //If the page has not been used yet, create a new object for it
                table.put(address, new Page(address));
            }
            table.get(address).line.add((long)accesses);    //Add access line to LinkedList of accesses for that page

        }
        
        accesses = 0;                                       //Reset accesses for use with opt 
       
    }

    public static void OPT(){
        LinkedList<Page> pages = new LinkedList<Page>();
        while (scan.hasNext()){
            accesses++;
            char mode = scan.next().charAt(0);
            long address = Long.decode(scan.next());
            address = address >>> 12;
            boolean hit = false;

            for (int i = 0; i < pages.size(); i++){
                if (address == pages.get(i).address){       //Found page
                    hit = true;
                    if (mode == 's'){                       //Set dirty bit when a store
                        pages.get(i).dirty = true;
                    }
                    if (pages.get(i).line.size() !=0 )      //Remove this access from list of accesses unless none are left
                        pages.get(i).line.removeFirst();
                    break;                                  //No need to search rest of list once page is found
                }
                
            }
            if (hit)                                        //No page replacement on hit
                continue;
            
            pagefaults++;
            if (pages.size() < framenum){
                if (mode == 's')
                    table.get(address).dirty = true;

                pages.add(table.get(address));              //Must add the same object from table
                if (table.get(address).line.size() != 0)
                    table.get(address).line.removeFirst();  //Remove this access from list unless none are left

            } else if (pages.size() == framenum){           //Page to remove is one with largest distTo 
                int remove = 0;                             //Index of page to remove
                long largestDist = 0;                       //Holds the distance to next access of current address in page table
                for (int i = 0; i < pages.size(); i++){
                    if (pages.get(i).line.size() == 0){
                        remove = i;                         //Will inherently select the LRU page, because any page which is no longer used will have a size of 0 and update remove. the LRU page will be at the end
                        largestDist = 10000000;
                        continue;                           //Check next page
                    }
                    if (largestDist < (pages.get(i).line.getFirst()-accesses)){     //If the current page has a larger distTo than previous highest, update values
                        largestDist = pages.get(i).line.getFirst()-accesses;        //DistTo is next access in list - currrent position
                        remove = i;                                                 //Page to remove is this page
                    }  
                }
                Page holder = pages.remove(remove);         
                
                if (holder.dirty){                          //Write to disk on removal
                    writes++;
                    holder.dirty = false;                   //Reset the dirty bit for this page object
                }
                if (mode == 's')
                    table.get(address).dirty = true;        //Update dirty bit for this page object
                
                pages.add(table.get(address));              //Add the page object from hashtable
                if (table.get(address).line.size() != 0)
                    table.get(address).line.removeFirst();      //Remove this access from list of accesses unless none left
            } else if (pages.size() > framenum){                                        //More frames in page table than specified
                System.out.println("PANIC: MORE FRAMES USED THAN GIVEN");
                System.exit(0);
            }
        }


    }
    
    public static void SCU(){
        LinkedList<Page> pages = new LinkedList<Page>();    //More efficient to convert to array
        int clock = 0;                                      //Current index position of "clock" pointer
        while (scan.hasNext()){
            accesses++;
            char mode = scan.next().charAt(0);
            long address = Long.decode(scan.next());
            address = address >>> 12;
            boolean hit = false;

            for (int i = 0; i < pages.size(); i++){       
                if (address == pages.get(i).address){
                    hit = true;
                    if (mode == 's'){
                        pages.get(i).dirty = true;
                    }
                    pages.get(i).second = true;             //Update used bit to true
                    break;
                }
            }

            if (hit){
                continue;
            }
            pagefaults++;

            if (pages.size() < framenum){
                pages.add(clock++, new Page(address));      //Add page at position of clock, and then move pointer to next position
                if (mode == 's')
                    pages.get((clock-1)).dirty = true;      //Update dirty bit of added page if needed
                
            } else if (pages.size() == framenum){
                Page removed = null;
                for (int i = 0; i < pages.size(); i++){
                    if (clock == pages.size())              //Loop clock back to begining if reached end of list
                        clock = 0;
                    if (pages.get(clock).second){           //Check used bit
                        pages.get(clock).second = false;    //Reset it if true
                        clock++;                            //Move to next position
                        if (clock == pages.size())          //Reset it here for case loop is about to exit and remove is null
                            clock = 0;
                    } else {
                        removed = pages.remove(clock); 
                        break;                              //Dont continue the search 
                    }
                }
                
                if (removed == null){                       //All pages were used, so clock looped to where it started and start page bit is now 0
                    removed = pages.remove(clock);
                }
                if (removed.dirty)                          //Write back
                    writes++;
                
                pages.add(clock++, new Page(address));      //Add page at position of clock, and then move pointer to next position
                if (mode == 's')
                    pages.get((clock-1)).dirty = true;

            } else {
                System.out.println("PANIC: MORE FRAMES USED THAN GIVEN");
                System.exit(0);
            }
        }
    }

}