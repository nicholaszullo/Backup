/**	| -> | -> |
* 	| <- | -> |
*	Save pointer to right, move pointer from mid.next to start, move start to mid and mid to right
*	At the begining special case move head to null, and at end special case move end to second to last
*/
public class ReverseLinkedList {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode start = head;
        ListNode mid = start.next;
        start.next = null;
        while (mid.next != null){
            ListNode right = mid.next;
            mid.next = start;
            start = mid;
            mid = right;
        }
        mid.next = start;
        return mid;
    }
}