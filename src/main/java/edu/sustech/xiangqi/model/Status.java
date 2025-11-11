package edu.sustech.xiangqi.model;

import java.util.ArrayList;
import java.util.List;

public class Status {
    private final List<Step> steps;
    private static final int ROWS = 10;
    private static final int COLS = 9;
    private int[][] boardStatus = new int[10][9];

    public Status(List<AbstractPiece> pieces){
        steps = new ArrayList<>();
        initBoardStatus(pieces);
    }

    private void initBoardStatus(List<AbstractPiece> pieces){
        for(AbstractPiece piece:pieces){
            setStatus(piece.getRow(), piece.getCol(), piece.getId());
        }
    }

    private boolean checkValid(int tarRow, int tarCol,int num){
        return true;
    }

    public boolean updateBoards(Step nowStep){
        steps.add(nowStep);
        setStatus(nowStep.getFromRow(), nowStep.getFromCol(), 0);
        setStatus(nowStep.getToRow(), nowStep.getToCol(), nowStep.getPieceID());
        return true;
    }

    public boolean setStatus(int tarRow, int tarCol,int num){
        if(checkValid(tarRow, tarCol, num)){
            boardStatus[tarRow][tarCol] = num;
            return true;
        }
        return false;
    }

    public int[][] getBoardNow(){
        return boardStatus;
    }
}
