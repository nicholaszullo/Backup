/**
*	O(N). Equalize the lengths then match the nodes.
*	3N because takes N time to calculate the length, which is done twice, then N to find the intersection
*/
public class IntersectionLists {
	public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int lengthA = 0;
        int lengthB = 0;
        for (ListNode currA = headA; currA != null; currA = currA.next){
            lengthA++;
        }
        for (ListNode currB = headB; currB != null; currB = currB.next){
            lengthB++;
        }
        if (lengthA > lengthB){
            for (int i = 0; i < lengthA-lengthB; i++){
                headA = headA.next;
            }
        } else {
            for (int i = 0; i < lengthB-lengthA; i++){
                headB = headB.next;
            }
        }
        for (ListNode currA = headA, currB = headB; currA != null && currB != null; currA = currA.next, currB = currB.next){
            if (currA == currB){
                return currA;
            }
        }
        return null;
    }
    }
}