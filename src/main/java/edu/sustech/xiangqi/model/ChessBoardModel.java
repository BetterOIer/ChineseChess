package edu.sustech.xiangqi.model;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardModel {
    //棋盘信息
    private String name; //棋盘的名字
    private String lastModTime; //棋盘最后修改的时间
    private static final int ROWS = 10;
    private static final int COLS = 9;
    private int id;
    private int boardType;
    //采用位运算叠加
    //xxx 仅棋盘1 远程2 AI4 已终结8
    private String description;
    //预留；


    //棋子信息
    private List<AbstractPiece> pieces;//这里存所有的棋子

    //过程信息
    private List<Step> steps;
    private int[][] boardStatus = new int[ROWS][COLS];


    public ChessBoardModel(int id, int boardType) {
        this.id=id;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
    }
    public ChessBoardModel(int id, String name, int boardType) {
        this.id=id;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
    }
    public ChessBoardModel(int id, int boardType, String description) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
    }
    public ChessBoardModel(int id, String name, int boardType, String description) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
    }
    public ChessBoardModel(int id, int boardType, List<AbstractPiece> pieces, List<Step> steps) {
        this.id=id;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
    }
    public ChessBoardModel(int id, String name, int boardType, List<AbstractPiece> pieces, List<Step> steps) {
        this.id=id;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
    }
    public ChessBoardModel(int id, int boardType, String description, List<AbstractPiece> pieces, List<Step> steps) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
    }
    public ChessBoardModel(int id, String name, int boardType, String description, List<AbstractPiece> pieces, List<Step> steps) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
    }

    private void initPieces() {
        // 黑方棋子
        pieces.add(new GeneralPiece(7, 0, 4, false));
        pieces.add(new SoldierPiece(6, 3, 0, false));
        pieces.add(new SoldierPiece(6, 3, 2, false));
        pieces.add(new SoldierPiece(6, 3, 4, false));
        pieces.add(new SoldierPiece(6, 3, 6, false));
        pieces.add(new SoldierPiece(6, 3, 8, false));
        pieces.add(new HorsePiece(2, 0, 7, false));
        pieces.add(new HorsePiece(2, 0, 1, false));
        pieces.add(new AdvisorPiece(5, 0, 3, false));
        pieces.add(new AdvisorPiece(5, 0, 5, false));
        pieces.add(new ElephantPiece(4, 0, 2, false));
        pieces.add(new ElephantPiece(4, 0, 6, false));
        pieces.add(new RookPiece(1, 0, 0, false));
        pieces.add(new RookPiece(1, 0, 8, false));
        pieces.add(new CannonPiece(3, 2, 1, false));
        pieces.add(new CannonPiece(3, 2, 7, false));
        // 红方棋子
        pieces.add(new GeneralPiece(7, 9, 4, true));
        pieces.add(new SoldierPiece(6, 6, 0, true));
        pieces.add(new SoldierPiece(6, 6, 2, true));
        pieces.add(new SoldierPiece(6, 6, 4, true));
        pieces.add(new SoldierPiece(6, 6, 6, true));
        pieces.add(new SoldierPiece(6, 6, 8, true));
        pieces.add(new HorsePiece(2, 9, 7, true));
        pieces.add(new HorsePiece(2, 9, 1, true));
        pieces.add(new AdvisorPiece(5, 9, 3, true));
        pieces.add(new AdvisorPiece(5, 9, 5, true));
        pieces.add(new ElephantPiece(4, 9, 2, true));
        pieces.add(new ElephantPiece(4, 9, 6, true));
        pieces.add(new RookPiece(1, 9, 0, true));
        pieces.add(new RookPiece(1, 9, 8, true));
        pieces.add(new CannonPiece(3, 7, 1, true));
        pieces.add(new CannonPiece(3, 7, 7, true));
    }

    private boolean checkValid(int tarRow, int tarCol,int num){
        return true;
    }

    private void initBoardStatus(List<AbstractPiece> pieces){
        for(AbstractPiece piece:pieces){
            setStatus(piece.getRow(), piece.getCol(), piece.getId());
        }
    }
    public boolean setStatus(int tarRow, int tarCol,int num){
        if(checkValid(tarRow, tarCol, num)){
            boardStatus[tarRow][tarCol] = num;
            return true;
        }
        return false;
    }
    public boolean updateBoards(Step nowStep){
        steps.add(nowStep);
        setStatus(nowStep.getFromRow(), nowStep.getFromCol(), 0);
        if(nowStep.getMode()==0)setStatus(nowStep.getToRow(), nowStep.getToCol(), nowStep.getPieceType());
        return true;
    }
    public int[][] getBoardNow(){
        return boardStatus;
    }

    public List<Step> getSteps(){
        return steps;
    }
    public void setSteps(List<Step> steps){
        this.steps = steps;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }

    public List<AbstractPiece> getPieces() {
        return pieces;
    }
    public void setPieces(List<AbstractPiece> pieces) {
        this.pieces = pieces;
    }

    public AbstractPiece getPieceAt(int row, int col) {
        for (AbstractPiece piece : pieces) {
            if (piece.getRow() == row && piece.getCol() == col) {
                return piece;
            }
        }
        return null;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public boolean movePiece(AbstractPiece piece, int newRow, int newCol) {
        if (!isValidPosition(newRow, newCol)) {
            return false;
        }

        if (!piece.canMoveTo(newRow, newCol, this)) {
            return false;
        }
        Step nowStep = new Step(piece.getType(),piece.getRow(),piece.getCol(),newRow,newCol, 0);
        updateBoards(nowStep);
        piece.moveTo(newRow, newCol);
        try{
            DBOperationBoard.updateBoardNowStatus(this.id, pieces);
            DBOperationBoard.updateBoardHistory(this.id,steps);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public static int getRows() {
        return ROWS;
    }

    public static int getCols() {
        return COLS;
    }

    public String getLastModTime(){
        return this.lastModTime;
    }
    public void setLastModTime(String lastModTime){
        this.lastModTime=lastModTime;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getType(){
        return this.boardType;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
}
