/**
 * 	Runtime O(N)
 * 	Memory O(N)
 */

public class AddTwoNumbers {
	private static boolean debugMode = false;
	public static void main(String[] args) {
		long val1 = 3494;			//Enter the number here normally, will be made reversed by while loop
		ListNode head1 = new ListNode((int)(val1%10));
		val1 /=10;
		ListNode curr = head1;
		while (val1 != 0){
			curr.next = new ListNode((int)(val1%10));
			curr = curr.next;
			val1 /= 10;
		}
		long val2 = 465;
		ListNode head2 = new ListNode((int)(val2%10));
		val2 /=10;
		ListNode curr2 = head2;
		while (val2 != 0){
			curr2.next = new ListNode((int)(val2%10));
			curr2 = curr2.next;
			val2 /= 10;
		}
		ListNode solution = addTwoNumbers(head1, head2);
		for (; solution != null; solution = solution.next){
			System.out.print(solution.val);
		}
	}

	public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode currAns = new ListNode(0);
		ListNode head = currAns;
		ListNode curr1 = l1;
		ListNode curr2 = l2;
		while (curr1 != null || curr2 != null){
			if (curr1 != null && curr2 != null){
				debug(curr1.val + " " + curr2.val + " curr vals");
				if (currAns.val + curr1.val + curr2.val < 10){
					currAns.val = currAns.val + curr1.val + curr2.val;
					debug(currAns.val + " no null");
					if (curr1.next != null || curr2.next != null){			//Dont add a leading 0 
						currAns.next = new ListNode(0);
						currAns = currAns.next;
					}
				} else {													//Overflow case
					currAns.next = new ListNode((currAns.val + curr1.val + curr2.val ) / 10);
					currAns.val = (currAns.val + curr1.val + curr2.val ) % 10;
					debug(currAns.val + " " + currAns.next.val + " no null");
					currAns = currAns.next;
				}
				curr1 = curr1.next;
				curr2 = curr2.next;
			} else if (curr1 == null){
				if (currAns.val + curr2.val < 10){
					currAns.val = currAns.val + curr2.val;
					debug(currAns.val + " curr1 null");
					if (curr2.next != null){
						currAns.next = new ListNode(0);
						currAns = currAns.next;
					}
				} else {
					currAns.next = new ListNode((currAns.val + curr2.val ) / 10);
					currAns.val = (currAns.val + curr2.val ) %10;
					debug(currAns.val + " " + currAns.next.val + " curr1 null");
					currAns = currAns.next;
				}
				curr2 = curr2.next;
			} else if (curr2 == null){
				if (currAns.val + curr1.val< 10){
					currAns.val = currAns.val + curr1.val;
					debug(currAns.val + " curr2 null");
					if (curr1.next != null){
						currAns.next = new ListNode(0);
						currAns = currAns.next;
					}
				} else {
					currAns.next = new ListNode((currAns.val + curr1.val) / 10);
					currAns.val = (currAns.val + curr1.val) %10;
					debug(currAns.val + " " + currAns.next.val + " curr2 null");
					currAns = currAns.next;
				}
				curr1 = curr1.next;
			}
		}
		return head;
	}
	private static void debug(String s){
		if (debugMode)
			System.out.println(s);
	}
}