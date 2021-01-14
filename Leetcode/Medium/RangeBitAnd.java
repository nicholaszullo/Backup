/**	Equivilant to find common bitwise prefix
*	Once max and min are the same, that is the common prefix, shift it to the proper place 
*/

public class RangeBitAnd {
    public int rangeBitwiseAnd(int m, int n) {
        int place = 0;
        while (m != n){
            m = m >> 1;
            n = n >> 1;
            place++;
        }
        return m << place;
    }
}