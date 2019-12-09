public class Vertex implements Comparable<Vertex>
{

    private Edge[] edgeTo;                                                  //The edgeTo array stores all the edges from this vertex to another vertex. It stores the other vertex at that vertex's ID
    private int id, num;

    public Vertex(int total, int id)
    {
        num = 0;
        edgeTo = new Edge[total];
        this.id = id;

    }

    public Vertex(int total, int id, int edge_id, Edge edge)
    {
        edgeTo = new Edge[total];
        this.id = id;
        addEdge(edge_id, edge);
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the edges
     */
    public Edge[] getEdges() {
        return edgeTo;
    }

    public void addEdge(int edge_id, Edge edge)
    {
        //System.out.println("linking vertex " + id + " to vertex " + edge_id);
        edgeTo[edge_id] = edge;
        num++;
    }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    @Override
    public int compareTo(Vertex other) {
        if (id > other.getId())
            return 1;
        else if (id < other.getId())
            return -1;
        return 0;
    }

}