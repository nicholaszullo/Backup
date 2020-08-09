/**	Do a triple sum to find if target - fourth number exists. When fourth moves to next index, 
 * 	no need to check array behind fourth because it would have been found already 
 * 	Runtime O(N^3)
 * 	Memory O(N^2)?
 */
import java.util.*;

public class QuadrupleSum {
	public static void main(String[] args) {
		int[] nums = {1, 0, -1, 0, -2, 2,7};
					//{-2,-1,0,0,1,2,7}
		int target = 9;
		List<List<Integer>> temp = fourSum(nums, target);
		for (List<Integer> l : temp){
			for (Integer i : l){
				System.out.print(i + " ");
			}
			System.out.print("|");
		}
		System.out.println();
		System.out.println("Expected [[1,2,7,-1],[0,2,7,0]]");
	}
	public static List<List<Integer>> fourSum(int[] nums, int target) {
		Arrays.sort(nums);
		List<List<Integer>> answer = new ArrayList<List<Integer>>();
		for (int i = 0; i < nums.length; i++){
			if (i != 0 && nums[i] == nums[i-1])				//Runs the first duplicate so that it can use the next duplicates in triple sum
				continue;									//but only run with the dupe as i once so skip if previous has been used
			List<List<Integer>> temp = threeSum(nums, target-nums[i], i);
			for (List<Integer> l : temp){
				answer.add(l);
			}
		}
		return answer;
	}
	/**
	 * Slight modification from earlier problem of threeSum
	 * Now allows any target, not just 0, and adds a fourth number to the solution set which is -target
	 */
	public static List<List<Integer>> threeSum(int[] nums, int target, int fourth) {
		Arrays.sort(nums);									//Critical to traversal of sum
		List<List<Integer>> answer = new ArrayList<List<Integer>>();
		for (int i = fourth+1; i < nums.length-2; i++){		//Start at fourth+1 so check the rest of the array for target. No need to check before fourth because it would have been found already
			int left = i+1, right = nums.length-1;			//Reset on each loop
			if (i != fourth+1 && nums[i] == nums[i-1])		//First run needs to allow dupes, so instead of 0 its fourth+1 as first run
				continue;
			while (left < right){							//Once they cross itll be using duplicate pairs of numbers from earlier in this loop, no need to check
				int sum = nums[left] + nums[right] + nums[i];
				if (sum == target){	//If a triple is found
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.add(nums[i]);
					temp.add(nums[left]);					//Add it to list of triples
					temp.add(nums[right]);
					temp.add(nums[fourth]);					//Add fourth number in sum here so dont need to later
					answer.add(temp);
					while (left < right && nums[left] == nums[left+1]){		//Skip past any duplicates of left number so no duplicate triples made with this triple
						left++;
					}
					while (left < right && nums[right] == nums[right-1]){	//Skip past any duplicates of right number so no duplicate triple
						right--;											//right and left will end these loops on the duplicate number so still need another increment
					}
					left++;									//Move to next number
					right--;								//Move to next number
				} else if (sum < target){	//If too small, move left up to make bigger 
					left++;
				} else {									//If too big, move right down to make smaller
					right--;
				}
			}
			
		}
		return answer;
	}
}