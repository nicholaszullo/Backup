import java.util.*;
/**
 * 	Current runtime O(N) 
 * 	Current memory O(N/2)	Can only be O(1) if you destroy the input - not good practice?
 * 							Less memory if use exact array, or trimToSize()? Leetcode says no
 */
public class PalandromeLinkedList
{
	public static void main(String[] args)
	{
		ListNode head = new ListNode(1);
		ListNode second = new ListNode(2);
		ListNode third = new ListNode(2);
		ListNode fourth = new ListNode(1);
		//ListNode fifth = new ListNode(1);
		head.next = second;
		second.next = third;
		third.next = fourth;
		//fourth.next = fifth;
		System.out.println(isPalindrome(head));
		
	}

	public static boolean isPalindrome(ListNode head) {
		ListNode middle = head;										//Stops at the middle
		ListNode end = head;										//Stops at the end
		ArrayList<ListNode> storage = new ArrayList<ListNode>();
		int i = 0;													//Number of nodes in first half 
		for (; end != null && end.next != null; end = end.next.next, middle = middle.next){		//Move one pointer to end of list, one to middle
			storage.add(middle);									//Add first half nodes to a list for reverse traversal 
			i++;
			System.out.print(i + " ");
		}
		//storage.trimToSize();		Made no difference in memory usage according to leetcode
		i--;														//i needs to be backed up by 1 so it starts count from 0. i.e. 0 1 instead of 1 2 
		if (end != null){											//Odd case
			middle = middle.next;									//Ignore the middle node in the odd case
			for (;middle != null; middle = middle.next){
				if (!(storage.get(i--).val == middle.val)){			//Move backwards in array and forwards in second half of linkedlist
					return false;
				}
			}
		} else {													//Even case
			for (;middle != null; middle = middle.next){
				if (!(storage.get(i--).val == middle.val)){
					return false;
				}
			}
		}
		return true;
	}

}