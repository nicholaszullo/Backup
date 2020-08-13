/** Improvements? 1ms, fastest is 0ms
 *	Runtime O(N) - Despite being nested for loops, the inner loop is only ran once, and worst case is only (N-1) + (N-2)
 *	Memory O(1)
 * 
 */
import java.util.Arrays;

public class NextPerm {
	public static void main(String[] args) {
		int[] nums = {2,2,7,5,4,3,2,2,1};
		nextPermutation(nums);
		for (int i : nums){
			System.out.print(i + " ");
		}
		System.out.print("\nExpected: [2,3,1,2,2,2,4,5,7]");
	}
	/** Change the order of the numbers in the array to the next possible largest number, or sort if already largest
	 * 
	 * @param nums the digits to use
	 * @return the array nums will be modified according to description
	 */
	public static void nextPermutation(int[] nums){
		int i = nums.length-1;
		for (; i > 0; i--){
			if (nums[i] > nums[i-1]){			//Find position where swap should occur
				int lowest = nums[i-1];
				int index = i;
				for (int k = i+1; k < nums.length; k++){				//Find the smallest number that is greater than num[i-1]
					if (nums[k] - lowest > 0 && nums[k]-lowest < nums[index] - lowest){
						index = k;
					}
				}
				int temp = nums[i-1];				//Swap num[i-1] with smallest number greater than num[i-1]
				nums[i-1] = nums[index];
				nums[index] = temp;
				break;
			}
		}										//Also accounts for case array is reverse order, sort in regular order
		Arrays.sort(nums,i, nums.length);					//Rest of array needs to be lowest possible combination, which is when it is sorted
	}

}
//System.out.println("checking " + nums[k] + " lowest " + lowest + " index " + nums[index]);