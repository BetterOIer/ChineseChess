package edu.sustech.xiangqi.model;

public class Step {
    private int pieceType;
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private int mode; //0 for move, 1 for eat.
    //If is mode 1, Please write toRow=toCol=-1;


    public Step(int pieceType,int fromRow, int fromCol, int toRow, int toCol, int mode){
        this.pieceType = pieceType;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.mode = mode;
    }

    public int getMode(){
        return this.mode;
    }
    public int getPieceType(){
        return this.pieceType;
    }
    public int getFromRow(){
        return this.fromRow;
    }
    public int getToRow(){
        return this.toRow;
    }
    public int getFromCol(){
        return this.fromCol;
    }
    public int getToCol(){
        return this.toCol;
    }
    
}
