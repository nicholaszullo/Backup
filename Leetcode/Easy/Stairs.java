public class Stairs {
	
	public static void main(String[] args){
		int n = 15;
		System.out.println("Iterative " + climbStairs(n));
		System.out.println("Recursive " + climbStairss(n));
	}
	
	public static int climbStairs(int n){
        int[] dp = new int[n+1];
        if (n == 1) {				//Special case no calculations needed
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        dp[n] = 1;					//Initialization
        dp[n-1] = 1;
        for (int i = n-2; i >= 0; i--) {				//n-2 to not go out of bounds
			dp[i] = dp[i+1] + dp[i+2];
        }
        return dp[0];
    }
	
	
	public static int climbStairss(int n) {
        return recClimbStairs(n, 0);
    }
	private static int recClimbStairs(int n, int curr){
		if (curr == n || curr == n-1){			//Either 1 step away or 2 steps away a solution is found
			return 1;
		}else {
			return recClimbStairs(n,curr+1) + recClimbStairs(n,curr+2);
		}
		
		
	}
	
	
}