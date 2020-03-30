import java.io.*;
import java.util.*;

public class vmsim {
/**
 *   TODO:
 *      Make a RAM/Page Table
 *      HashMap for optimized lookups with OPT
 *          struct for each address? Hold line number that page is used
 *          One pass through input to find and record all used pages, store in a struct, store structs in a hashmap
 *          How tf to make a hashmap in C??
 *      Write OPT
 *      Write LRU
 *      Write SCA
 *      Possible actions - Page hit, Fault no evict, Fault evict clean, Fault evict dirty
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




    }
    
    public static void SCU(){




    }

}