/**
 * Runtime O(N) worst case
 * Memory O(N)
 */
import java.util.*;
public class TwoSum {
	public static void main(String[] args){
		int[] tester= {2,4};
		int[] result = twoSum(tester, 6);
		for (int i : result){
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println("Expected [0,1]");
	}

	public static int[] twoSum(int[] nums, int target) {
		HashMap<Integer, Integer> indicies = new HashMap<Integer, Integer>();
		
        for (int i = 0; i < nums.length; i++){
			if (indicies.containsKey(target-nums[i]) && !(indicies.get(target-nums[i]) == i)){
				return new int[] {indicies.get(target-nums[i]),i};
			}
			indicies.put(nums[i], i);
		}
		return null;
    }
}