/** First try :)
 * Runtime O(N)
 * Memory O(N)
 */
public class ZigZag {
	public static void main(String[] args) {
		System.out.println(convert("PAYPALISHIRING", 5) + " expected PHASIYIRPLIGAN");	
	}
	public static String convert(String s, int numRows){
		if (numRows == 1)
			return s;
		StringBuilder ans = new StringBuilder();
		int index = numRows-1, start = 0;							//Used to calculate letters to skip
		while (ans.length() != s.length()){							//If length is less, didnt add all letters. if length is greater, something very wrong
			int counter = start;									//Restart starting point for skipping
			boolean useIndex = true;								//When moving through the word, unless at the middle skip distance will be different every other skip. Boolean to decide which skip calculation was last used
			while (counter < s.length()){							//If skip past length, none left in row
				ans.append(s.charAt(counter));						//Add the current letter then skip to next letter
				if ((index != 0 && useIndex) || start == 0){		//index and start will only both be 0 if numRows == 1. Use the one that isnt 0, or alternate which one to use. Start with index to skip farther first
					useIndex = false;
					counter += index*2;								//Skip is ((n-1)*2)-1; n-1 from numRows -1, -1 at end omitted because need the char after skipping
				} else {
					useIndex = true;
					counter +=start*2;
				}
			}
			index--;
			start++;
		}
		return ans.toString();
	}
}

/**
 * 1
PAYPALISHIRING
	
	SKIP ((N-1)*2) -1 LETTERS TO REACH NEXT ONE
2 skip 1
P  Y  A  I  H  R  N
 A  P  L  S  I  I  G

3  skip 3
P   A   H   N
 A P L S I I G
  Y   I   R

4  skip 5
P       I       N
 A     L S     I G
  Y  A    H  R
   P       I

5  skip 7
P        H         F        F
 A      S  I      F F      F
  Y    I     R   F   F    F
   P  L       I G     F  F
    A          N       F

6	skip 9
P         R
 A       I I
  Y     H   N
   P   S     G
    A I
	 L

7 skip 11
P           N
 A         I G
  Y       R
   P     I
    A   H
	 L S
	  I
 */