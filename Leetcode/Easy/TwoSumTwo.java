/**
*	Simple O(N) 
*	Input array is sorted, find 2 numbers that sum to target, exactly 1 solution 
*/

public class TwoSumTwo{
    public int[] twoSum(int[] numbers, int target) {
        int left =0;
        int right = numbers.length-1;
        while (left < right){
            if (numbers[left]+numbers[right] > target){
                right--;
            } else if (numbers[left] + numbers[right] < target){
                left++;
            } else {
                return new int[] {left+1, right+1};
            }
        }
        return null;
    }
	
}