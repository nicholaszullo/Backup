import java.io.*;
import java.util.*;

public class vmsim {
/**
 *   TODO:
 *      HashMap for optimized lookups with OPT
 *      Write OPT
 *      SCU close but not quite matching output
 */

    static Scanner scan;
    static HashMap<Long, Page> table;
    static int framenum;               //Number of frames used
    static int accesses;               //Number of memory accesses 
    static int pagefaults;             //Number of page faults
    static int writes;                 //Number of writes back to disk
    public static void main(String[] args) {
        framenum = Integer.parseInt(args[1]);
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
            fillHash(args[4]);
            OPT();
        }else if (algorithm.equals("second"))
            SCU();

        System.out.println("Algorithm: " + algorithm.toUpperCase() + "\nNumber of frames: "+framenum);
        System.out.println("Total memory accesses: " + accesses + "\nTotal page faults: " + pagefaults + "\nTotal writes to disk: " + writes);
        
    }

    public static void LRU(){
        LinkedList<Page> pages = new LinkedList<Page>();
        while (scan.hasNext()){
            accesses++;
            char mode = scan.next().charAt(0);              //Mode is a single char but scanner reads as string
            long address = Long.decode(scan.next());        //Hex to long
            address = address >>> 12;                        //Remove offset bits
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
                if (mode == 's'){                           //Add new page with a true dirty bit or not
                    pages.addFirst(new Page(address, true));
                } else{
                    pages.addFirst(new Page(address));  
                }
              
            } else if (pages.size() == framenum){           //Page table is full
                if (mode == 's'){
                    pages.addFirst(new Page(address, true));
                } else {
                    pages.addFirst(new Page(address));  
                }
                
                Page holder = pages.removeLast();           //Remove Least Recently Used page
                if (holder.dirty){                          //Write to disk on removal
                    writes++;
                }
            } else {                                        //More frames in page table than specified
                System.out.println("ERROR MORE FRAMES USED THAN GIVEN");
                System.exit(0);
            }

        }  

    }

    public static void fillHash(String input){
        Scanner read = null;
        try {
            read = new Scanner(new File(input));
        } catch (FileNotFoundException e) {
            System.out.println("PANIC: FILE NOT FOUND!");
            System.exit(0);
        }

        table = new HashMap<Long, Page>();
        accesses = 0;
        while (read.hasNext()){
            accesses++;
            char mode = read.next().charAt(0);
            long address = Long.decode(read.next());
            address = address >>> 12;
            
            if (!table.containsKey(address)){
                table.put(address, new Page(address));
            }
            table.get(address).line.add((long)accesses);

        }
        
        accesses = 0;
       
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
                    if (pages.get(i).line.size() !=0 )
                        pages.get(i).line.removeFirst();
                    break;                                  //No need to search rest of list once page is found
                }
                
            }
            if (hit)
                continue;
            
            pagefaults++;
            if (pages.size() < framenum){
                if (mode == 's')
                    table.get(address).dirty = true;

                pages.add(table.get(address));
                if (table.get(address).line.size() != 0)
                    table.get(address).line.removeFirst();

            } else if (pages.size() == framenum){   //Page to remove is one with largest distTo 
                int remove = 0;
                long largestDist = 0;
                for (int i = 0; i < pages.size(); i++){
                    if (pages.get(i).line.size() == 0){
                        remove = i;
                        largestDist = 10000000;
                        continue;
                    }
                    if (largestDist < (pages.get(i).line.getFirst()-accesses)){
                        largestDist = pages.get(i).line.getFirst()-accesses;        //DistTo is next access in list - currrent position
                        remove = i;                                                 //Page to remove
                    }
                   
                }

                Page holder = pages.remove(remove);
                
                if (holder.dirty){                          //Write to disk on removal
                    writes++;
                    holder.dirty = false;
                }
                if (mode == 's')
                    table.get(address).dirty = true;
                
                pages.add(table.get(address));
                table.get(address).line.removeFirst();
            } else if (pages.size() > framenum){                                        //More frames in page table than specified
                System.out.println("ERROR MORE FRAMES USED THAN GIVEN");
                System.exit(0);
            }



        }


    }
    
    //NOT MATCHING OUTPUT ON PIAZZA
    public static void SCU(){
        LinkedList<Page> pages = new LinkedList<Page>();
        int clock = 0;
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
                    pages.get(i).second = true;
                    break;
                }
            }

            if (hit){
                continue;
            }
            pagefaults++;
            if (pages.size() < framenum){
                if (mode == 's')
                    pages.add(clock++,new Page(address, true));
                else 
                    pages.add(clock++,new Page(address));
                
            } else if (pages.size() == framenum){
                Page removed = null;
                for (int i = 0; i < pages.size(); i++){
                    if (clock == pages.size())
                        clock = 0;
                    if (pages.get(clock).second){
                        pages.get(clock).second = false;
                        clock++;
                        if (clock == pages.size())
                            clock = 0;
                    } else {
                        removed = pages.remove(clock); 
                        break;                                          //Dont continue the search 
                    }
                }
                
                if (removed == null){
                    removed = pages.remove(clock);
                }
                if (removed.dirty)
                    writes++;
                
                if (mode == 's')
                    pages.add(clock++,new Page(address, true));
                else 
                    pages.add(clock++,new Page(address));

            } else {
                System.out.println("ERROR MORE FRAMES USED THAN GIVEN");
                System.exit(0);
            }


        }




    }

}