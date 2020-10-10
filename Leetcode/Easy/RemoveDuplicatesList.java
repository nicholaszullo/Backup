public class RemoveDuplicatesList{
	
	public static void main(String[] args){
		ListNode last = new ListNode(3);
		ListNode a = new ListNode(3,last);
		ListNode b = new ListNode(2,a);
		ListNode c = new ListNode(1,b);
		ListNode d = new ListNode(1,c);
		deleteDuplicates(d);
		for (ListNode curr = d; curr != null; curr = curr.next){
			System.out.print(curr.val + "->");
		}
		
		
		
	}
	
	public static ListNode deleteDuplicates(ListNode head) {
		ListNode temp = new ListNode(0,head);
		
		for (ListNode curr = temp.next; curr != null;){
			if (curr.next != null && curr.val == curr.next.val){
				curr.next = curr.next.next;
			} else {
				curr = curr.next
			}
		}
		
		return temp.next;
		
    }
	
	
}