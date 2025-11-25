package edu.sustech.xiangqi.model;

public class ElephantPiece extends AbstractPiece{
    public ElephantPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public ElephantPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    public boolean canBasicMove(int currentRow, int currentCol, int targetRow, int targetCol) {
        int rowDiff = Math.abs(targetRow - currentRow);
        int colDiff = Math.abs(targetCol - currentCol);

        // 象/相的移动规则:
        // 1.走田字（斜线两格）;
        // 2.不能过河;
        // 3.堵象眼不可走;

        //非原地移动
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        // 堵象眼
        int eyeRow = (currentRow + targetRow) / 2;
        int eyeCol = (currentCol + targetCol) / 2;
        boolean isBlocked = model.getPieceAt(eyeRow, eyeCol) != null;

        if (isRed()) {
            // 不能过河
            boolean crossedRiver = currentRow < 5;
            if (!crossedRiver && !isBlocked) {
                return rowDiff == 2 && colDiff == 2;

            }
        } else {
            boolean crossedRiver = currentRow >= 5;
            if (!crossedRiver && !isBlocked) {
                return rowDiff == 2 && colDiff == 2;
            }
        }
        return false;
    }
}
