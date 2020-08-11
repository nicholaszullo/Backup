/** NOT ALLOWED * / or %
 *	Runtime fastest
 *	Memory O(1)
 */
public class MyDivide {
	public static void main(String[] args) {
		System.out.println(divide(-214748364,-214748364) + " expected " + (-214748364/-214748364));
	}
	/** Concept: CPUs will multiply by shifting left then adding, so we can divide by shifting right then subtracting. 
	 *  Last loop will perform the subtraction
	 * 
	 * @param dividend the number to divide
	 * @param divisor the number to divide by
	 * @return the integer division of dividend/divisor
	 */
	public static int divide(int dividend, int divisor){
		boolean negative = (dividend < 0) ^ (divisor < 0) ? true : false;	//If 1 but not both are negative, result is negative
		boolean exceeded = false;
		int shift = 0, answer = 0;
		if (dividend == Integer.MIN_VALUE){							//Special case overflow possible
			exceeded = true;
			dividend += 1;
		}
		dividend = Math.abs(dividend);								//Make both positive so subtraction works properly in while loop
		divisor = Math.abs(divisor);
		if (dividend+1 == Integer.MIN_VALUE && divisor == 1)		//Special case MIN_VALUE / 1
			return negative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		if (divisor == 1)											//Shortcut case ANY / 1
			return negative ? -dividend : dividend;
		if ((exceeded && dividend+1 == divisor) || (!exceeded && dividend == divisor)){		//Special case and shortcut case MIN / MIN or ANY/ANY == 1
			return negative ? -1 : 1;
		}
		if (divisor == Integer.MIN_VALUE){							//Special case ANY / MIN == 0
			return 0;
		}
		while (dividend >= divisor){								//While more division needed
			shift = 0;												//Reset shift each loop
			while (dividend - (divisor << shift) > 0){				//subtract until cant subtract anymore
				shift++;
				if (dividend - (divisor << shift) < 0){				//Dont add to shift when next loop will be negative 
					shift--;
					break;
				}
			}
			answer += (1 << shift);									//divisor * (1 << shift) + remainder = dividened. first iteration finds shift number, second iteration finds remainder
			dividend -= (divisor << shift);							//Update dividend for next loop
			if (exceeded){											//Subtracted 1 at top but need it to be back to normal, so add 1 here
				dividend++;
				exceeded = false;									//Only add 1 once 
			}
		}
		return negative ? -answer : answer;
	}
}