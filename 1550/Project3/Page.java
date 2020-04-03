import java.util.*;
public class Page{

    public boolean dirty;
    public boolean second;
    public long address;
    public LinkedList<Long> line;

    public Page(long addr){
        dirty = false;
        second = false;
        address = addr;
        line = new LinkedList<Long>();
    }

    public Page(long addr, boolean dirt){
        dirty = dirt;
        second = false;
        address = addr;
        line = new LinkedList<Long>();
    }

    public Page(long addr, boolean dirt, boolean sec){
        dirty = dirt;
        second = sec;
        address = addr;
        line = new LinkedList<Long>();
    }

    public Page(long addr, boolean sec, int junk){
        dirty = false;
        second = sec;
        address = addr;
        line = new LinkedList<Long>();
    }

    public String toString(){
        return  "addr = " + address + " Dirty = " + dirty + " second = " + second;
    }
} 