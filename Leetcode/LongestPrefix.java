/**
 * Runtime O(N*M) where N is number of characters in first word and M is the number of words
 * Memory O(1)
 * 
 * Now starts searching from end of string backwards, faster empirical runtime but not fastest
 */

public class LongestPrefix {
	public static void main(String[] args) {
		String[] strs = {"a"};
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
					if (!strs[word].substring(0, currPre).equals(curr)){
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