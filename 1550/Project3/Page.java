import java.util.*;
public class Page{

    public boolean dirty;                   //Dirty bit for page
    public boolean second;                  //Used bit
    public long address;                    //Page address
    public LinkedList<Long> line;           //Used for OPT, lines page is accessed on

    public Page(long addr){
        dirty = false;
        second = false;
        address = addr;
        line = new LinkedList<Long>();
    }

    //Helper print format to see page values
    public String toString(){
        return  "Addr = " + address + " Dirty = " + dirty + " Second = " + second;
    }
} 