/**
*	Fastest empirical runtime, but not fastest asymptotic. Probably able to avoid recalculating the min in remove
*	by storing data differently. 
*
*	Problem assumes that user wont call pop getMin or top on an empty stack
*
*/
class MinStack {
    private Node head;
    private Node min;	//Store the min for O(1) access
    
    /** initialize your data structure here. */
    public MinStack() {
        min = new Node(Integer.MAX_VALUE);
    }
    
    public void push(int x) {
        Node front = new Node(x);
        front.next = head;
        head = front;
        min = min.val < x ? min : front;        
    }
    
    public void pop() {
        if (min == head){		//If the min is about to be removed, recalculate the min
            min.val = Integer.MAX_VALUE;
            for (Node curr = head.next; curr != null; curr= curr.next){
                min = min.val < curr.val ? min : curr;
            }
            
        }
        head = head.next;
        
    }
    
    public int top() {
        return head.val;
    }
    
    public int getMin() {
        return min.val;
    }
}

class Node {
    public Node next;
    public int val;
    
    public Node(){
        this(null,0);
    }
    public Node(int val){
        this(null,val);
    }
    
    public Node(Node next, int val){
        this.next = next;
        this.val = val;
    }
    
    
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(x);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */