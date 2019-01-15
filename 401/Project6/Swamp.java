import java.io.*;
import java.util.*;

// DO NOT!! IMPORT JAVA.LANG

public class Swamp
{
	static int[][] swamp;  // NOW YOU DON'T HAVE PASS THE REF IN/OUT METHODS

 	public static void main(String[] args) throws Exception
	{
		int[] dropInPt = new int[2]; // row and col will be on the 2nd line of input file;
		swamp = loadSwamp( args[0], dropInPt );
		int row=dropInPt[0], col = dropInPt[1];
		String path = ""; // with each step grows to => "[2,3][3,4][3,5][4,6]" etc
		swamp[row][col] = -1;			//swamp2 did not work without this line, every other input worked without it
		dfs( row, col, path );
	} // END MAIN

	// --YOU-- WRITE THIS METHOD (LOOK AT PRINTSWAMP FOR CLUES)
   	// ----------------------------------------------------------------
	private static int[][] loadSwamp( String infileName, int[] dropInPt  ) throws Exception
	{
		
		int[][] temp;
		Scanner infile = new Scanner(new File(infileName));
		while(infile.hasNextInt()){
			int size = infile.nextInt();			//First number is size of swamp
			dropInPt[0] = infile.nextInt();			//Next two numbers are drop in point
			dropInPt[1] = infile.nextInt();
			temp = new int[size][size];
			for (int row = 0; row < size; row++){
				for (int col = 0; col < size; col++){
					temp[row][col] = infile.nextInt();			//Fills array with rest of values of the swamp
				}
			}
			return temp;
		}
		return null;
		
	}
	

	static void dfs( int row, int col, String path ) // dfs = DEPTH FIRST SEARCH
	{
		path += "[" + row + "," + col + "]";
		if (onEdge(row,col)){
			System.out.println(path);
			return ;
		}

		//North
		if (swamp[row-1][col] ==1){
			swamp[row-1][col] = -1;
			dfs(row-1,col, path);		//Check if the path continues
			swamp[row-1][col] = 1;
		}
		
		//North East
		if (swamp[row-1][col+1] ==1){
			swamp[row-1][col+1] = -1;
			dfs(row-1,col+1, path);		//Check if the path continues
			swamp[row-1][col+1] = 1;
		}
		
		//East
		if (swamp[row][col+1] ==1){
			swamp[row][col+1] = -1;
			dfs(row,col+1, path);		//Check if the path continues
			swamp[row][col+1] = 1;
		}
		
		//South East
		if (swamp[row+1][col+1] ==1){
			swamp[row+1][col+1] = -1;
			dfs(row+1,col+1, path);		
			swamp[row+1][col+1] = 1;
		}
		
		//South
		if (swamp[row+1][col] ==1){
			swamp[row+1][col] = -1;
			dfs(row+1,col, path);		
			swamp[row+1][col] = 1;
		}
		
		//South West
		if (swamp[row+1][col-1] ==1){
			swamp[row+1][col-1] = -1;
			dfs(row+1,col-1, path);		
			swamp[row+1][col-1] = 1;
		}
		
		//West
		if (swamp[row][col-1] ==1){
			swamp[row][col-1] = -1;
			dfs(row,col-1, path);		
			swamp[row][col-1] = 1;
		}
		
		//North West
		if (swamp[row-1][col-1] ==1){
			swamp[row-1][col-1] = -1;
			dfs(row-1,col-1, path);		
			swamp[row-1][col-1] = 1;
		}
	}	
	
	//Returns true iff the coord is on the edge of the array
	static boolean onEdge(int r, int c){
		if (r == 0 || r == swamp.length-1)
			return true;
		if (c == 0 || c == swamp[r].length-1)
			return true;
		return false;
		
	}
}

