/**	O(N), 0ms
*	Find the peak, start from the front and go to peak,
*	Then go from end back to peak. total 2N
*	Probably able to be done without finding peak first. Find improvement?
*/
public class TrapRain {
    public int trap(int[] height) {
        int peak = 0;
        int highest = 0;
        for (int i = 0; i < height.length; i++){
            if (highest < height[i]){
                peak = i;
                highest = height[i];
            }    
        }
        highest = 0;
        int area = 0;
        for (int i = 0; i < peak; i++){
            if (highest > height[i]){
                area += highest - height[i];
            } else {
                highest = height[i];
            }
        }
        highest = 0;
        for (int i = height.length-1; i > peak; i--){
            if (highest > height[i]){
                area += highest - height[i];
            } else {
                highest = height[i];
            }
        }
        return area;
    }




}