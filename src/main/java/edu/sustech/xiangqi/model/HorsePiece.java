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
        int colDiff = targetCol - currentCol;
        //蹩马脚
        boolean isStopped = false;
        int footCol;
        int footRow;
        if (Math.abs(rowDiff) == 1 && colDiff == -2){
            footCol = currentCol - 1;
            if (model.getPieceAt(currentRow, footCol) != null){
                isStopped = true;
            }
        }else if (Math.abs(colDiff) == 1 && rowDiff == -2){
            footRow = currentRow - 1;
            if (model.getPieceAt(footRow, currentCol) != null){
                isStopped = true;
            }
        } else if (Math.abs(rowDiff) == 1 && colDiff == 2) {
            footCol = currentCol + 1;
            if (model.getPieceAt(currentRow, footCol) != null){
                isStopped = true;
            }
        }else if (Math.abs(colDiff) == 1 && rowDiff == 2){
            footRow = currentRow + 1;
            if (model.getPieceAt(footRow, currentCol) != null){
                isStopped = true;
            }
        }


        //走日字（先直一格再斜一格）；蹩马腿不可走
        if(!isStopped){
            if(Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 1){
                return true;
            }
            return Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 2;
        }
        return false;
    }
}
