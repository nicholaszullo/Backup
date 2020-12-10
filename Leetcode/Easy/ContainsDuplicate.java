import java.util.HashSet;

public class ContainsDuplicate {
    public boolean containsDuplicate(int[] nums) {
        HashSet<Integer> seen = new HashSet<Integer>();
        for (int i = 0; i < nums.length; i++){
            if (!seen.add(nums[i])){
                return true;
            }
            
        }
        return false;
    }


}