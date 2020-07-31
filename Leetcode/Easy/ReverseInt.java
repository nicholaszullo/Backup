/**	Used string for easy overflow protection but too slow. Improve this with more overflow checks
 * 	Runtime O(N)
 * 	Memory O(1)
 * 
 */

public class ReverseInt{

	public static void main(String[] args) {
		System.out.println(reverse(-171471289) + " expected " + -982174171);
	}
	public static int reverse(int x){
		int answer = 0;
		int end = 0;										//Check if first digit of x is equal to last digit of answer. not the most robust overflow protection, but decent
		while (x != 0){
			if (x / 10 == 0)
				end = x % 10;
			answer *= 10;									//*10 first so first digit is in 1s place not 10s place
			answer += x % 10;
			x /= 10;
		}
		return end != answer % 10 ? 0 : answer;				//Return 0 if digits dont match, answer otherwise
	}

}