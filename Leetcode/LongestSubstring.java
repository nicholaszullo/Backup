/**
 * Runtime O(N^2) worst case (all unique)
 * Memory O(N) 
 */

import java.util.ArrayList;
import java.util.HashSet;

public class LongestSubstring {
	public static void main(String[] args) {
		System.out.println(lengthOfLongestSubstring("abcccdefg") + " expected 5");
	}
	public static int lengthOfLongestSubstring(String s) {
		HashSet<Character> seen;
		ArrayList<String> solutions = new ArrayList<String>();
        for (int i = 0; i < s.length(); i++){
			seen = new HashSet<Character>();
			boolean stop = false;
			for (int j = i; j < s.length(); j++){
				if (!seen.add(s.charAt(j))){
					System.out.println("duplicate found! adding " + s.substring(i,j-1));
					solutions.add(s.substring(i,j-1));
					stop = true;
					break;
				}
			}
			if (!stop)
				solutions.add(s.substring(i, s.length()));
		}
		int largest = 0;
		for (int i = 0; i < solutions.size(); i++){
			if (solutions.get(i).length() >= solutions.get(largest).length())
				largest = i;
		}
		return solutions.get(largest).length();
    }
}