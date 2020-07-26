/**
 * 	Brute force solution, first one that came to mind. No optimizations
 * 	Runtime O(N^2) worst case
 * 	Memory O(1)
 */

public class RemoveDuplicates {
	public static void main(String[] args) {
		int[] arr = {0,0,1,1,1,2,2,3,3,4};
		System.out.println(removeDuplicates(arr));
	}
	public static int removeDuplicates(int[] nums) {
		int length = nums.length;
        for (int i = 0; i < length-1; i++){
			for (int a : nums){
				System.out.print(a + " ");
			}
			System.out.println("on i = " + i);
			if (nums[i] == nums[i+1]){
				shiftLeft(i, nums);
				length--;
				i--;
			}
		}
		return length;
    }
    public static void shiftLeft(int index, int[] arr){
        int toEnd = arr[index];
        for (; index < arr.length-1; index++){
            arr[index] = arr[index+1];
        }
        arr[arr.length-1] = toEnd;
    }
}