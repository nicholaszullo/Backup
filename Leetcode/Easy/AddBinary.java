/**	Input constraints: 
 * 		Nonempty, only 0 and 1, no leading 0s, max length 10^4
 * 	Using append() then reverse same empirical runtime as insert(0, x)?? 
 * 	insert(0) is O(N) and append is O(1) reverse is O(N). Only 1 O(N) instead of many, but i guess its all asymptotically the same?
 * 	Runtime O(N)
 * 	Memory O(N) 2N
 * 	Could improve by not equalizing lengths and using 2 pointers instead
 */
public class AddBinary {
	public static void main(String[] args) {
		System.out.println(addBinary("1010", "1011") + " expected 10101");		//previous commit forgot to change these back to normal
	}

	public static String addBinary(String a, String b) {
		if (a.length() > b.length()){							//Equalize lengths so while loop will finish all of both strings
			b = padZeros(b, a.length()-b.length());
		}else if (b.length() > a.length()){
			a = padZeros(a, b.length()-a.length());
		}
		int place = a.length()-1;
		boolean carry = false;
		StringBuilder answer = new StringBuilder();
		while (place >= 0){
			if (!carry && a.charAt(place) == '0' && b.charAt(place) =='0'){	//Case 0 + 0 + 0
				carry = false;
				answer.append("0");
			} else if (!carry && ((a.charAt(place) == '1' && b.charAt(place) == '0') ||(b.charAt(place) == '1' && a.charAt(place) == '0'))){		//Case 0 + 1 + 0
				carry = false;
				answer.append("1");
			} else if (!carry && (a.charAt(place) == '1' && b.charAt(place) == '1')){			//Case 0 + 1 + 1
				carry = true;
				answer.append("0");
			} else if (carry && (a.charAt(place) == '1' && b.charAt(place) == '1')){			//Case 1+1+1
				carry = true;
				answer.append("1");
			} else if (carry && (a.charAt(place) == '1' || b.charAt(place) == '1')){			//Case 1+ 0 + 1
				carry = true;
				answer.append("0");
			} else if (carry && a.charAt(place) == '0' && b.charAt(place) == '0'){				//Case 1 + 0 + 0
				carry = false;
				answer.append("1");
			} else {
				System.out.println("INPUT INVALID!!! " + place + " " +answer);
				System.exit(0);	//CRASH!
			}
			place--;
		}
		if (carry){
			answer.append("1");
		}
		return answer.reverse().toString();
	}
	public static String padZeros(String a, int num){
		StringBuilder aa = new StringBuilder();
		for (int i = 0; i < num; i++)
			aa.append("0");
		aa.append(a);
		return aa.toString();
	}
}