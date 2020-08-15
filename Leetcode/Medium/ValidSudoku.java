/**
 * Runtime O(3N^2)
 * Memory O(27N)
 */

import java.util.HashSet;

public class ValidSudoku {
	public boolean isValidSudoku(char[][] board) {
		HashSet<Character> seen = new HashSet<Character>();
		for (int i = 0; i < board.length; i++){             //Check Rows
			seen = new HashSet<Character>();
			for (int k = 0; k < board[i].length; k++){ 
				if (board[i][k] == '.')
					continue;
				else if (!seen.add(board[i][k])){
					return false;
				}
			}
		}
		for (int i = 0; i < board.length; i++){             //Check columns
			seen = new HashSet<Character>();
			for (int k = 0; k < board[i].length; k++){ 
				if (board[k][i] == '.')
					continue;
				else if (!seen.add(board[k][i])){
					return false;
				}
			}
		}
		for (int i = 0; i < board.length; i += 3){			//i is the top corner row
			for (int k = 0; k < board[i].length; k += 3){	//k is the top corner column
				seen = new HashSet<Character>();
				for (int ii = i; ii < i+3; ii++){			//Check the 3x3 box with i and k as the top corner
					for (int kk = k; kk < k+3; kk++){
						if (board[ii][kk] == '.')
							continue;
						else if (!seen.add(board[ii][kk])){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}