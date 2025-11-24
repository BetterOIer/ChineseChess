package edu.sustech.xiangqi.model;

public class AdvisorPiece extends AbstractPiece{
    public AdvisorPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public AdvisorPiece(int type, int row, int col, boolean isRed, boolean status) {
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

        // 仕/士的移动规则：
        // 1.九宫内走斜线一格（对角线）;

        if (isRed()) {
            //红方
            if (targetRow >= 7 && targetRow <= 9 && targetCol >= 3 && targetCol <= 5) {
                return rowDiff == 1 && colDiff == 1;
            }
        } else {
            //黑方
            if (targetRow >= 0 && targetRow <= 2 && targetCol >= 3 && targetCol <= 5) {
                return rowDiff == 1 && colDiff == 1;
            }
        }
        return false;
    }
}
