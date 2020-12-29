/**
*	O(N^2). Can improve to O(N)
*/
public class IntersectionLists {
	public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        for (ListNode currA = headA; currA != null; currA = currA.next){
            for (ListNode currB = headB; currB != null; currB = currB.next){
                if (currA == currB){
                    return currA;
                }   
            }           
        }
        return null;
    }
}