package edu.sustech.xiangqi.model;

public class ElephantPiece extends AbstractPiece{
    public ElephantPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public ElephantPiece(int type, int row, int col, boolean isRed, boolean status) {
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
        if(rowDiff !=2 || colDiff !=2) return false;
        if(model.getPieceAt((row+getRow())/2, (col+getCol())/2)!=null) return false;
        if(isRed()){if(row<5) return false;}
        else{if(row>=5) return false;}
        return true;
    }

    @Override
    public boolean canMove(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)!=null) return false;
        if(row==getRow() && col==getCol()) return false;
        int rowDiff = Math.abs(row - getRow());
        int colDiff = Math.abs(col - getCol());
        if(rowDiff !=2 || colDiff !=2) return false;
        if(model.getPieceAt((row+getRow())/2, (col+getCol())/2)!=null) return false;
        if(isRed()){if(row<5) return false;}
        else{if(row>=5) return false;}
        return true;
    }
}
