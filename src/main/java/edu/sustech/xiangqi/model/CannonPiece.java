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

        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        // 炮的移动规则：
        // 1.平直走；
        // 2.吃子需隔一子（炮架）;

        boolean hasCarriage = false;
        boolean isHorizontal = (currentRow == targetRow);
        boolean isVertical = (currentCol == targetCol);
        if (!isHorizontal && !isVertical) {
            return false;
        }
        int carriageCol = targetCol - 1;
        int carriageRow = targetRow - 1;

        //水平移动
        if (isHorizontal){
            if (model.getPieceAt(carriageRow, carriageCol ) != null){
                    hasCarriage = true;
            }
        }else {
            //竖直移动
            if (model.getPieceAt(carriageRow, carriageCol) != null) {
                hasCarriage = true;
            }
        }
        return hasCarriage;
    }
}
