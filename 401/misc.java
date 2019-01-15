import java.util.*;
public class misc
{
	public static void main(String[] args){
		int[][] grid = new int[3][3];
		for (int i = 0; i < grid.length; i++){
			for (int c = 0; c < grid.length; c++){
				grid[i][c] = i*c;
			}
		}
		for (int i = 0; i < grid.length; i++){
			for (int c = 0; c < grid.length; c++){
				System.out.print(grid[i][c] + " ");
			}
			System.out.println();
		}
		
		
	}
}

