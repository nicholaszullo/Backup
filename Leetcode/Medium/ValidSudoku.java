/** Not using a hashset makes it 1ms. Why - no extra time allocating heap for objects most likely
 * 	Also removed checking if true was true in if statements, can just use the boolean value from array
 * Runtime O(N^2) where N is the length of row in the board
 * Memory O(N) More memory to use array than hashset
 */


public class ValidSudoku {
	public boolean isValidSudoku(char[][] board) {
		boolean[][] seenRow = new boolean[9][9];					//Need space for each row to have each number
		boolean[][] seenCol = new boolean[9][9];					//Need space for each column to have each number 
		boolean[][] seenBox = new boolean[9][9];					//Space for each box to have each number
		for (int i = 0; i < board.length; i++){ 					//For every row
			for (int k = 0; k < board[i].length; k++){				//For every column
				if (board[i][k] == '.'){							//Blank space go to next 
					continue;
				}
				if (!seenRow[i][board[i][k] - '1']){				//If not in the row, add it
					seenRow[i][board[i][k] - '1'] = true;	
				} else if (seenRow[i][board[i][k] - '1']){			//If seen in this row, invalid
//					System.out.println("BAD ROW! " + i + " " + k);
					return false;
				}
				if (!seenCol[k][board[i][k] - '1']){				//Column k will repeat down the rows, and go to index board[i][k] - 1 (1 stored at 0)
					seenCol[k][board[i][k] - '1'] = true;			//If it hasnt been seen mark it seen
				} else if (seenCol[k][board[i][k] - '1']){			//If seen invalid
//                    System.out.println("BAD COL! " + i + " " + k);
					return false;
				}  
				if (!seenBox[(k/3)+(i/3)*3][board[i][k] - '1']){	//Box number adds 1 for every 3 spaces right, and 3 every 3 spaces down. numbered 1 2 3 along top 
//                    System.out.println("Box #" + ((k/3)+(i/3)*3) + " saw " + board[i][k]);
					seenBox[(k/3)+(i/3)*3][board[i][k] - '1'] = true;
				} else if (seenBox[(k/3)+(i/3)*3][board[i][k] - '1']){
//					System.out.println("BAD BOX! " + i + " " + k");
					return false;
				}
			}
		}
		return true;												//Made it to end so valid
		
	}
}