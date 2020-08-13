/** still 1ms, maybe would be 0ms if nums was restricted
 *	Runtime O(N) -  first version of this solution used an array of size 10 to store digits 0-9 and the occurance. Decreased inner loop runtime in case of duplicates 
		however, nums is not restricted in the length of each element (digits can be anything), so a hashmap was used for O(1) access 
 *	Memory O(N)
 * 
 */
import java.util.Arrays;
import java.util.HashMap;

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
		HashMap<Integer,Integer> occurance = new HashMap<Integer,Integer>();	//Store the last index a number appears
		int i = nums.length-1, max = -1;
		for (; i > 0; i--){
			max = max > nums[i] ? max : nums[i];					//Update highest number for limit on inner for loop
			occurance.put(nums[i], i);								//Update last seen
			occurance.put(nums[i-1], i-1);
			if (nums[i] > nums[i-1]){								//Find position where swap should occur
				int index = i;										//Initially select the next number after as smallest number greater than nums[i-1]
				for (int k = nums[i-1] +1; k <= max; k++){			//Start search at number after selected number. ideally would increment k to next number in hashmap, but how to do in O(1)? iterator has no guarantee
					if (!occurance.containsKey(k))					//skip if k is not in nums
						continue;
					if (nums[occurance.get(k)] - nums[i-1] > 0 && nums[occurance.get(k)] - nums[i-1] < nums[index] - nums[i-1]){
						index = occurance.get(k);					//If the current number is closer to selected number than previous current number, update index
					}
				}
				int temp = nums[i-1];								//Swap num[i-1] with smallest number greater than num[i-1]
				nums[i-1] = nums[index];
				nums[index] = temp;
				break;
			}
		}															//Also accounts for case array is reverse order, sort in regular order
		Arrays.sort(nums,i, nums.length);							//Sort into smallest order for numbers after where swap occured
	}

}