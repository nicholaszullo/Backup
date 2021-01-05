/**	Remove occurances of val from linked list
*	Don't move curr when a remove happens because .next has changed and need to see if its back to back removes
*	1ms, but recurrsion is 0ms? how does recurrsion perform better than a for loop, there's stack overhead?
*/
public class RemoveTarget {
    public ListNode removeElements(ListNode head, int val) {
        ListNode dummy = new ListNode(0, head);
        for (ListNode curr = dummy; curr.next != null; ){
            if (curr.next.val == val){
                curr.next = curr.next.next;
            } else {
                curr = curr.next;
            }
        }
        return dummy.next;
    }
}