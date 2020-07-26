/**
 *  A shift on A consists of taking string A and moving the leftmost character to the rightmost position. 
 *  For example, if A = 'abcde', then it will be 'bcdea' after one shift on A. 
 *  Return True if and only if A can become B after some number of shifts on A.
 *
 *  Runtime 0ms, but 37.2 MB memory
 */

public class RotateStrings{

	public static void main(String[] args){

		System.out.println(rotateString("abcde","cdeab"));
	}

	public static boolean rotateString(String A, String B) {
		StringBuilder shifter = new StringBuilder(A);
		if (A.equals(B))
			return true;
		int i = 0;
		while (i < A.length()){
			shifter.append(shifter.charAt(0));
			shifter.deleteCharAt(0);
			System.out.println(shifter);
			if (shifter.toString().equals(B))
				return true;
			i++;
		}
		return false;
	}


}