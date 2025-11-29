package edu.sustech.xiangqi.model;

public class GeneralPiece extends AbstractPiece {

    public GeneralPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public GeneralPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    @Override
    public boolean canEat(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)==null) return false;
        if(row==getRow() && col==getCol()) return false;
        if((isRed()==model.getPieceAt(row, col).isRed()))return false;
        int rowDiff = Math.abs(row - getRow());
        int colDiff = Math.abs(col - getCol());
        if(rowDiff+colDiff>1) return false;
        if (isRed())return row >= 7 && row <= 9 && col >= 3 && col <= 5;
        else return row >= 0 && row <= 2 && col >= 3 && col <= 5;
    }

    @Override
    public boolean canMove(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)!=null) return false;
        if(row==getRow() && col==getCol()) return false;
        int rowDiff = Math.abs(row - getRow());
        int colDiff = Math.abs(col - getCol());
        if(rowDiff+colDiff>1) return false;
        if (isRed())return row >= 7 && row <= 9 && col >= 3 && col <= 5;
        else return row >= 0 && row <= 2 && col >= 3 && col <= 5;
    }
}
