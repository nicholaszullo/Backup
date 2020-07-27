/**
 * 	Runtime O(N^2)
 * 	Memory O(1)
 */

public class LongestPalindrome{
	public static void main(String[] args) {
		System.out.println(longestPalindrome("asdfghjkllkjhgfdsatghnm") + " expected asdfghjkllkjhgfdsa");
	}

	public static String longestPalindrome(String s) {
		int longest = 0;
		int left = 0, right = 0;
		for (int i = 0; i < s.length(); i++){
			int oddLength = checkPalindrome(s, i, i);			//i is midpoint
			int evenLength = checkPalindrome(s, i, i+1);
			if (oddLength > evenLength){
				if (oddLength > longest){
					longest = oddLength;
					left = i - ((longest-1)/2);
					right = (longest/2) +i + 1;
					//System.out.println(i + " " + longest + " " + left + " " + right);
				}
			} else {
				if (evenLength > longest){
					longest = evenLength;
					left = i-((longest-1)/2);
					right = (longest/2)+i +1;
					//System.out.println(longest + " " + left + " " + right);
				}
			}
		}
		return s.substring(left, right);

	}
	public static int checkPalindrome(String s, int left, int right){
		while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)){
			left--;
			right++;
		}
		return right-left-1;			//-1 to 0 index length
	}



}