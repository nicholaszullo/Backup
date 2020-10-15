public class HasCycle {

	public boolean hasCycle(ListNode head) {
		//If  the 2 pointers ever cross, there is a cycle
        for (ListNode next = head, doubleNext = head; next != null && doubleNext != null && doubleNext.next != null; next = next.next, doubleNext = doubleNext.next.next){
            if (doubleNext.next == next){	//If doubleNext gets behind next, there is a cycle
                return true;
            }
        }
        return false;
    }

}