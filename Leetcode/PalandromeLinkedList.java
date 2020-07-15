import java.util.*;
/**
 * 	Current runtime O(2N) 
 * 	Current memory O(N)
 */
public class PalandromeLinkedList
{
	public static void main(String[] args)
	{
		ListNode head = new ListNode(1);
		ListNode second = new ListNode(2);
		ListNode third = new ListNode(2);
		//ListNode fourth = new ListNode(4);
		head.next = second;
		second.next = third;
		//third.next = fourth;
		System.out.println(isPalindrome(head));
	}

	public static boolean isPalindrome(ListNode head) {
        int num = 0;                //Number of nodes seen
		ArrayList<ListNode> storage = new ArrayList<ListNode>();
        for (ListNode curr = head; curr != null; curr = curr.next){
            num++;            
            storage.add(curr);
        }
        if (num %2 == 0) {
			for (int i = 0; i < num/2; i++){
				if (storage.get(i).val == (storage.get((num-1)-i)).val){
					continue;
				} else{
					return false;
				}
			}
		} else {
        	for (int i = 0; i < (num/2)+1; i++){
            	if (storage.get(i).val == (storage.get((num-1)-i)).val){
                	continue;
            	} else{
                	return false;
            	}
			}
		}
        return true;
        
    }

}