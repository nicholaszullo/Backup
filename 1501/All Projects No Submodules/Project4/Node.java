public class Node
{
    private Vertex curr;                                                                    //Stores a vertex
    private Node next;                                                                      //Stores the next vertex in list

    public Node(Node other)
    {
        this.curr = other.curr;
        this.next = null;
    }

    public Node(Vertex curr, Node next)
    {
        this.curr = curr;
        this.next = next;
    }    

    public Node(Vertex curr)
    {
        this.curr = curr;
    }

    /**
     * @return the curr vertex
     */
    public Vertex getCurr() {
        return curr;
    }

    /**
     * @return the next vertex
     */
    public Node getNext() {
        return next;
    }
    /**
     * @param curr the curr to set
     */
    public void setCurr(Vertex curr) {
        this.curr = curr;
    }
    /**
     * @param next the next to set
     */
    public void setNext(Node next) {
        this.next = next;
    }

    

}