/**
 * Given an array nums with n integers, your task is to check if it could become non-decreasing by modifying at most 1 element.
 * 
 * Runtime O(N)
 * Memory O(1)	leetcode says this uses a ton of memory?
 * 
 */

public class NondecreasingArray {

	public static void main(String[] args){
		int[] tester = {3,4,2,3};
		System.out.println(checkPossibility(tester));
		System.out.println("Expected " + "false");
		
			
	}
	public static boolean checkPossibility(int[] nums){
		int numOutOfPlace = 0;
		for(int i = 1; i < nums.length && numOutOfPlace<=1 ; i++){
			if(nums[i-1] > nums[i]){
				numOutOfPlace++;
				if (i-2<0 || nums[i-2] <= nums[i])
					nums[i-1] = nums[i];
				else 
					nums[i] = nums[i-1];
			}
		}
		if (numOutOfPlace <= 1)
			return true;
		else
			return false;
	}

}