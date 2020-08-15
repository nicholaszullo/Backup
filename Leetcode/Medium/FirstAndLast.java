/**	Assume input array will be sorted. Must run in O(log(N))
 *	Runtime O(2Log(N)) =O(log(N))
 *	Memory O(1)
 */
public class FirstAndLast {
	public static void main(String[] args) {
		int[] nums = {0,0,1,2,2};
		int target = 0;
		int[] ans = searchRange(nums, target);
		for (int i : ans)
			System.out.print(i + " ");
		System.out.println("expected [0,1]");
	}
	public static int[] searchRange(int[] nums, int target){
		int left = 0, right = nums.length-1;
		int[] ans = {-1,-1};
		while (left <= right){								//Binary search to find lowest. needs to be <= to ensure lowest is found and doesnt stop 1 early
			int mid = (left + right)/2;
			if (nums[mid] < target){						//target is in upper half, move lower bound up
				left = mid+1;
			} else if (nums[mid] >= target){				//target is in this half or in lower half, move upper bound down
				right = mid-1;
			}
			if (nums[mid] == target){						//Target is found, update lowest index
				ans[0] = mid;
			}
		}
		left = ans[0];										//No need to start at 0, start with lowest occurance of target
        if (ans[0] == -1)   								//If wasnt found, wont be found again so exit early
            return ans;
		right = nums.length-1;								//reset upper bound
		while (left <= right){								//Binary search for upper occurance. needs to be <= to ensure highest is found and doesnt stop 1 early
			int mid = (left + right)/2;
			if (nums[mid] <= target){						//Target is in this half or upper half, move lower bound up
				left = mid+1;
			} else if (nums[mid] > target){					//Target is in lower half, move upper bound down
				right = mid-1;
			}
			if (nums[mid] == target){						//Update highest occurance
				ans[1] = mid;
			}
		}
		ans[1] = ans[1] == -1 ? ans[0] : ans[1];			//If not found by second loop, make second occurance equal to first. else leave it the same
		return ans;
	}
}