/** 0 ms first try :). Could still optimize by only adding to temp when current.length == digits, then second array isnt needed and traversal of temp isnt needed either
 *  Runtime O(N)	
 * 	Memory O(2N)
 */

import java.util.ArrayList;
import java.util.List;

public class PhoneLetters {
	char[][] key = {{'2','a','b','c'},{'3','d','e','f'},{'4','g','h','i'},{'5','j','k','l'},{'6','m','n','o'},{'7','p','q','r','s'},{'8','t','u','v'},{'9','w','x','y','z'}};

	public List<String> letterCombinations(String digits) {
		ArrayList<String> answer = new ArrayList<>();
		ArrayList<String> temp = new ArrayList<>();
		recLetterCombs(digits, 0, new StringBuilder(),temp);
		for (String s : temp){									//Temp stores all combinations, only want those with same length as digits
			if (s.length() == digits.length())
				answer.add(s);
		}
		return answer;
	}
	private List<String> recLetterCombs(String digits, int position, StringBuilder current, List<String> answer){
		if (position == digits.length()){						//Base case all digits used			
			return answer;
		}						//ex. '2'-'0' -2 == 0, where 2's letters are stored in array
		for (int i = 1; i < key[digits.charAt(position)-'0'-2].length; i++){		//Letters possible from the current number, array stores letters starting at 1
			current.append(key[digits.charAt(position)-'0'-2][i]);					//Add new letter to current substring
			answer.add(current.toString());											//Add substring to list of all combinations
			recLetterCombs(digits, (position+1), current, answer);					//Find all combinations after current substring
			current.delete(current.length()-1, current.length());					//Remove current letter for next iteration of loop
		}
		return answer;																//Return current state of combinations

	}

}