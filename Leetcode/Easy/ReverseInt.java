/**	Build a string of the integer in reverse, then convert to an int. Kinda slow
 * 	Runtime O(N+toString+parseInt)
 * 	Memory O(N)
 * 
 */

public class ReverseInt{

	public static void main(String[] args) {
		System.out.println(reverse(1999999999));
	}
	public static int reverse(int x){
		boolean negative = x < 0 ? true : false;
		StringBuilder reversed = new StringBuilder();
		while (x != 0){
			reversed.append(Math.abs(x%10));
			x /= 10;
		}
		try{
			int answer = Integer.parseInt(reversed.toString());
			return negative ? -answer : answer;
		} catch (Exception NumberFormatException){
			return 0;
		}
		
	}

}