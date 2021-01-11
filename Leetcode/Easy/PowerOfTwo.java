/**	1ms, looked at the 0ms and its really smart. I knew there would be a bitwise solution 
*
*/

public class PowerOfTwo {
    public boolean isPowerOfTwo(int n) {
        int bits = n;
        while (bits != 0){
            int bit = bits & 1;
            if (bit == 1){
                bits = bits >> 1;
                if (bits == 0){
                    return true;
                } else {
                    return false;
                }
            } else {
                bits = bits >> 1;
            }
        }
        return false;
    }
}