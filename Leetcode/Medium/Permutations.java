/**	Quick 5 minute initial solution
 * 	Runtime not too bad
 * 	Memory decent
 */
import java.util.*;

public class Permutations {
	public List<List<Integer>> permute(int[] nums) {
		List<List<Integer>> ans = new ArrayList<List<Integer>>();
		recPermutate(nums, 0, new ArrayList<Integer>(), ans, new HashSet<Integer>());
		return ans;
	}
	private void recPermutate(int[] nums, int index, List<Integer> curr, List<List<Integer>> ans, HashSet<Integer> seen){
		if (index == nums.length){
			ans.add(new ArrayList<Integer>(curr));
			return ;
		}
		for (int i = 0; i < nums.length; i++){ 
			if (!seen.add(nums[i]))
				continue;
			curr.add(nums[i]);
			recPermutate(nums, index+1, curr, ans, seen);
			curr.remove(curr.size()-1);
			seen.remove(nums[i]);
		}
	}
}