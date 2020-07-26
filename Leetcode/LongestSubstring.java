/**
 * Runtime O(N^2) worst case (all unique)	Slightly faster than before but still slow
 * Memory O(N) 
 */
import java.util.HashSet;

public class LongestSubstring {
	public static void main(String[] args) {
		System.out.println(lengthOfLongestSubstring("abcccdefg") + " expected 5");
	}
	public static int lengthOfLongestSubstring(String s) {
		int longest = 0;
		HashSet<Character> seen;
		for (int i = 0; i < s.length()-longest; i++){
			seen = new HashSet<Character>();
			int currLength =0; 
			for (int j = i; j < s.length(); j++){
				if (seen.add(s.charAt(j))){
					currLength++;
				} else {
					break;
				}
			}
			if (longest < currLength)
				longest = currLength;
		}
		return longest;
	}
}