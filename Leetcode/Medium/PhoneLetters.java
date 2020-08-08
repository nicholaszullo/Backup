/** Optimized recursion
 *  Runtime O(N)	
 * 	Memory O(N)
 */

import java.util.ArrayList;
import java.util.List;

public class PhoneLetters {
	char[][] key = {{'2','a','b','c'},{'3','d','e','f'},{'4','g','h','i'},{'5','j','k','l'},{'6','m','n','o'},{'7','p','q','r','s'},{'8','t','u','v'},{'9','w','x','y','z'}};

	public List<String> letterCombinations(String digits) {
		return recLetterCombs(digits, 0, new StringBuilder(), new ArrayList<String>());
	}
	private List<String> recLetterCombs(String digits, int position, StringBuilder current, List<String> answer){
		if (position == digits.length()){						//Base case all digits used			
			return answer;
		}						//ex. '2'-'0' -2 == 0, where 2's letters are stored in array
		for (int i = 1; i < key[digits.charAt(position)-'0'-2].length; i++){		//Letters possible from the current number, array stores letters starting at 1
			current.append(key[digits.charAt(position)-'0'-2][i]);					//Add new letter to current substring
			if (current.length() == digits.length())								//If lengths match, it is a solution so add it 
				answer.add(current.toString());
			recLetterCombs(digits, (position+1), current, answer);					//Find all combinations after current substring
			current.delete(current.length()-1, current.length());					//Remove current letter for next iteration of loop
		}
		return answer;																//Return current state of combinations

	}

}