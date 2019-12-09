/**
 * Object of a given Apartment, stores info about an Apartment. No structures, just values
 * 
 * @author Nick Zullo
 */
public class Apartment implements Comparable<Apartment>
{

    private String address, num, combined;
    private int zip, area, hash;
    private double cost;
    private City city;

    public Apartment(String num, String address, String city, int zip, int area, double cost)
    {
        setNum(num);
        setAddress(address);
        setCity(city);
        setZip(zip);
        setArea(area);
        setCost(cost);
        setCombined();
        setHash();
    }

    /**
     *  Uses Horner's method to generate a hash for the apartment based on values searched
     */
    private void setHash()
    {
        hash = 0;
        for (int i = 0; i < combined.length(); i++)
        {
            int temp = combined.charAt(i);
            temp *= 256;
            hash += temp;
        }
        hash %= 257;
    }
    /**
     * @param num the num to set
     */
    public void setNum(String num) {
        this.num = num;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = new City(city);
    }
    /**
     * @param zip the zip to set
     */
    public void setZip(int zip) {
        if (zip > 0)
            this.zip = zip;
        else 
            throw new IllegalArgumentException("The zip code must be over 0!");
    }
    /**
     * @param area the area to set
     */
    public void setArea(int area) {
        if (area > 0)
            this.area = area;
        else 
            throw new IllegalArgumentException("The area must be greater than 0!");
    }
    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        if (cost > 0)
            this.cost = cost;
        else
            throw new IllegalArgumentException("The cost cannot be negative!");
    }

    /**
     *  The string of values of apartment used to create a hash
     */
    private void setCombined()
    {
        combined = address.substring(0,4) + num + zip;
    }

    /**
     * @return the area
     */
    public int getArea() {
        return area;
    }

    /**
     * @return the key
     */
    public int getHash() {
        return hash;
    }

    /**
     * @return the city
     */
    public City getCity() {
        return city;
    }
    /**
     * @return the combined
     */
    public String getCombined() {
        return combined;
    }
    public String toString()
    {
        return "Some info on this apartment: " + address +", "+ num + ", "+ city +", "+ zip +", " + cost +", "+ area;
    }

    /**
     *  Used by IndexMinPQ to sort the PQ based on rent
     *  
     */
    public int compareTo(Apartment other) {
        if (cost < other.cost)
            return -1;
        else if (cost > other.cost)
            return 1;
        return 0;        
    }


}