/**
*	Track all seen positives, then return if one is missing, or return the next positive number
*/

public class FirstMissingPositive {
    public int firstMissingPositive(int[] nums) {
        HashMap<Integer, Integer> occ = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++){
            if (nums[i] > 0){
                occ.put(nums[i],1);
            }
        }
        for (int i = 1; i <= occ.size(); i++){
            if (!occ.containsKey(i)){
                return i;
            }
        }
        return occ.size()+1;
    }



}