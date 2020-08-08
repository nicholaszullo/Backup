/** First try :)
 * Runtime O(N^2 + N*log(N)) i.e. O(N^2)
 * Memory O(1)
 */
import java.util.Arrays;

public class TripleSumClosest {
	public static void main(String[] args) {
		int[] tester = {123,31,15,63,-1,-351,-23,-23,-64,-74,-42,-24,1,73,72,13,86};
		int target = -54;
		System.out.println(threeSumClosest(tester, target) + " expected -53");
	}
	public static int threeSumClosest(int[] nums, int target) {
		Arrays.sort(nums);											//Needed for traversal logic
		int closest = Integer.MAX_VALUE-100000;						//Kinda bad, but target restricted to -10^4 <= target <= 10^4 so should be big enough to not overflow when target is negative
		for (int i = 0; i < nums.length -2; i++){
			int left = i+1, right = nums.length-1;
			if (i != 0 && nums[i] == nums[i-1])
				continue;
			while (left < right){
				int sum = nums[i] + nums[left] + nums[right];
				if (sum == target){									//Found target so return it
					return target;
				} else {			//To determine distance to the target, take max - min and the smaller distance is the closest to the target
					closest = Math.max(closest, target) - Math.min(closest, target) < Math.max(target, sum) - Math.min(target, sum) ? closest : sum;
					if (sum > target){								//Sum is above target, make sum smaller
						right--;
					} else {										//Sum is under target, make sum bigger
						left++;
					}
				}
			}
		}
		return closest;
	}
}
//System.out.print((Math.max(closest, target) - Math.min(closest, target)) + " < " + (Math.max(target, sum) - Math.min(target, sum)) + " ?" );
//System.out.println( " closest = " + closest);