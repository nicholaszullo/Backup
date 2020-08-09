/** 2 passes is still 0 ms, but it can be done in 1. Find out how!
 * 	Runtime O(N^2)
 * 	Memory O(1)
 */
public class RemoveNthFromEnd {
	public static void main(String[] args) {
		
	}
	public static ListNode removeNthFromEnd(ListNode head, int n) {
		ListNode prevNth = null;
		int num = 0;
		for (ListNode curr = head; curr != null; curr = curr.next, num++);			//Find length
		int size = num;
		num = 0;
		for (ListNode curr = head; curr != null && num != size-n; curr = curr.next, num++){		//Find node before removal
			prevNth = curr;
		}
		if (prevNth == null){					//If null remove head
			return head.next;
		}
		if (prevNth.next == null || prevNth.next.next == null){		//If at end of list set end to null
			prevNth.next = null;
		} else {
			prevNth.next = prevNth.next.next;			//Link node after removal to this
		}
		return head;
		

    }
}