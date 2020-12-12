/** O(N^2) Runtime Dynamic programming
 * 	O(N^2) Space, possible to do it in O(N) in the discussion.
 */
public class UniquePaths2 {
	public int uniquePathsWithObstacles(int[][] obstacleGrid) {
		int[][] dp = new int[obstacleGrid.length][obstacleGrid[0].length];
		for (int i = 0; i < obstacleGrid.length; i++) { 	//Initialize base case in row
			if (obstacleGrid[i][0] == 1){			//Once there is an obstacle, this path does not recurse anymore so no longer feasible, don't put anymore 1s
				break;								//Java defaults arrays to 0 so no need to continue 
			} else {
				dp[i][0] = 1;
			}   
		}
		for (int i = 0; i < obstacleGrid[0].length; i++) {		//Initialize base case in col
			if (obstacleGrid[0][i] == 1){
				break;
			} else {
				dp[0][i] = 1;
			}
		}
		for (int i = 1; i < obstacleGrid.length; i++){		//For each row, start at 1 because 1st row and col are covered by base case
			for (int j = 1; j < obstacleGrid[i].length; j++){	//For each col
				if (obstacleGrid[i][j] == 1){			//If there is an obstacle here, no path can come through here
					dp[i][j] = 0;
				} else {			//If there is no obstacle, can get here from left or above
					dp[i][j] = dp[i-1][j] + dp[i][j-1];			//Recursive condition
				}
			}
		}
		
		return dp[obstacleGrid.length-1][obstacleGrid[obstacleGrid.length-1].length-1];		//Answer is bottom right corner
	}
}