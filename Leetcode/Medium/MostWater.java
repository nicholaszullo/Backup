/**
 * Runtime O(N)
 * Memory O(1) why does leetcode use so muc memory? is it Math?
 */
public class MostWater {
	public static void main(String[] args) {
		int[] arr = {1,8,6,2,5,4,8,3,7};
		System.out.println(maxArea(arr) + " expected 49");	
	}
	public static int maxArea(int[] height){
		int max = 0, i = 0, j = height.length -1;
        while (i < j) {						
            max = Math.max(max, (j - i) * Math.min(height[i], height[j]));
            if (height[i] < height[j]) 
                i++;                        //move right 
            else 
                j--;                        //Move left
        }
        return max;
	}
}