/**
 * Given an array nums with n integers, your task is to check if it could become non-decreasing by modifying at most 1 element.
 * 
 * Runtime O(N^2)
 * Memory O(1)
 * 
 */

public class NondecreasingArray {

    public static void main(String[] args){
        int[] tester = {-1,4,2,3};
        System.out.println(checkPossibility(tester));
        System.out.println("Expected " + "true");
        
			
    }
    public static boolean checkPossibility(int[] nums){
		if (nums.length == 1)
			return true;
		int previous = 0;
		int after = 0;
		int changed = 0;
		boolean swapped = false;
		boolean marked = false;
		for (int i = 0; i < nums.length; i++){
			if (i == 0)
				previous = 10000000;
			else
				previous = nums[i-1];
			if (i == nums.length-1)
				after = 10000000;
			else 	
				after = nums[i+1];
			if (previous < 10000000) {
				if (nums[i] < previous){
					swapped = true;
					changed = nums[i];
					nums[i] = previous+1;
					System.out.println("changed " + changed + " to " + nums[i] + " i = " + i);
				}
			} 
			if (after < 10000000 && !swapped){
				if (nums[i] > after){
					swapped = true;
					changed = nums[i];
					nums[i] = after-1;
					System.out.println("changed " + changed + " to " + nums[i] + " i = " + i);
				}
			}
			
			for (int k : nums){
				System.out.print(k + " ");
			}
			System.out.println();
			
			for (int j = 1; j < nums.length; j++){
				if (nums[j-1] > nums[j]){
					marked = false;
					break;
				} else if (j == nums.length-1){
					marked = true;
					return true;
				}
				
			}
			if (swapped){
				nums[i] = changed;
				swapped = false;
			}
			System.out.print("Restored ");
			for (int k : nums){
				System.out.print(k + " ");
			}
			System.out.println();
		}
        return marked;
    }

}