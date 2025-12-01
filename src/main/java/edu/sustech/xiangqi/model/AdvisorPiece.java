package edu.sustech.xiangqi.model;

public class AdvisorPiece extends AbstractPiece{
    public AdvisorPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public AdvisorPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    @Override
    public boolean canMove(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)!=null) return false;
        if(row==getRow() && col==getCol()) return false;
        int rowDiff = Math.abs(row - getRow());
        int colDiff = Math.abs(col - getCol());
        if (isRed()) {
            //红方
            if (row >= 7 && row <= 9 && col >= 3 && col <= 5) {
                return rowDiff == 1 && colDiff == 1;
            }
        } else {
            //黑方
            if (row >= 0 && row <= 2 && col >= 3 && col <= 5) {
                return rowDiff == 1 && colDiff == 1;
            }
        }
        return false;
    }

    @Override
    public boolean canEat(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)==null) return false;
        if(row==getRow() && col==getCol()) return false;
        if((isRed()==model.getPieceAt(row, col).isRed()))return false;
        int rowDiff = Math.abs(row - getRow());
        int colDiff = Math.abs(col - getCol());
        if (isRed()) {
            //红方
            if (row >= 7 && row <= 9 && col >= 3 && col <= 5) {
                return rowDiff == 1 && colDiff == 1;
            }
        } else {
            //黑方
            if (row >= 0 && row <= 2 && col >= 3 && col <= 5) {
                return rowDiff == 1 && colDiff == 1;
            }
        }
        return false;
    }
}
