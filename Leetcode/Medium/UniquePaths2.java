/** Too slow but works, convert to iterative
 * 
 */
public class UniquePaths2 {
	public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        return findPaths(obstacleGrid,0,0);
    }
    private int findPaths(int[][] grid, int row, int col){
        if (row == grid.length || col == grid[row].length){
            return 0;
        } else if (row == grid.length-1 && grid[row].length -1 == col && grid[row][col] == 0){
            return 1;
        } else if (grid[row][col] == 1){
            return 0;
        } else {
            return findPaths(grid, row+1, col) + findPaths(grid, row, col+1);
        }
    }
}