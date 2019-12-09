public class Edge implements Comparable<Edge>
{
    private String type;
    private int start, end, length, bandwidth, speed;

    public Edge(int start, int end, String type, int bandwidth, int length)
    {
        this.start = start;
        this.end = end;
        this.type = type;
        this.bandwidth = bandwidth;
        this.length = length;
        setSpeed();
    }

    private void setSpeed()
    {
        if (type.equals("optical"))
            speed = 200000000;
        else if (type.equals("copper"))
            speed = 230000000;
        else 
            throw new IllegalArgumentException("Invalid cable material! Fix the input file");
    }

    

    /**
     * @return the time to travel a path
     */
    public double getTime() {
        return ((double)length/speed);
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the bandwidth
     */
    public int getBandwidth() {
        return bandwidth;
    }
    @Override
    public int compareTo(Edge other) {
        if (speed > other.getTime())
            return 1;
        else if (speed < other.speed)
            return -1;
        else
            return 0;
    }
}