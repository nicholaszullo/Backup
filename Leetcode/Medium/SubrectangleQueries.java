/**
 *  N represents rows, M represents columns 
 *  Constructor runtime O(N*M)  Can be O(1) if make a shallow copy instead of a deep copy
 *  updateSubrectangle  O(N*M) worst case
 *  getValue O(1)
 *  Memory O(N*M)   Can be O(1) if make a shallow copy instead of a deep copy
 */
public class SubrectangleQueries {
    int[][] rectangle;
    public SubrectangleQueries(int[][] rectangle) {
        this.rectangle = new int[rectangle.length][rectangle[0].length];
        for (int i = 0; i < rectangle.length; i++){
            for (int j = 0; j < rectangle[0].length; j++){
                this.rectangle[i][j] = rectangle[i][j]; 
            }
        }
    }
    
    public void updateSubrectangle(int row1, int col1, int row2, int col2, int newValue) {
        for (int i = row1; i <= row2; i++){
            for (int j = col1; j <= col2; j++){
                rectangle[i][j] = newValue;
            }
        }
    }
    
    public int getValue(int row, int col) {
        return rectangle[row][col];
    }
}

/**
 * Your SubrectangleQueries object will be instantiated and called as such:
 * SubrectangleQueries obj = new SubrectangleQueries(rectangle);
 * obj.updateSubrectangle(row1,col1,row2,col2,newValue);
 * int param_2 = obj.getValue(row,col);
 */