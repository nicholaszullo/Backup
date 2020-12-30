/**	O(32) because n is fixed at 32 bits so O(1)
*	Simply get the next bit, then push it to its reverse position and add it into the answer
*
*/
public class ReverseBits {
    // you need treat n as an unsigned value
    public int reverseBits(int n) {
        int ans = 0;
        for (int i = 0; i < 32; i++){
            int bit = (n>>i) & 1;
            bit = bit << (31-i);
            ans = ans | bit;
        }
        return ans;
    }

}