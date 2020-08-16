/**	Only difference between this and no dupes is only run first occurance of duplicate
 * 
 */

import java.util.*;

public class CombinationSumDups {

	public List<List<Integer>> combinationSum2(int[] candidates, int target) {
		ArrayList<List<Integer>> answer = new ArrayList<List<Integer>>();
		Arrays.sort(candidates);
		
		recCombinationSum(candidates, target, 0, 0, new ArrayList<Integer>(), answer);
		
		return answer;
	}
	private static void recCombinationSum(int[] candidates, int target, int index, int sum, List<Integer> vals, List<List<Integer>> answer){
		if (sum == target){
			List<Integer> temp = new ArrayList<Integer>();		//Need to copy vals into a new array to store in answer because val will always end empty
			for (Integer i : vals){
				temp.add(i);
			}
			answer.add(temp);
		}
		for (int i = index; i < candidates.length; i++){
			if (i != index && candidates[i] == candidates[i-1]){		//If this is not the first time seeing this number dont use it and continue to next
				continue;
			}
			if (sum + candidates[i] > target)			//If adding the current number would be too big, go back
				return ;
			else {
				vals.add(candidates[i]);				//sum+curr is <= target so add it
				recCombinationSum(candidates, target, i+1, sum+candidates[i], vals, answer);		//Find all solutions including curr and all after curr
				vals.remove(vals.size()-1);				//Go back to try other numbers
			}

		}

	}
}