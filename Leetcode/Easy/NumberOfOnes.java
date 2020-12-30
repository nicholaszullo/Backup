/**	O(32) ~= O(1)
*	Loop and count the 1s, most likely a way to do it with bit masks, find it?
*/
public class NumberOfOnes {
    // you need to treat n as an unsigned value
    public int hammingWeight(int n) {
        int tot = 0;
        for (int i = 0; i < 32; i++){
            if (((n>>i) & 1) == 1){
                tot++;
            }
        }
        return tot;
    }
}