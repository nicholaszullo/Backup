/**	convert 32 bit to hex, 2s complement
*	trick is to use bit manipulation instead of math to work for negative numbers too
*	num & 15 instead of num % 16 and num >>> 4 instead of num / 16
*/

public class ToHex {
    public String toHex(int num) {
        char[] key = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        StringBuilder ans = new StringBuilder();
        while (num != 0){
            ans.append(key[num & 15]);
            num = num >>> 4;
        }
        if (ans.length() == 0) return "0";
        return ans.reverse().toString();
    }

}