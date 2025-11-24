package edu.sustech.xiangqi.model;

public class GeneralPiece extends AbstractPiece {

    public GeneralPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public GeneralPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();

        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        int rowDiff = Math.abs(targetRow - currentRow);
        int colDiff = Math.abs(targetCol - currentCol);

        // 兵/卒的移动规则：
        // 1.九宫内走一格直线；
        // 2.不得对面见将

        if (isRed()) {
            if (targetRow >= 7 && targetRow <= 9 && targetCol >= 3 && targetCol <= 5) {
                if(rowDiff == 1 && colDiff == 0){
                    return true;
                }
                return rowDiff == 0 && colDiff == 1;
            }
        }
        else {
            if (targetRow >= 0 && targetRow <= 2 && targetCol >= 3 && targetCol <= 5) {
                if(rowDiff == 1 && colDiff == 0){
                    return true;
                }
                return rowDiff == 0 && colDiff == 1;
            }
        }
        return false;
    }
}
