/**	Instructions dont say to not use indexof, but completely ruins problem. 0ms solution is just 1 line indexOf. this solution is 1ms
 * 	Boyer-Moore String Pattern Matching!
 * 	Input restricted to lowercase english characters
 * 	Runtime O(N/M) average case where N is the length of string being searched and M is the length of the pattern to find
 * 		O(N*M) worst case but very very rare
 * 	Memory O(R) where R is the radix of the alphabet (26 in this case)
 */

public class StrStr {
	public static void main(String[] args) {
		System.out.println(strStr("giakdniavubiueavbiav", "biav") + " expected " + "giakdniavubiueavbiav".indexOf("biav"));	
	}
	public static int strStr(String haystack, String needle){
		if (haystack.length() - needle.length() == 0){						//Shortcut case they are the same length dont run the algorithm just match each character				
			return haystack.equals(needle) ? 0 : -1;
		}
		int[] rightmost = new int[26];
		for (int i = 0; i < 26; i++){				//Can these loops be combined?
			rightmost[i] = -1;
		}
		for (int i = 0; i < haystack.length(); i++){
			rightmost[haystack.charAt(i)-97] = i;
		}
		// int k = 0;
		// for (int i : rightmost){
		// 	System.out.print((char)((k++)+97) + " " + i + " ");
		// }
		// System.out.println();
		int skip = 0;														//Distance to move in string
		for (int i = 0; i <= haystack.length()-needle.length(); i+= skip){	//Stop when only needle.length is left in haystack
			skip = 0;
			for (int j = needle.length()-1; j >= 0; j--){					//Start checking at end
				if (needle.charAt(j) != haystack.charAt(i+j)){
					skip = j - rightmost[haystack.charAt(i+j)-97];			//Align to farthest position of character
					if (skip < 1) 											//Always move by 1
						skip = 1;
					break;
				}
			}
			if (skip == 0)													//Skip never changed so never mismatch
					return i;
		}
		return -1;
	}
}