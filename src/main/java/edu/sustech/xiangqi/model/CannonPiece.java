package edu.sustech.xiangqi.model;

public class CannonPiece extends AbstractPiece{

    public CannonPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public CannonPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();

        //中间无子：直接移动但不吃子；
        //中间隔一子：若目标位置有对方子，移动且吃子；
        //其他无法移动或吃子；

        if (!canBasicMove(currentRow, currentCol, targetRow, targetCol, model)
                || model.getPieceAt(targetRow, targetCol) != null) {
            return false;
        }
        return betweenPieceNumber(currentRow, currentCol, targetRow, targetCol, model) == 0;
    }

    @Override
    public boolean canEat(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();

        //能否移动
        if (!canBasicMove(currentRow, currentCol, targetRow, targetCol, model)) {
            return false;
        }

        // 能否吃子：目标是敌方棋子 + 有且仅有1个炮架
        return model.getPieceAt(targetRow, targetCol) != null
                && model.getPieceAt(targetRow, targetCol).isRed() != this.isRed()
                && betweenPieceNumber(currentRow, currentCol, targetRow, targetCol, model) == 1;
    }

    private int betweenPieceNumber(int currentRow, int currentCol, int targetRow, int targetCol, ChessBoardModel model) {
        int betweenPieceNumber = 0;
        boolean isHorizontal = (currentRow == targetRow);

        if (isHorizontal) {
            // 水平移动
            int startCol = Math.min(currentCol, targetCol) + 1;
            int endCol = Math.max(currentCol, targetCol);
            for (int col = startCol; col < endCol; col++) {
                if (model.getPieceAt(currentRow, col) != null) {
                    betweenPieceNumber++;
                }
            }
        } else {
            // 竖直移动
            int startRow = Math.min(currentRow, targetRow) + 1;
            int endRow = Math.max(currentRow, targetRow);
            for (int row = startRow; row < endRow; row++) {
                if (model.getPieceAt(row, currentCol) != null) {
                    betweenPieceNumber++;
                }
            }
        }
        return betweenPieceNumber;
    }
    public boolean canBasicMove(int currentRow, int currentCol, int targetRow, int targetCol, ChessBoardModel model) {

        // 非原地移动
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }
        // 平直走
        boolean isHorizontal = (currentRow == targetRow);
        boolean isVertical = (currentCol == targetCol);
        if (!isHorizontal && !isVertical) {
            return false;
        }
        //至少隔一子才能走
        return Math.abs(currentCol - targetCol) > 1 || Math.abs(currentRow - targetRow) > 1;
    }
}

