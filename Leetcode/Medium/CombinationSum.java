/**	DFS to find all solutions using backtracking recursion. 
 * 		Inital loop is not needed, case vals empty should just start next iteration in backtracking
 * 	Runtime quick
 * 	Memory not too much
 */
import java.util.*;

public class CombinationSum {
	public static void main(String[] args) {
		int[] candidates = {3,6,7,8,9,10};
		int target = 15;
		List<List<Integer>> temp = combinationSum(candidates, target);
		for (List<Integer> l : temp){
			for (Integer i : l){
				System.out.print(i + " ");
			}
			System.out.print("| ");
		}
		System.out.print("\n3,3,3,3,3 | 3,3,3,6 | 3,3,9 | 3,6,6 | 6,9 | 7,8 ");
	}
	public static List<List<Integer>> combinationSum(int[] candidates, int target) {
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
			if (sum + candidates[i] > target)			//If adding the current number would be too big, go back
				return ;
			else {
				vals.add(candidates[i]);				//sum+curr is <= target so add it
				recCombinationSum(candidates, target, i, sum+candidates[i], vals, answer);		//Find all solutions including curr and all after curr
				vals.remove(vals.size()-1);				//Go back to try other numbers
			}

		}

	}

}