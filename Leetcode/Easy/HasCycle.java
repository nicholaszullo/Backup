public class HasCycle {

	public boolean hasCycle(ListNode head) {
        HashSet<ListNode> seen = new HashSet<ListNode>();
        for (ListNode curr = head; curr != null; curr = curr.next){
            if (!seen.add(curr)){
                return true;
            }
        }
        return false;
    }

}