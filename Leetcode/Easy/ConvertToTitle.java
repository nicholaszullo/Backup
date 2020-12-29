/**
*	Ideally dont reverse, but insert at 0 is not any faster. 
*	Different direction or different data structure to improve?
*/

public class ConvertToTitle {
    public String convertToTitle(int n) {
        StringBuilder ans = new StringBuilder();
        while (n > 0){
            if (n % 26 == 0){
                ans.append('Z');
                n--;
                n /= 26;
            } else {
                ans.append((char)('A'+(n%26)-1));
                n /= 26;
            }
        }
        return ans.reverse().toString();
    }


}