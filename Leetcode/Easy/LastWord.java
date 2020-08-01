/**
 * Runtime O(N)
 * Memory O(1)
 */
public class LastWord {
	public static void main(String[] args) {
		System.out.println(lengthOfLastWord("") + " expected 15");
	}
	public static int lengthOfLastWord(String s){
		int index = s.length()-1;
		for (;index >= 0 &&  s.charAt(index) == ' '; index--);			//Move past ending " "s
		int length = index;												//Length is starting after " "s

		while (index >=0){
			if (s.charAt(index) == ' '){								//Space means end of last word
				break;
			}
			index--;													//Move to next char
		}
		return length-index;											//Number of characters in last word is logical length - characters unseen
	}
}