/** Still 2ms online, fastest is 1. got rid of 2 double for loops, so its faster than before regardless
 * Runtime O(N^2) where N is the length of row in the board
 * Memory O(N) top 97 in memory even with hashsets for row
 */

import java.util.HashSet;

public class ValidSudoku {
	public boolean isValidSudoku(char[][] board) {
		HashSet<Character> seenRow = new HashSet<Character>();	//Could use an array here but easier to leave from earlier solution. Would it be faster? slightly less memory probably because primitive array not objects
		boolean[][] seenCol = new boolean[9][9];				//Need space for each column to have each number 
		boolean[][] seenBox = new boolean[9][9];				//Space for each box to have each number
		for (int i = 0; i < board.length; i++){					//For every row
			seenRow = new HashSet<Character>();					//Reset values seen in row
			for (int k = 0; k < board[i].length; k++){ 			//For every column
				if (board[i][k] == '.'){							//Blank space go to next 
					continue;
				} else if (!seenRow.add(board[i][k])){			//If seen in this row, invalid
//					System.out.println("BAD ROW! " + i + " " + k");
					return false;
				}
                if (seenCol[k][board[i][k] - '1'] == false){	//Column k will repeat down the rows, and go to index board[i][k] - 1 (1 stored at 0)
					seenCol[k][board[i][k] - '1'] = true;		//If it hasnt been seen mark it seen
				} else if (seenCol[k][board[i][k] - '1'] == true){	//If seen invalid
//                    System.out.println("BAD COL! " + i + " " + k);
					return false;
				}  
                if (seenBox[(k/3)+(i/3)*3][board[i][k] - '1'] == false){		//Box number adds 1 for every 3 spaces right, and 3 every 3 spaces down. numbered 1 2 3 along top 
//                    System.out.println("Box #" + ((k/3)+(i/3)*3) + " saw " + board[i][k]);
					seenBox[(k/3)+(i/3)*3][board[i][k] - '1'] = true;
				} else if (seenBox[(k/3)+(i/3)*3][board[i][k] - '1'] == true){
//					System.out.println("BAD BOX! " + i + " " + k");
					return false;
				}
			}
		}
        return true;										//Made it to end so valid
		
	}
}