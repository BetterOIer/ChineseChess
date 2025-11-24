package edu.sustech.xiangqi.model;

public abstract class AbstractPiece {
    private final int type;
    /* 
         1   2  3   4   5  6   7
    红方:車，馬，炮，相，仕，兵，帥
    黑方:車，馬，炮，象，士，卒，將
    */
    private final boolean isRed;
    private int row;
    private int col;
    private int id;
    private boolean alive;
    private static int idCnt;

    public AbstractPiece(int type, int row, int col, boolean isRed, boolean alive) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.isRed = isRed;
        this.alive=alive;
        this.id = ++idCnt;
    }

    public AbstractPiece(int type, int row, int col, boolean isRed) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.isRed = isRed;
        this.alive=true;
        this.id = ++idCnt;
    }

    public int getId(){
        return this.id;
    }

    public void resetId(){
        idCnt=0;
    }

    public int getType() {
        return type;
    }

    public String getName(){
        if(this.isRed){
            if(this.type==1) return "車";
            else if(this.type==2) return "馬";
            else if(this.type==3) return "炮";
            else if(this.type==4) return "相";
            else if(this.type==5) return "仕";
            else if(this.type==6) return "兵";
            else if(this.type==7) return "帥";
            else return "";
        }else{
            if(this.type==1) return "車";
            else if(this.type==2) return "馬";
            else if(this.type==3) return "炮";
            else if(this.type==4) return "象";
            else if(this.type==5) return "士";
            else if(this.type==6) return "卒";
            else if(this.type==7) return "將";
            else return "";
        }
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
    public void setStatus(boolean status){
        this.alive=status;
    }
    public boolean getStatus(){
        return this.alive;
    }

    /**
     * 判断棋子是否可以移动到目标位置
     * @return 是否可以移动
     */
    public abstract boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model);
}
