/**
*	Messy solution.... Find better way to find mode of an array
*
*/

public class MajorityElement {
    public int majorityElement(int[] nums) {
        HashMap<Integer, Integer> occ = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++){
            if (occ.containsKey(nums[i])){
                occ.put(nums[i], occ.get(nums[i])+1);
            } else {
                occ.put(nums[i], 1);
            }
        }
        int max = 0;
        int key = 0;
        for (Map.Entry<Integer, Integer> ent : occ.entrySet()){
            if (max < ent.getValue()){
                max = ent.getValue();
                key = ent.getKey();
            }
        }
        return key;
    }



}