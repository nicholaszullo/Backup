/** Time complexity must be O(log(N)). Assume no duplicates
 * 	Runtime O(3log(N)) = O(log(N))
 * 	Memory O(log(N)) on the runtime stack
 */
public class RotatedSearch {
	public static void main(String[] args) {
		int[] tester = {4,5,6,7,0,1,2};
		int target = 0;
		System.out.println(search(tester, target) + " expected 4");
	}
	/** Find pivot point, then binary search with pivot as mid on first run
	 * 	Need to find pivot in log(N). Pivot point is minimum value. Binary search for min
	 */
	public static int search(int[] nums, int target){
		int left = 0, right = nums.length-1;
		while (left < right){						//Find smallest value
			int mid  = (left + right)/2;
			if (nums[mid] > nums[right]){			//If there is a smaller number to the right, move half to the right half
				left = mid+1;
			} else {								//There is a smaller number to the left, move half left
				right = mid;
			}
		}
		int pivot = right;							//Left == right == smallest value
		if (pivot == -1)
			return -1;
		
		int leftside = binarySearch(nums, target, 0, pivot-1);		//O(log(N))
		int rightside = binarySearch(nums, target, pivot, nums.length-1);	//O(log(N))
		return leftside != -1 ? leftside : rightside;
	}
	private static int binarySearch(int[] nums, int target, int left, int right){
		if (left < 0 || right < 0)					//Case one of the original bounds was too small 
			return -1;
		int mid = (left + right)/2;
		if (left == right){							//Base case last index to check
			if (nums[left] == target){				//If found return the index
			 	return left;
			}
			return -1;								//Not found
		}
		else if (nums[mid] == target){				//Found return index
			return mid;
		} else if (nums[mid] > target){				//Too big so move to left half
			return binarySearch(nums, target, left, mid);
		} else {									//Too small so move to right half
			return binarySearch(nums, target, mid+1, right);
		} 
	}
}