/**
 * Didnt run local testing for this one...
 * 	Runtime O(N)
 * 	Memory O(1)
 */

public class RemoveElement {
	public static void main(String[] args) {
		
	}
	public int removeElement(int[] nums, int val) {
        int length = nums.length-1;
        if (length == 0 && nums[length] == val)
            return 0;
        for (int i = 0; i <= length; i++){
            if (nums[i] == val){
                for (; nums[length] == val && length >= 1 && length != i; length--);			//Checks the end of the array for val, and also dont swap until num != val. also if i is the end dont loop. clean and concise 
                nums[i] = nums[length--];
            }
        }
        return length+1;
    }
}