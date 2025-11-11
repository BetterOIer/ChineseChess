package edu.sustech.xiangqi.model;

public class HorsePiece extends AbstractPiece {
    public HorsePiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();

        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        int rowDiff = targetRow - currentRow;
        int colDiff = Math.abs(targetCol - currentCol);

        //走日字（先直一格再斜一格）；蹩马腿不可走
        if (isRed()) {
            //红方
            if (targetRow >= 0 && targetRow <= 10 && targetCol >= 0 && targetCol <= 9) {
                if(Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 1){
                    return true;
                }
                return Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 2;
            }

        } else {
            //黑方
            if (currentRow >= 0 && currentRow <= 10 && currentCol >= 0 && currentCol <= 9) {
                if(Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 1){
                    return true;
                }
                return Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 2;
            }
        }
        return false;
    }
}
