/**	Duplicates allowed in this array. (way harder)
 * 	Yikes runtime on this one
 * 	Runtime O(N^3*log(N))
 * 	Memory O(N^2)
 */
import java.util.*;

public class TripleSum {

	public static void main(String[] args) {
		int[] tester = {-1,0,1,2,-1,-4,1,4,0,5,-3,-1,5,0,2,3,0};
		List<List<Integer>> solution = threeSum(tester);
		for (List<Integer> a : solution){
			for (Integer i : a){
				System.out.print(i + " ");
			}
			System.out.print("| ");
		}
		System.out.println();
		System.out.print("Expected:[[-4,-1,5],[-4,0,4],[-4,1,3],[-4,2,2],[-3,-1,4],[-3,0,3],[-3,1,2],[-1,-1,2],[-1,0,1],[0,0,0]]");
	}
	public static List<List<Integer>> threeSum(int[] nums) {
		Arrays.sort(nums);
		int[][] seen = new int[nums.length*9][3];
		List<List<Integer>> answer = new ArrayList<List<Integer>>();
		boolean valid = true;
		int total = 0;
		for (int i = 0; i < nums.length; i++){
			for (int j = i+1; j < nums.length; j++){
				int found = Arrays.binarySearch(nums, -(nums[i] + nums[j]));
				if (found >= 0 && found != i && found != j){
					for (int k = 0; k < total; k++){
						if (nums[i]  == nums[j] && nums[j]== nums[found]){
							if (seen[k][0]  == seen[k][1] && seen[k][1]== seen[k][2]){
								System.out.println("DUPLICATE!" +seen[k][0] + " " + seen[k][1] + " "+ seen[k][2] + " against " + nums[i] + " " + nums[j] + " "+nums[found] );
								valid = false;
								break;
							}
						} else if (seen[k][0] == nums[i] || seen[k][1] == nums[i] || seen[k][2] == nums[i]){
							if (seen[k][0] == nums[j] || seen[k][1] == nums[j] || seen[k][2] == nums[j]){
								if (seen[k][0] == nums[found] || seen[k][1] == nums[found] || seen[k][2] == nums[found]){
									System.out.println("DUPLICATE!" +seen[k][0] + " " + seen[k][1] + " "+ seen[k][2] + " against " + nums[i] + " " + nums[j] + " "+nums[found] );
									valid = false;
									break;
								}
							}
						}
					}
					if (valid){
						ArrayList<Integer> temp = new ArrayList<Integer>();
						System.out.println("ADDING!"+ nums[i] + " " + nums[j] + " "+nums[found]);
						temp.add(nums[i]);
						temp.add(nums[j]);
						temp.add(nums[found]);
						answer.add(temp);
						seen[total][0] = nums[i];
						seen[total][1] = nums[j];
						seen[total++][2] = nums[found];
					}
					valid = true;
				}
			}
		}
		return answer;
	}

}