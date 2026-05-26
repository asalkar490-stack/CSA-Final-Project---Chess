package Board;

public class Location {
    
    private int row;
    private int column;
    
    public Location(int row, int col) {
        this.row = row;
        this.column = col; 
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return column;
    }
    public void printRow() {
        System.out.println(getRow());
    }
    public void printCol() {
        System.out.println(getCol());
    }
    public String toString() {
        return row + " " + column;
    }
}
