package edu.sustech.xiangqi.model;

public class SoldierPiece extends AbstractPiece {

    public SoldierPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public SoldierPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    @Override
    public boolean canEat(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)==null) return false;
        if(row==getRow() && col==getCol()) return false;
        if((isRed()==model.getPieceAt(row, col).isRed()))return false;
        int rowDiff = row - getRow();
        int colDiff = Math.abs(col - getCol());
        if(Math.abs(rowDiff)+colDiff>1) return false;
        if (isRed()) {
           if(getRow()>=5){
                // 未过河：只能向前（向上）走一步
                return rowDiff == -1 && colDiff == 0;
            }else{
                // 过河后：可以向前、向左、向右走一步
                if (rowDiff == -1 && colDiff == 0) return true; // 向前
                return rowDiff == 0 && colDiff == 1;  // 向左或向右
            }
        } else {
            if (getRow()<5){
                // 未过河：只能向前（向下）走一步
                return rowDiff == 1 && colDiff == 0;
            }else{
                // 过河后：可以向前、向左、向右走一步
                if (rowDiff == 1 && colDiff == 0) return true; // 向前
                return rowDiff == 0 && colDiff == 1; // 向左或向右
            }
        }
    }

    @Override
    public boolean canMove(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)!=null) return false;
        if(row==getRow() && col==getCol()) return false;
        int rowDiff = row - getRow();
        int colDiff = Math.abs(col - getCol());
        if(Math.abs(rowDiff)+colDiff>1) return false;
        if (isRed()) {
           if(getRow()>=5){
                // 未过河：只能向前（向上）走一步
                return rowDiff == -1 && colDiff == 0;
            }else{
                // 过河后：可以向前、向左、向右走一步
                if (rowDiff == -1 && colDiff == 0) return true; // 向前
                return rowDiff == 0 && colDiff == 1;  // 向左或向右
            }
        } else {
            if (getRow()<5){
                // 未过河：只能向前（向下）走一步
                return rowDiff == 1 && colDiff == 0;
            }else{
                // 过河后：可以向前、向左、向右走一步
                if (rowDiff == 1 && colDiff == 0) return true; // 向前
                return rowDiff == 0 && colDiff == 1; // 向左或向右
            }
        }
    }
}
