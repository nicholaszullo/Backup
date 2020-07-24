/**
 * Runtime O(N*M) where N is number of characters in first word and M is the number of words
 * Memory O(1)
 * 
 * Removed extra call to substring and equals, 1ms empirical runtime
 */

public class LongestPrefix {
	public static void main(String[] args) {
		String[] strs = {"aaa", "abaa","aaaa"};
		System.out.println(longestCommonPrefix(strs));
	}
	public static String longestCommonPrefix(String[] strs) {
		if (strs.length == 0)
			return "";
		for (int currPre = strs[0].length(); currPre > 0; currPre-- ){
			String curr = strs[0].substring(0, currPre);
			boolean finished = true;
			for (int word = 1; word < strs.length; word++){
				try {
					if (strs[word].indexOf(curr) != 0){
						finished = false;
						break;
					}
				}
				catch (IndexOutOfBoundsException e){
					finished = false;
					break;
				}
			}
			if (finished){
				return curr;
			}
			
		}
		return "";
    }
}