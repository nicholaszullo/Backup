/** 1 Pass
 * 	Runtime O(N)
 * 	Memory O(1)
 */
public class RemoveNthFromEnd {
	public static void main(String[] args) {
		
	}
	public static ListNode removeNthFromEnd(ListNode head, int n) {
		ListNode fast = head, slow = head;
		int seen = 0;
		for (; fast != null && seen <= n; fast = fast.next, seen++);		//Put a gap of n between slow and fast
		for (; fast != null; fast = fast.next, slow = slow.next);			//Move fast to the end of the list, bringing slow along with it
        if (seen == n){			//If second for never ran, seen == n, which means remove head
            return head.next;
        }
		slow.next = slow.next.next;			//Unlink node to be removed
		return head;			//Return the front of the list
    }
}