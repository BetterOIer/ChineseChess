package edu.sustech.xiangqi.model;

public class Step {
    private int pieceId;
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private int stepId;
    private int mode; //0 for move, 1 for eat.

    private static int stepIdCnt;

    public Step(int pieceId,int fromRow, int fromCol, int toRow, int toCol, int mode){
        this.pieceId = pieceId;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.mode = mode;
        this.stepId = ++stepIdCnt;
    }

    public int getMode(){
        return this.mode;
    }
    public int getPieceID(){
        return this.pieceId;
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
