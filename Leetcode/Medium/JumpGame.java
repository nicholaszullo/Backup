/**	O(N) Runtime
 *
 */
public class JumpGame{
	public boolean canJump(int[] nums) {
        int max = 0;		//Maximum reachable index
        for (int i = 0; i < nums.length; i++){
            max += nums[i];	//Jump by 1 each time, and track what happens if you make the max jump instead
        }
        return max >= nums.length-1;	//If max jump is >= last index, it is possible to stop there
        
    }

}
