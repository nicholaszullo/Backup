/**
 * Runtime O(N)
 * Memory O(N)		Leetcode shows solutions using 2 stacks as less memory??
 */
import java.util.Stack;

public class ValidParentheses {
	
	public static void main(String[] args) {
		System.out.println(isValid("(((([]{()}))))") + " Expected true");
	}

	public static boolean isValid(String s) {
		Stack<Character> stack = new Stack<Character>();
		for (int place = 0; place < s.length(); place++){
			char curr = s.charAt(place);
            try{										//Either check if stack is empty each time, or catch the EmptyStackException
                if (curr == '(' || curr == '{' || curr == '[')
                    stack.push(curr);
                else if (curr == ')' && stack.peek() == '(')
                    stack.pop();
                else if (curr == '}' && stack.peek() == '{')
                    stack.pop();
                else if (curr == ']' && stack.peek() == '[')
                    stack.pop();
                else 
                    return false;
            } catch (Exception e){						//Leetcode errors with EmptyStackException so just catch all i guess
                if (place >= s.length()-1)
                    return false;
            }
		}
		return stack.isEmpty();							//Empty if valid
    }
}