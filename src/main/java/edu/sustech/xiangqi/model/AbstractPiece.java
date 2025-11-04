package edu.sustech.xiangqi.model;

public abstract class AbstractPiece {
    private final String name;
    private final boolean isRed;
    private int row;
    private int col;

    public AbstractPiece(String name, int row, int col, boolean isRed) {
        this.name = name;
        this.row = row;
        this.col = col;
        this.isRed = isRed;
    }

    public String getName() {
        return name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isRed() {
        return isRed;
    }

    public void moveTo(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    /**
     * 判断棋子是否可以移动到目标位置
     * @return 是否可以移动
     */
    public abstract boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model);
}
