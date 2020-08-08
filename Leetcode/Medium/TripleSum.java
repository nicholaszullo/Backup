/**	Duplicates allowed in this array. (way harder)
 * 	Runtime O(N*log(N) + N^2) i.e. O(N^2)
 * 	Memory O(N)
 */
import java.util.*;

public class TripleSum {

	public static void main(String[] args) {
		int[] tester = {-1,0,1,2,-1,-4,1,4,0,5,-3,-1,5,0,2,3,0};
		//int[] tester = {-1,0,1,2,-2};
		List<List<Integer>> solution = threeSum(tester);
		for (List<Integer> a : solution){
			for (Integer i : a){
				System.out.print(i + " ");
			}
			System.out.print("| ");
		}
		System.out.println();
		System.out.print("Expected:[[-4,-1,5],[-4,0,4],[-4,1,3],[-4,2,2],[-3,-1,4],[-3,0,3],[-3,1,2],[-1,-1,2],[-1,0,1],[0,0,0]]");
		//System.out.print("Expected: -1 0 1 | -2 0 2");
	}
	public static List<List<Integer>> threeSum(int[] nums) {
		Arrays.sort(nums);									//Critical to traversal of sum
		List<List<Integer>> answer = new ArrayList<List<Integer>>();
		for (int i = 0; i < nums.length-2; i++){			//-2 because left is i+1, and on last loop right is i+2, cant go anymore without being out of bounds
			int left = i+1, right = nums.length-1;			//Reset on each loop
			if (i != 0 && nums[i] == nums[i-1])				//Runs the first duplicate so that it can use the next duplicates as left and right
				continue;									//but only run with the dupe as i once so skip if previous has been used
			while (left < right){							//Once they cross itll be using duplicate pairs of numbers from earlier in this loop, no need to check
				if (nums[left] + nums[right] == -nums[i]){	//If a triple is found
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.add(nums[i]);
					temp.add(nums[left]);					//Add it to list of triples
					temp.add(nums[right]);
					answer.add(temp);
					while (left < right && nums[left] == nums[left+1]){		//Skip past any duplicates of left number so no duplicate triples made with this triple
						left++;
					}
					while (left < right && nums[right] == nums[right-1]){	//Skip past any duplicates of right number so no duplicate triple
						right--;											//right and left will end these loops on the duplicate number so still need another increment
					}
					left++;									//Move to next number
					right--;								//Move to next number
				} else if (nums[left] + nums[right] < -nums[i]){	//If too small, move left up to make bigger 
					left++;
				} else {											//If too big, move right down to make smaller
					right--;
				}
			}
			
		}
		return answer;
	}

}