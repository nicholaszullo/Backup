/** Find the character in t that is not in s
*	Really cool trick to compare sums of the strings. Since t differs from s by only 1 character and contains every other character, the sums will differ by the missing character
* O(N)
*/
public class FindTheDifference {
    public char findTheDifference(String s, String t) {
        int sumS = 0;
        int sumT = 0;
        for (int i = 0; i < s.length(); i++){
            sumS += s.charAt(i);
            sumT += t.charAt(i);
        }
        sumT += t.charAt(t.length()-1);
        return (char)(sumT-sumS);
    }
}