package edu.sustech.xiangqi.model;

public class CannonPiece extends AbstractPiece{

    public CannonPiece(int type, int row, int col, boolean isRed) {
        super(type, row, col, isRed);
    }
    public CannonPiece(int type, int row, int col, boolean isRed, boolean status) {
        super(type, row, col, isRed, status);
    }

    @Override
    public boolean canEat(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)==null) return false;
        if(row==getRow() && col==getCol()) return false;
        if(row!=getRow() && col!=getCol()) return false;
        if((isRed()==model.getPieceAt(row, col).isRed()))return false;
        if(betweenPieceNumber(getRow(), getCol(), row, col, model)!=1) return false;
        return true;
    }

    @Override
    public boolean canMove(ChessBoardModel model, int row, int col){
        if(!model.isValidPosition(row, col)) return false;
        if(model.getPieceAt(row, col)!=null) return false;
        if(row==getRow() && col==getCol()) return false;
        if(row!=getRow() && col!=getCol()) return false;
        if(betweenPieceNumber(getRow(), getCol(), row, col, model)!=0) return false;
        return true;
    }

    private int betweenPieceNumber(int currentRow, int currentCol, int targetRow, int targetCol, ChessBoardModel model) {
        int betweenPieceNumber = 0;
        boolean isHorizontal = (currentRow == targetRow);

        /* 不含端点 */

        if (isHorizontal){
            // 水平移动
            int startCol = Math.min(currentCol, targetCol) + 1;
            int endCol = Math.max(currentCol, targetCol);
            for (int col = startCol; col < endCol; col++) {
                if (model.getPieceAt(currentRow, col) != null) {
                    betweenPieceNumber++;
                }
            }
        }else{
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
}

