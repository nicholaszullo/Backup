/**	Input constraint 1 <= n <= 30 
 * 
 * Runtime O(?) work increases with each recursive call, cant use master theorem
 * 	Each call to recCountAndSay is O(M) where M is length of curr 
 * Memory O(N) N recursive calls on the runtime stack
 */

public class CountAndSay {
	public static void main(String[] args) {
		
	}
	public String countAndSay(int n) {
        return recCountAndSay(n, new StringBuilder("1"), 1);
    }
    public String recCountAndSay(int n, StringBuilder curr, int currNum){
        if (currNum == n)
            return curr.toString();
        StringBuilder next = new StringBuilder();
        for (int i = 0; i < curr.length(); i++){
            int count = 1;
        
            while (i < curr.length()-1 && curr.charAt(i) == curr.charAt(i+1)){
                count++;
                i++;
            }
            
            next.append(count);				//Using a stringbuilder instead of String += here went from 14 ms to 1 ms. String += is O(N), append is O(1)
            next.append(curr.charAt(i));
            
        }
        return recCountAndSay(n, next, (currNum+1));
    }
}