import java.io.*;
import java.util.*;

public class vmsim {
/**
 *   TODO:
 *      HashMap for optimized lookups with OPT
 *      Write OPT
 *      When to write dirty pages that do not get evicted??
 *      Confirm output numbers are correct?
 */

    static Scanner scan;
    static Page[] table;
    static int framenum;               //Number of frames used
    static int accesses;               //Number of memory accesses 
    static int pagefaults;             //Number of page faults
    static int writes;                 //Number of writes back to disk
    public static void main(String[] args) {
        framenum = Integer.parseInt(args[1]);
        String algorithm = args[3].toLowerCase();
        table = new Page[framenum];
        try {
            scan = new Scanner(new File(args[4]));
        } catch (FileNotFoundException e) {
            System.out.println("PANIC: FILE NOT FOUND!");
            System.exit(0);
        }


        if (algorithm.equals("lru"))
            LRU();
        else if (algorithm.equals("opt"))
            OPT();
        else if (algorithm.equals("second"))
            SCU();

        System.out.println("Algorithm: " + algorithm + "\nNumber of frames:\t"+framenum);
        System.out.println("Total memory accesses: " + accesses + "\nTotal page faults:\t" + pagefaults + "\nTotal writes to disk:  " + writes);
        
    }

    public static void LRU(){
        LinkedList<Page> pages = new LinkedList<Page>();
        while (scan.hasNext()){
            accesses++;
            char mode = scan.next().charAt(0);
            long address = Long.decode(scan.next());
            address = address >> 12;
            address = address & 0x000fffff;
            boolean hit = false;

            for (int i = 0; i < pages.size(); i++){
                if (address == pages.get(i).address){
                    hit = true;
           //         System.out.println("PAGE HIT!");
                    if (mode == 's'){
                        pages.get(i).dirty = true;
                    }
                    pages.addFirst(pages.remove(i));        //Move used page to front of list
                }
            }
            if (hit){
                continue;
            }
            pagefaults++;
            if (pages.size() < framenum){
          //      System.out.println("Page Fault adding new page");
                pages.addFirst(new Page(address));
            } else if (pages.size() == framenum){  
                pages.addFirst(new Page(address));   
                Page holder = pages.removeLast();
            //    System.out.println("Evicting page address " + holder.address);
                if (holder.dirty){
                    writes++;
                }
            } else {
                System.out.println("ERROR MORE FRAMES USED THAN GIVEN");
                System.exit(0);
            }

        }  
       

    }

    public static void OPT(){
        
        while (scan.hasNext()){
            accesses++;
            char mode = scan.next().charAt(0);
            long address = Long.decode(scan.next());
            address = address >> 12;
            address = address & 0x000fffff;
            boolean hit = false;





        }


    }
    
    public static void SCU(){
        LinkedList<Page> pages = new LinkedList<Page>();
        while (scan.hasNext()){
            accesses++;
            char mode = scan.next().charAt(0);
            long address = Long.decode(scan.next());
            address = address >> 12;
            address = address & 0x000fffff;
            boolean hit = false;

            for (int i = 0; i < pages.size(); i++){
                if (address == pages.get(i).address){
                    hit = true;
           //         System.out.println("PAGE HIT!");
                    if (mode == 's'){
                        pages.get(i).dirty = true;
                    }
                    pages.get(i).second = true;
                }
            }

            if (hit){
                continue;
            }
            pagefaults++;
            if (pages.size() < framenum){
          //      System.out.println("Page Fault adding new page");
                pages.add(new Page(address));
            } else if (pages.size() == framenum){    
                Page holder = null;     
                for (int i = 0; i < pages.size(); i++){
                    if (pages.get(i).second == true)
                        pages.get(i).second = false;
                    else 
                        holder = pages.remove(i);
                }
                if (holder == null){
                    for (int i = 0; i < pages.size(); i++){
                        if (pages.get(i).second == false)
                            holder = pages.remove(i);
                    }
                }
            //    System.out.println("Evicting page address " + holder.address);
                if (holder.dirty){
                    writes++;
                }
                pages.add(new Page(address));
            } else {
                System.out.println("ERROR MORE FRAMES USED THAN GIVEN");
                System.exit(0);
            }


        }




    }

}