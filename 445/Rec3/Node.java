public class Node<T>
{
     private T data;
     private Node<T> next;

     public Node(T dataPortion)
     {
          this(dataPortion, null);
     } // end constructor

     public Node(T dataPortion, Node<T> nextNode)
     {
          data = dataPortion;
          next = nextNode;
     } // end constructor

     public T getData()
     {
          return data;
     } // end getData

     public void setData(T newData)
     {
          data = newData;
     } // end setData

     public Node<T> getNextNode()
     {
          return next;
     } // end getNextNode

     public void setNextNode(Node<T> nextNode)
     {
          next = nextNode;
     } // end setNextNode
} // end Node
