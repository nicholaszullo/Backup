import java.util.HashMap;

public class SingleNumber {
    public int singleNumber(int[] nums) {
        HashMap<Integer,Boolean> seen = new HashMap<Integer,Boolean>();
        for (int i = 0; i < nums.length; i++){
            seen.put(nums[i], seen.containsKey(nums[i]));
        }
        for (Integer i : seen.keySet()){
           if (!seen.get(i)){
               return i;
           }
        }
        return 0;
    }
}