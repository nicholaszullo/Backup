/**
 *  A city object storing the name of the city and 2 data structures of Apartments
 * 
 *  @author Nick Zullo
 */
public class City
{

    private String name;
    private int hash;
    private IndexMinPQ<Apartment> rent;
    private MaxHeap area;

    public City(String name)
    {
        this.name = name;
        rent = new IndexMinPQ<Apartment>(257);
        area = new MaxHeap(257);
        generateHash();
    }

    private void generateHash()
    {
        hash = 0;
        for (int i = 0; i < name.length(); i++)
        {
            int temp = name.charAt(i);
            temp *= 256;
            hash += temp;
        }
        hash %= 257;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the rent PQ
     */
    public IndexMinPQ<Apartment> getRent() {
        return rent;
    }

    /**
     * @return the area Heap
     */
    public MaxHeap getArea() {
        return area;
    }

    /**
     * @return the hash
     */
    public int getHash() {
        return hash;
    }

    public String toString()
    {
        return name;
    }
}