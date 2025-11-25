package edu.sustech.xiangqi.model;

/**
 * 兵/卒
 */
public class SoldierPiece extends AbstractPiece {

    public SoldierPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public SoldierPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    public boolean canBasicMove(int currentRow, int currentCol, int targetRow, int targetCol) {
        int rowDiff = targetRow - currentRow;
        int colDiff = Math.abs(targetCol - currentCol);
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }
        // 兵/卒的移动规则：
        // 1. 未过河前只能向前走一步
        // 2. 过河后可以向前、向左、向右走一步，但不能后退
        if (isRed()) {
            // 红方兵（向上走，row减小）
            boolean crossedRiver = currentRow < 5; // 过了楚河汉界

            if (!crossedRiver) {
                // 未过河：只能向前（向上）走一步
                return rowDiff == -1 && colDiff == 0;
            } else {
                // 过河后：可以向前、向左、向右走一步
                if (rowDiff == -1 && colDiff == 0) return true; // 向前
                return rowDiff == 0 && colDiff == 1;  // 向左或向右
            }
        } else {
            // 黑方卒（向下走，row增大）
            boolean crossedRiver = currentRow >= 5; // 过了楚河汉界

            if (!crossedRiver) {
                // 未过河：只能向前（向下）走一步
                return rowDiff == 1 && colDiff == 0;
            } else {
                // 过河后：可以向前、向左、向右走一步
                if (rowDiff == 1 && colDiff == 0) return true; // 向前
                return rowDiff == 0 && colDiff == 1; // 向左或向右
            }
        }
    }
}
