/**
 * Runtime O(N)
 * Memory O(1)
 */
public class MaxSubArray{

	public static void main(String[] args) {
		int[] arr = {4,-5,6,1,-2,-100,1};
		System.out.println(maxSubArray(arr) + " expected 7");
	}
	public static int maxSubArray(int[] nums){
		int sum = 0, max = Integer.MIN_VALUE;
		for (int i=0;i<nums.length;++i){
			sum= Math.max(sum + nums[i],nums[i]);				//Continue following this subarray, or reset sum to be this index
			max=Math.max(max, sum);								//Make sum max if it is the max
		}
		return max;
	}
}