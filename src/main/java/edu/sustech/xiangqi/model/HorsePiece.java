package edu.sustech.xiangqi.model;

public class HorsePiece extends AbstractPiece {
    public HorsePiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public HorsePiece(int type, int row, int col, boolean isRed, boolean status) {
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
        if(rowDiff*rowDiff+colDiff*colDiff!=5) return false;
        if(rowDiff==2) return model.getPieceAt((row+getRow())/2, getCol())==null;
        else return model.getPieceAt(getRow(), (col+getCol())/2)==null;
    }

    @Override
    public boolean canMove(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)!=null) return false;
        if(row==getRow() && col==getCol()) return false;
        int rowDiff = Math.abs(row - getRow());
        int colDiff = Math.abs(col - getCol());
        if(rowDiff*rowDiff+colDiff*colDiff!=5) return false;
        if(rowDiff==2) return model.getPieceAt((row+getRow())/2, getCol())==null;
        else return model.getPieceAt(getRow(), (col+getCol())/2)==null;
    }
}
