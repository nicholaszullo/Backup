/**
 * Runtime O(N)
 * Memory O(R)	where R is the radix of the alphabet 
 */

public class LongestSubstring {
	public static void main(String[] args) {
		//System.out.println(lengthOfLongestSubstring("sfcgvhbjknhfdtrfyuglh;ijklnbvfaaasd") + " expected 13");
		System.out.println(lengthOfLongestSubstring("abcabcbb") + " expected 3");
	}
	public static int lengthOfLongestSubstring(String s) {
		int[] lastSeen = new int[128];				//ASCII size to hold lowercase, uppercase, numbers, space 
		int start = 0;
		int longest = 0;
		for (int i = 0; i < s.length(); i++){
			System.out.println(s.charAt(i) + " " + lastSeen[s.charAt(i)] + " " + start + " " + longest);
			if (lastSeen[s.charAt(i)] > start){				//If last seen is < start, not a duplicate. If seen is > start, that char has been seen in the currently tracked substring
				start = lastSeen[s.charAt(i)];				//Move to duplicate character but passed first duplicate. Trick is s is still 0 indexed, so not +1 to move passed
			}
			lastSeen[s.charAt(i)] = i+1;					//+1 so not 0 indexed, start is 0 but needs to know 'a' at 3 is not the first one because array defaults to 0(ex abcabcbb)
			if (longest < (i - start) +1)					//Length is not 0 indexed so +1 to start count at 1
				longest = (i-start) +1;
			System.out.println("   " + s.charAt(i) + " " + lastSeen[s.charAt(i)] + " " + start + " " + longest);
		}
		return longest;
	}
}