/**	O(NK). Runtime with return condition at top is 0ms, but without it's 1000ms? Leetcode wut
*	Could use a hashmap instead but all the top solutions use the same idea as me, so why 
*	is the runtime so much higher?
*/
public class ContainsNearDup {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
		//if(nums.length>5000)return false;
        
		for (int i = 0; i < nums.length; i++){
            for (int j = i+1; j <= i+k && j < nums.length; j++){
                if (nums[i] == nums[j]){
                    return true;
                }
            }
        }
        return false;
    }
}