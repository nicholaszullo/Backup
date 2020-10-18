public class Robber {

    public int rob(int[] nums) {
        return recRob(nums,0,-1);
    }
    
    private int recRob(int[] nums, int curr, int last){
        if (curr == nums.length){
            return 0;
        } else if (curr == last){
            return recRob(nums, curr+1, last);
        } else {
            return Math.max(recRob(nums, curr+1, curr+1) + nums[curr], recRob(nums, curr+1, last));
        }
        
    }
}