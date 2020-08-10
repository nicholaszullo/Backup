/** 0 ms first try :)
 * 	Runtime O(?) doesnt work with master theorem becuase work per call is increasing? 
 * 	Memory O(?) uh
 */

import java.util.*;

public class GenerateParentheses {

	public static void main(String[] args) {
		List<String> temp = generateParentheses(3);
		for (String s : temp){
			System.out.println(s);
		}
	}

	public static List<String> generateParentheses(int n){
		return recGenerateParentheses(n, 0, 0,new StringBuilder(), new ArrayList<String>());
	}

	/**Use a backtracking recursive algorithm to track the string and go up and down combinations of parentheses 
	 * 
	 * @param n	the number of parentheses pairs to use 
	 * @param left the number of current left parentheses in the string
	 * @param right the number of current right parentheses in the string
	 * @param curr the current string
	 * @param answer the list of solution strings
	 * @return the solution list of strings
	 */
	private static List<String> recGenerateParentheses(int n, int left, int right, StringBuilder curr, List<String> answer){
		if (curr.length() == n*2){					//If string reaches 2n parentheses used, it must be a valid string so add it 
			answer.add(curr.toString());
			return answer;
		}
		if (left < n){
			curr.append("(");			//Add a new left parentheses while more are needed
			recGenerateParentheses(n, left+1, right, curr, answer);		//Find all solutions with current string and 1 more left
			curr.deleteCharAt(curr.length()-1);			//Remove the left parentheses for backtracking
		}
		if (right < n && right < left){		//Add a right paren only if max isnt reached and there are more lefts in the string
			curr.append(")");
			recGenerateParentheses(n, left, right+1, curr, answer);
			curr.deleteCharAt(curr.length()-1);
		}
		return answer;
	}

}