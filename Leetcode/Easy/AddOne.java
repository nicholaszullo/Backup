/**
 * Runtime O(N)
 * Memory O(1) unless all 9s then O(N)
 */
public class AddOne {
	public static void main(String[] args) {
		
	}
	public int[] plusOne(int[] digits) {
		int curr = digits.length-1;
		while(curr >= 0 && digits[curr] == 9){					//Carry is needed, set current digit to 0 
			digits[curr--] = 0;
		}
		if (curr == -1){										//Case all 9s, need to increment size of array by 1
			int[] solution = new int[digits.length+1];
			// for (int i = 1; i < digits.length; i++){			//No need to do a copy. All places will be 0 except solution[0] and arrays initialize to 0
			// 	solution[i] = digits[i-1];
			// }
			solution[0] = 1;									//9..9 +1 is 10..0
			return solution;
		} else {
			digits[curr]++;										//All other cases add 1 to place
		}
		return digits;
    }
}