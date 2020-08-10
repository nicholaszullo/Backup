/**	Runtime O(N)
 * 	Memory O(1)
 * 
 */
public class SwapPairs {
	public static void main(String[] args) {
		ListNode head = new ListNode(1,null);
		ListNode curr = head;
		int i = 1;
		while (i< 10){
			curr.val = i++;
			curr.next = new ListNode();
			curr = curr.next;
		}
		curr.val = i;
		for (ListNode temp = head; temp != null; temp = temp.next){
			System.out.print(temp.val + " ");
		}
		System.out.println();
		ListNode newhead = swapPairs(head);
		for (ListNode temp = newhead; temp != null; temp = temp.next){
			System.out.print(temp.val + " ");
		}
	}
	/** For each adjacent 2 nodes, swap their order in the list. If odd length, the last node is not touched
	 * 
	 * @param head The start of the list
	 * @return the head node of the list
	 */
	public static ListNode swapPairs(ListNode head){
		ListNode temphead = new ListNode(0, head);
		ListNode curr = temphead;				//Focal point of operations is based on node before the 2 being swapped. for the begining, add a dummy node
		while (curr != null && curr.next != null && curr.next.next != null){			//If anything is null no switch possible so dont run
			ListNode first = curr.next;			//First node being swapped
			ListNode second = curr.next.next;	//Second node being swapped
			curr.next.next = second.next;		//first -> rest of list
			curr.next = second;					//begining of list -> second
			second.next = first;				//second -> first
			curr = first; 						//new focus point is before next swap
		}
		return temphead.next;
	}
}