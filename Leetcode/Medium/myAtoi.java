/**
 * The function first discards as many whitespace characters as necessary until the first non-whitespace character is found. 
 * Then, starting from this character, takes an optional initial plus or minus sign followed by as many numerical digits as possible, 
 * and interprets them as a numerical value.
 * The string can contain additional characters after those that form the integral number, 
 * which are ignored and have no effect on the behavior of this function.
 * If the first sequence of non-whitespace characters in str is not a valid integral number, 
 * or if no such sequence exists because either str is empty or it contains only whitespace characters, no conversion is performed.
 * If no valid conversion could be performed, a zero value is returned.
 * 
 * 
 * 3 ms runtime, 39.6 MB Memory. NOT GOOD!
 */

public class myAtoi {
	public static void main(String[] args){
		String tester = "-000001";
		int result = atoi(tester);
		System.out.println(result);
		System.out.println("Expected " + "-1");
	}
	public static int atoi(String s){
		int i = 0;
		int result = 0;
		boolean negative = false;
		if (s.length() == 0)
			return 0;
		for (i = 0; (int)s.charAt(i) == 32 && i < s.length(); i++);                   //Move i to first place after whitespace
		if (i == s.length())                                        //Case all whitespace
			return 0;
		if ((int)s.charAt(i) == 45){                                //Check for negative sign
			negative = true;
			i++;                                                    //Inside if because need to move to next letter. If if was false, stay on same letter
			if (s.length() == 1)
				return 0;
		} else if ((int)s.charAt(i) == 43){                         //Check for positive sign
			negative = false;
			i++;
			if (s.length() == 1)
				return 0;
		}
		if ((int)s.charAt(i) > 57 || (int)s.charAt(i) < 48)
			return 0;
		while (i < s.length() && (int)s.charAt(i) <= 57 && (int)s.charAt(i) >= 48){
			if (!negative && ((result*10 + (int)s.charAt(i)-48) % 10 != (int)s.charAt(i)-48)){
				return Integer.MAX_VALUE;
			} else if (negative && ((result*10 + (int)s.charAt(i)-48) % 10 != (int)s.charAt(i)-48)){
				return Integer.MIN_VALUE;
			}
			result *= 10;
			result += (int)s.charAt(i)-48;
			i++;
		}
		if (negative)
			result *= -1;
		return result;
	}
} 
