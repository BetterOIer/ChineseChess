package edu.sustech.xiangqi.model;

public class RookPiece extends AbstractPiece{
    public RookPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public RookPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    public boolean canBasicMove(int currentRow, int currentCol, int targetRow, int targetCol) {

        //非原地移动
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        // 车的移动规则：
        // 直线任意步；
        // 无障碍可行；

        boolean isBlocked = false;
        boolean isHorizontal = (currentRow == targetRow);
        boolean isVertical = (currentCol == targetCol);
        if (!isHorizontal && !isVertical) {
            return false;
        }

        //水平移动
        if (isHorizontal){
            int startCol = Math.min(currentCol, targetCol);
            int endCol = Math.max(currentCol, targetCol);
            for (int i = startCol; i <= endCol; i++){
                if (model.getPieceAt(currentRow, i) != null){
                    isBlocked = true;
                    break;
                }
            }
        }else {
            //竖直移动
            int startRow = Math.min(currentRow, targetRow);
            int endRow = Math.max(currentRow, targetRow);
            for (int i = startRow; i <= endRow; i++){
                if (model.getPieceAt(i, currentCol) != null){
                    isBlocked = true;
                    break;
                }
            }
        }
        return !isBlocked;
    }
}
