package edu.sustech.xiangqi.model;

public class Step {
    private int pieceType;
    private boolean isRed;
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private int mode; //0 for move, 1 for eat.
    //If is mode 1, Please write toRow=toCol=-1;


    public Step(int pieceType,boolean isRed, int fromRow, int fromCol, int toRow, int toCol, int mode){
        this.pieceType = pieceType;
        this.isRed = isRed;
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
    public boolean getIsRed(){
        return this.isRed;
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


    @Override
    public String toString(){
        return "Type "+this.pieceType + (this.isRed?" Red":" Black")+ " move from ("+this.fromRow+", "+this.fromCol+") to ("+this.toRow+", "+this.toCol+")";
    }

    public String getStepNameInCh(){
        if (toRow == -1 && toCol == -1) return "被吃";

        String[] redNames = {"", "車", "馬", "炮", "相", "仕", "兵", "帥"};
        String[] blackNames = {"", "車", "馬", "炮", "象", "士", "卒", "將"};
        String[] redNums = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] blackNums = {"", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        String name = isRed ? redNames[pieceType] : blackNames[pieceType];

        // 计算起始列号
        // 红方：从右向左 1-9 (col 8->1, col 0->9) => 9 - col
        // 黑方：从左向右 1-9 (col 0->1, col 8->9) => col + 1
        String startColStr;
        if (isRed) {
            startColStr = redNums[9 - fromCol];
        } else {
            startColStr = blackNums[fromCol + 1];
        }

        String action;
        String endStr;

        if (fromRow == toRow) {
            action = "平";
            if (isRed) {
                endStr = redNums[9 - toCol];
            } else {
                endStr = blackNums[toCol + 1];
            }
        } else {
            // 纵向移动
            boolean movingForward;
            if (isRed) movingForward = fromRow > toRow; // 红方在下方(row大)，向上(row小)为进
            else movingForward = fromRow < toRow; // 黑方在上方(row小)，向下(row大)为进

            action = movingForward ? "进" : "退";

            // 直行棋子(车炮兵帅)显示步数，斜行棋子(马相士)显示落点列号
            // 1:車, 2:馬, 3:炮, 4:相/象, 5:仕/士, 6:兵/卒, 7:帥/將
            boolean isStraight = (pieceType == 1 || pieceType == 3 || pieceType == 6 || pieceType == 7);

            if (isStraight) {
                int distance = Math.abs(fromRow - toRow);
                if (isRed) endStr = redNums[distance];
                else endStr = blackNums[distance];
            } else {
                // 斜行棋子显示目标列
                if (isRed) endStr = redNums[9 - toCol];
                else endStr = blackNums[toCol + 1];
            }
        }

        return name + startColStr + action + endStr;
    }
    
}


