
//A node for the DLB Linked List
public class Node
{
    public Node nextData;
    public Node otherNode;
    public char data;
    public int freq;

    public Node(char val, int num)                          //Construtor to create a node with a given char and frequency
    {
        nextData = null;
        otherNode = null;
        data = val;
        freq = num;
    }
    public Node(Node copy)                                  //Constructor to make a deep copy of a node 
    {
        nextData = copy.nextData;
        otherNode = copy.otherNode;
        data = copy.data;
        freq = copy.freq;
    }

}