/**	Given a sorted array, give index an element should be inserted at
 * 	Runtime O(log(N))
 * 	Memory O(log(N))	log(N) calls to binary search so log(N) memory on the runtime stack
 */

public class SearchInsert {
	public static void main(String[] args) {
		int[] arr = {1,4,6,7,8,9,10};
		System.out.println(searchInsert(arr, 10) + " expected 6");
	}
	public static int searchInsert(int[] nums, int target) {
        if (nums.length == 0)
            return 0;
        return binarySearch(nums, target, 0, nums.length-1);
    }
    public static int binarySearch(int[] nums, int target, int start, int end){
        int middle = (start + end) /2;
        if (start > end)
            return start;
        else if (nums[middle] > target)
            return binarySearch(nums, target, start, middle-1);
        else if (nums[middle] < target)
            return binarySearch(nums, target, middle+1, end);
        else
            return middle;
        
    }
}