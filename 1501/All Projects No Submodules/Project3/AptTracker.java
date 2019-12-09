import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
/**
 *  A program to keep track of list of apartments, storing information about them. 
 *  It efficiently retrieves values to display to a user using heap based data structures.
 *  There is one <b>Priority Queue</b> used, and it stores all the apartments. Other elements
 *  are stored in seperate <b>heap</b> structures. No data structure is used from Java except
 *  basic arrays
 * 
 *  @author Nick Zullo njz12
 */
public class AptTracker
{
                                                                                //All are indexed by a hash value
    static IndexMinPQ<Apartment> pq = new IndexMinPQ<Apartment>(257);           //The priority queue storing by rent
    static MaxHeap maxArea = new MaxHeap(257);                                  //A max heap sorted by max square footage
    static City[] cities = new City[257];                                       //Each city will contain its own pq and maxArea
    public static void main(String[] args) throws Exception
    {
        int selection = 0;                                                      //User input selection
        BufferedReader input = new BufferedReader(new FileReader("apartments.txt"));
        Scanner scan = new Scanner(System.in);
        
        input.readLine();                                                       //Buffer from first line that is # in apartment file
        while (input.ready())                                                   //While there are more apartments to input
        {
            String line = input.readLine();                                     //Process the first line
            String[] seperate = line.split(":");                                //Splits the input into each segment and make a new Apartment based on those values 
            Apartment curr = new Apartment(seperate[1],seperate[0],seperate[2],Integer.parseInt(seperate[3]),Integer.parseInt(seperate[5]),Double.parseDouble(seperate[4]));
            
            maxArea.add(curr.getHash(), curr);                                  //Add Apartment to the max square footage heap
            pq.insert(curr.getHash(), curr);                                    //Add Apartment to the min rent PQ
            if (cities[curr.getCity().getHash()] == null)                       //If the first time seeing a given city
                cities[curr.getCity().getHash()] = curr.getCity();              //Create a new City, initializing the PQ and Heap in City()
            cities[curr.getCity().getHash()].getArea().add(curr.getHash(), curr);//Add to city structures
            cities[curr.getCity().getHash()].getRent().insert(curr.getHash(), curr);
            
        }
        input.close();                                                          //File is finished reading
     
        while (selection != 8)                                                  //Get user input from terminal based menu system
        {
            System.out.println("Enter a selection, enter 8 to exit program. Note the entries are case sensitive and avoid ending with spaces:");
            System.out.println("1) Add an apartment\t2) Update an apartment\t3)Remove an apartment\t4)Retrieve lowest Rent");
            System.out.println("5) Retireve highest ft^2\t6)Retrieve lowest rent in a city\t7)Retireve highest ft^2 in a city");
            System.out.println("8) Exit");
            selection = scan.nextInt();
            switch (selection)                                                  //Call the method for the user selection
            {
                case 1: { addApartment(scan); break;}
                case 2: { updateApartment(scan); break; }
                case 3: { removeApartment(scan); break; }
                case 4: { retrieveRent(); break; }
                case 5: { retrieveSqft(); break; }
                case 6: { retrieveRentCity(scan); break; }
                case 7: { retrieveSqftCity(scan); break; }
                case 8: { break;}
                default: { System.out.println("Invalid selection!"); break;}
            }
        
        }


        System.out.println("Goodbye");
    }

    private static void retrieveRentCity(Scanner scan) 
    {
        System.out.println("Enter the name of the city to search: ");           //Find the city based on a Horners method hash of the length of the city
        scan.nextLine();
        String city = scan.nextLine();
        int hash = 0;
        for (int i = 0; i < city.length(); i++)
        {
            int temp = city.charAt(i);
            temp *= 256;
            hash += temp;
        }
        hash %= 257;                                                            //Next prime number after 256, also the size of the hash array and PQ and heap
        System.out.println("\n"+cities[hash].getRent().minKey()+"\n");          //Retrieve the min rent from a city
    }

