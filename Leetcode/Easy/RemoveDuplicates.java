/**	found same solution as leetcode! same strategy from PalandromeLinkedList. Use a fast and slow pointer to traverse in O(N) time
 * 	Runtime O(N)
 * 	Memory O(1)
 */

public class RemoveDuplicates {
	public static void main(String[] args) {
		int[] arr = {0,0,1,1,1,2,2,3,3,4};
		System.out.println(removeDuplicates(arr) + " expected 5");
	}
	public static int removeDuplicates(int[] nums) {
		int i = 0;
		for (int j = 1; j < nums.length; j++){
			if (nums[i] != nums[j]){
				nums[i+1] = nums[j];
				i++;
			}
		}
		return i+1;
	}
}