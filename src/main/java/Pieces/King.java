package Pieces;

public class King extends Piece {
    
    private boolean hasMoved;

    public King(String color, int row, int col){
        super(color, row, col);
        hasMoved = false;
    }

    public boolean isLegal(int row, int col, Piece[][] board){

        if (row == this.row && col == this.col)
            return false;

        if (Math.abs(row - this.row) <= 1 && Math.abs(col - this.col) <= 1)
            return true;

        return false;
    }

    public void castle(int row, int col, Piece[][] board){
        //black queenside
        if (canCastle(row, col, board)){
            if (row == 0 && col == 2){
                board[this.row][this.col] = null;
                board[row][col] = this;
                Piece temp = board[row][0];
                board[row][0] = null;
                board[row][3] = temp;
            }

            //black kingside
            if (row == 0 && col == 6){
                board[this.row][this.col] = null;
                board[row][col] = this;
                Piece temp = board[row][7];
                board[row][7] = null;
                board[row][5] = temp;
            }

            //white, queenside
            if (row == 7 && col == 2){
                board[this.row][this.col] = null;
                board[row][col] = this;
                Piece temp = board[row][0];
                board[row][0] = null;
                board[row][3] = temp;
            }

            //white kingside
            if (row == 7 && col == 6){
                board[this.row][this.col] = null;
                board[row][col] = this;
                Piece temp = board[row][7];
                board[row][7] = null;
                board[row][5] = temp;
            }

            this.row = row;
            this.col = col;
            hasMoved = true;
        }
    }

    public boolean hasMoved(){
        return hasMoved;   
    }

    

    public boolean canCastle(int row, int col, Piece[][] board){
        if (hasMoved())return false;

        //check white side
        if (this.getColor().equals("White")){

            //white queenside
            if (row == 7 && col == 2 && board[7][0] != null && board[7][0].getType().equals("Rook") && !board[7][0].hasMoved() && board[7][1] == null && board[7][2] == null && board[7][3] == null)
                return true;

            //white kingside
            if (row == 7 && col == 6 && board[7][7] != null && board[7][7].getType().equals("Rook") && !board[7][7].hasMoved() && board[7][5] == null && board[7][6] == null)
                return true;
        }

        //check left side
        if (this.getColor().equals("Black")){

            //black queenside
            if (row == 0 && col == 2 && board[0][0] != null && board[0][0].getType().equals("Rook") && !board[0][0].hasMoved() && board[0][1] == null && board[0][2] == null && board[0][3] == null)
                return true;

            //black kingside
            if (row == 0 && col == 6 && board[0][7] != null && board[0][7].getType().equals("Rook") && !board[0][7].hasMoved() && board[0][5] == null && board[0][6] == null)
                return true;
        }
        return false;
    }

    public String getType(){
        return "King";
    }

    public int getValue(){
        return 0;
    }
}