    private static void retrieveSqftCity(Scanner scan) {
        System.out.println("Enter the name of the city to search: ");
        scan.nextLine();
        String city = scan.nextLine();
        int hash = 0;
        for (int i = 0; i < city.length(); i++)
        {
            int temp = city.charAt(i);
            temp *= 256;
            hash += temp;
        }
        hash %= 257;

        System.out.println("\n"+cities[hash].getArea().retrieveTop()+"\n");
    }

    private static void retrieveRent() {
        System.out.println("\n"+pq.minKey() + "\n");
    }

    private static void retrieveSqft() {
        System.out.println("\n" + maxArea.retrieveTop() + "\n");
    }

    private static void removeApartment(Scanner scan) {
        System.out.println("Enter the street address: ");                       //Find a selected Apartment
        scan.nextLine();                                                        //Buffer for glitch with after nextInt
        String address = scan.nextLine();
        System.out.println("Enter the apartment number: ");
        String number = scan.nextLine();
        System.out.println("Enter the zip code: ");
        int zip = scan.nextInt();
        String combined = address.substring(0,4) + number + zip;                //Horner's method hash of the combined string
        int hash = 0;
        for (int i = 0; i < combined.length(); i++)             
        {
            int temp = combined.charAt(i);
            temp *= 256;
            hash += temp;
        }
        hash %= 257;

        cities[pq.keyOf(hash).getCity().getHash()].getRent().delete(hash);      //Remove apartment from each structure O(4log(N)) ~= O(log(N))
        cities[pq.keyOf(hash).getCity().getHash()].getArea().delete(hash);
        pq.delete(hash);
        maxArea.delete(hash);


    }

    private static void updateApartment(Scanner scan) {
        System.out.println("Enter the street address: ");                       //Find a selected apartment
        scan.nextLine();                                                        //Buffer for glitch with after nextInt
        String address = scan.nextLine();
        System.out.println("Enter the apartment number: ");
        String number = scan.nextLine();
        System.out.println("Enter the zip code: ");
        int zip = scan.nextInt();
        System.out.println("Enter the NEW rent in $: ");
        double rent = scan.nextDouble();
        String combined = address.substring(0,4) + number + zip;
        int hash = 0;
        for (int i = 0; i < combined.length(); i++)
        {
            int temp = combined.charAt(i);
            temp *= 256;
            hash += temp;
        }
        hash %= 257;

        pq.keyOf(hash).setCost(rent);                                           //Updating the rent of an object will change its value in all the strutures, but need to adjust the order
        pq.changeKey(hash, pq.keyOf(hash));                                     //Change key to the updated object, put it in new spot
        cities[pq.keyOf(hash).getCity().getHash()].getRent().changeKey(hash, pq.keyOf(hash));  


    }

    private static void addApartment(Scanner scan) {
        System.out.println("Enter the street address: ");                       //Stats about apartment given by user
        scan.nextLine();                                                        //Buffer for glitch with after nextInt
        String address = scan.nextLine();
        System.out.println("Enter the apartment number: ");
        String number = scan.nextLine();
        System.out.println("Enter the city name: ");
        String city = scan.nextLine();
        System.out.println("Enter the zip code: ");
        int zip = scan.nextInt();
        System.out.println("Enter the rent in $: ");
        double rent = scan.nextDouble();
        System.out.println("Enter the square footage: ");
        int area = scan.nextInt();

        Apartment curr = new Apartment(number, address, city, zip, area, rent); //Create a new Apartment

        pq.insert(curr.getHash(), curr);                                        //Add it to all the structures
        maxArea.add(curr.getHash(),curr);
        
        if (cities[curr.getCity().getHash()] == null)
            cities[curr.getCity().getHash()] = curr.getCity();
        cities[curr.getCity().getHash()].getArea().add(curr.getHash(), curr);
        cities[curr.getCity().getHash()].getRent().insert(curr.getHash(), curr);
    }


}