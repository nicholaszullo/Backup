/**
 * Runtime O(N*M) where N is number of characters in first word and M is the number of words
 * Memory O(1)s
 */

public class LongestPrefix {
	public static void main(String[] args) {
		String[] strs = {"aaa", "aaaa", "aaa"};
		System.out.println(longestCommonPrefix(strs));
	}
	public static String longestCommonPrefix(String[] strs) {
		String solution = "";
        if (strs.length == 0)
            return solution;
        for (int place = 0; place < strs[0].length(); place++){
            char curr;
            try {
                curr = strs[0].charAt(place);
            } catch (IndexOutOfBoundsException e){
                return solution;
            }
            
            for (int word = 1; word < strs.length; word++){
                try {
                    if (strs[word].charAt(place) != curr){
                        return solution;
                    }
                } catch (IndexOutOfBoundsException e){
                    return solution;
                }
            }
            solution += curr;
		}
		return solution;
    }
}