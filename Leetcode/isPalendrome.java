/**
 * Runtime O(N) Leetcode will not give lowest runtime even though it is (now it did)
 * Memory O(1)
 */

public class isPalendrome{

	public static void main(String[] args) {
		System.out.println(isIntPalindrome(2147483647));
	}
	public static boolean isIntPalindrome(int x) {
		if (x < 0 || (x + 1) < 0)
		return false;
		int holder = x;
		int reversedX = 0;
		if (x == 0){				//If only digit was 0, it is a palindrome
			return true;
		} else if (x % 10 == 0){	//If last digit is 0, not a palindrome because int cannot start with a leading 0			
			return false;
		}
		while (holder != 0){
			reversedX = (reversedX * 10) + (holder % 10);	//Shift current number by 10, then add new digit
			holder /= 10;
		}
		return (reversedX == x);

    }


}