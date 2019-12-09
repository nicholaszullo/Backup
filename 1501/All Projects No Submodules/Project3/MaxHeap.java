import java.util.NoSuchElementException;

/**
 *  A max heap of Apartments sorted by square footage
 *  Uses 1 based indexing. <code>arr</code> is the heap structure, and qp stores 
 *  the index that a given apartment is stored at in arr. <code>qp</code> is accessed
 *  based on the hash of an apartment
 * 
 * @author Nick Zullo
 */
public class MaxHeap
{
    private Apartment[] arr; 
    private int[] qp;
    
    private int num;                                                        //The next open spot in the array
    public MaxHeap(int size)
    {
        arr = new Apartment[size+1];
        qp = new int[size+1];
        for (int i = 0; i <= size; i++)                                     //Fill the qp array with -1 for a given hash is not found
            qp[i] = -1;
        num = 0;
    }
    public MaxHeap(int size, Apartment first)
    {
        arr = new Apartment[size+1];
        qp = new int[size+1];
        for (int i = 0; i <= size; i++)
            qp[i] = -1;
        add(first.getHash(), first);
    }

    public void add(int hash, Apartment curr)
    {
        num++;                                                              //Increment number of values stored
        qp[hash] = num;                                                     //The apartment with hash is stored at num 
        arr[num] = curr;                                                    //Stores the apartment at the end of the heap
        swim(num);                                                          //Move the apartment to its spot based on area
        
    }

    public Apartment retrieveTop()
    {
        if (num == 0) throw new NoSuchElementException("Priority queue underflow");
        return arr[1];                                                      //Top of heap is stored at 1
    }

    public void delete(int hash)
    {
        int index = qp[hash];
        exch(index, num--);                                                 //An apartment is deleted when it is moved out of the logical size of heap. Will be overwriten next time heap reaches that size
        swim(index);                                                        //Last element is moved to random position. Could need to move up
        sink(index);                                                        //Could need to move down
        qp[hash] = -1;                                                      //The apartment with hash is not found in heap
        
    }
    

    private void sink(int k) {
        while (2*k <= num) {                                                //While not at bottom
            int j = 2*k;                                                    //j is child
            if (j < num && less(j, j+1)) j++;                               //Make j the lesser child
            if (!less(k, j)) break;                                         //If the current value is greater, do not move it down
            exch(k, j);                                                     //Swap a lower value with a higher value
            k = j;                                                          //Curr is now the child
        }
    }
    private void swim(int k)
    {
        while (k > 1 && less(k/2, k)) {                                     //While not at top and parent is smaller than child
            exch(k, k/2);                                                   //Move child up
            k = k/2;
        }
    }

    private void exch(int first, int second) {
        Apartment temp = arr[second];                                       //Save values for swapping
        int temploc = qp[arr[second].getHash()];

        arr[second] = arr[first];                                           //Swap positions in heap
        arr[first] = temp;
        qp[temp.getHash()] = qp[arr[second].getHash()];                     //Swap the location of each apartment in heap
        qp[arr[second].getHash()] = temploc; 
    }

    private boolean less(int parent, int curr)
    {
        return arr[parent].getArea() < arr[curr].getArea();
    }

}