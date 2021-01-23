/**	O(N*M) DP Solution
*	Develop recurance relation, then convert to iterative bottom up
*/

public class MinPathSum {
	//Grid is N x M size
	public int minPathSum(int[][] grid) {
        int[][] dp = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                if (i == 0 && j == 0){		//Inital case start at top right
                    dp[i][j] = grid[i][j];
                } else if (i == 0){		//If on first row move along row
                    dp[i][j] = grid[i][j]+dp[i][j-1];
                } else if (j == 0){		//If on first col move along col
                    dp[i][j] = grid[i][j]+dp[i-1][j];
                } else {		//Otherwise take the best route to this grid spot
                    dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);
                }
            }            
        }

        return dp[grid.length-1][grid[0].length-1];
        
        //return recurse(grid, 0, 0);
    }
    private int recurse(int[][] grid, int i, int j){    
        if (i == grid.length-1 && j == grid[i].length-1){
            return grid[i][j];
        } else if (i == grid.length-1){
            return grid[i][j]+recurse(grid,i,j+1);
        } else if (j == grid[i].length-1){
            return grid[i][j] + recurse(grid,i+1,j);
        } else {
            return grid[i][j]+Math.min(recurse(grid, i+1, j), recurse(grid, i, j+1));
        }       
    }

}