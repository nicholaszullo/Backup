/**
 * Runtime O(N^2)
 * Memory O(1)
 */
public class MaxSubArray{

	public static void main(String[] args) {
		int[] arr = {-2,1,-3,4,-1,2,1,-5,4};
		System.out.println(maxSubArray(arr) + " expected 6");
	}
	public static int maxSubArray(int[] nums){
		int sum = 0, largest = Integer.MIN_VALUE;
		for (int i = 0; i < nums.length; i++){
			sum = 0;
			for (int j = i; j < nums.length; j++){
				sum += nums[j];
				largest = sum > largest ? sum : largest;
			}
		}
		return largest;
	}
}