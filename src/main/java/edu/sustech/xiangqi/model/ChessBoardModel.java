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
    private AbstractPiece selectedPiece = null;
    private List<Coordinate> moveRange;
    private List<Coordinate> eatRange;

    //过程信息
    private List<Step> steps;
    private int[][] boardStatus = new int[ROWS][COLS];

    //用户信息
    private User userRed;
    private User userBlack;
    private User userOwner;
    private boolean whoseTurn;

    @Override
    public String toString(){
        return name+" "+boardType+" "+description+" "+userRed+" "+userBlack+" "+whoseTurn;
    }

    //构造函数
    public ChessBoardModel(int id, int boardType, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
    }
    public ChessBoardModel(int id, String name, int boardType, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
    }
    public ChessBoardModel(int id, int boardType, String description, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
    }
    public ChessBoardModel(int id, String name, int boardType, String description, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
    }
    public ChessBoardModel(int id, int boardType, List<AbstractPiece> pieces, List<Step> steps, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
    }
    public ChessBoardModel(int id, String name, int boardType, List<AbstractPiece> pieces, List<Step> steps, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
    }
    public ChessBoardModel(int id, int boardType, String description, List<AbstractPiece> pieces, List<Step> steps, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
    }
    public ChessBoardModel(int id, String name, int boardType, String description, List<AbstractPiece> pieces, List<Step> steps, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
    }
    

    //初始化
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
    private void initBoardStatus(List<AbstractPiece> pieces){
        for(AbstractPiece piece:pieces){
            setStatus(piece.getRow(), piece.getCol(), piece.getId());
        }
    }


    //Getter Setter
    //棋盘
    public int[][] getBoardNow(){
        return boardStatus;
    }
    public void setStatusAll(int[][] boardNow){
        this.boardStatus = boardNow;
    }
    public void setStatus(int tarRow, int tarCol,int num){
        boardStatus[tarRow][tarCol] = num;
    }
    public static int getRows(){
        return ROWS;
    }
    public static int getCols(){
        return COLS;
    }

    //历史
    public List<Step> getSteps(){
        return steps;
    }
    public void setSteps(List<Step> steps){
        this.steps = steps;
    }

    //ID
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }

    //名字
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    //类型
    public int getType(){
        return this.boardType;
    }
    public void setType(int type){
        this.boardType=type;
    }

    //描述
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    //修改时间
    public String getLastModTime(){
        return this.lastModTime;
    }
    public void setLastModTime(String lastModTime){
        this.lastModTime=lastModTime;
    }

    //棋子
    public AbstractPiece getPieceAt(int row, int col) {
        for (AbstractPiece piece : pieces) {
            if (piece.getRow() == row && piece.getCol() == col) {
                return piece;
            }
        }
        return null;
    }
    public List<AbstractPiece> getPieces() {
        return pieces;
    }
    public void setPieces(List<AbstractPiece> pieces) {
        this.pieces = pieces;
    }
    public int getSelectedRow() {
        return selectedPiece.getRow();
    }
    public void setSelectedRow(int selectedRow){
        this.selectedPiece.setRow(selectedRow);
    }
    public int getSelectedCol() {
        return selectedPiece.getCol();
    }
    public void setSelectedCol(int selectedCol){
        this.selectedPiece.setCol(selectedCol);
    }
    public List<Coordinate> getMoveRange(){
        return this.moveRange;
    }
    public List<Coordinate> getEatRange(){
        return this.eatRange;
    }

    //用户
    public User getUserRed(){
        return this.userRed;
    }
    public User getUserBlack(){
        return this.userBlack;
    }
    public User getUserOwner(){
        return this.userOwner;
    }
    public boolean getWhoseTurn(){
        return this.whoseTurn;
    }

    public boolean updateBoards(Step nowStep, boolean updateSteps){
        if(updateSteps)steps.add(nowStep);
        setStatus(nowStep.getFromRow(), nowStep.getFromCol(), 0);
        if(nowStep.getMode()==0)setStatus(nowStep.getToRow(), nowStep.getToCol(), nowStep.getPieceType());
        return true;
    }
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }
    public AbstractPiece trySelectPiece(int row, int col){
        if(((this.boardType&8)!=0)) return null;
        if(getPieceAt(row, col)!=null && getPieceAt(row, col).isRed()!=whoseTurn) return null;
        this.selectedPiece=getPieceAt(row, col);
        refreshTar();
        return this.selectedPiece;
    }
    private void refreshTar(){
        if(selectedPiece==null) return;
        moveRange = new ArrayList<>();
        eatRange = new ArrayList<>();
        for(int i = 0;i<ROWS;i++){
            for(int j = 0;j<COLS;j++){
                if(selectedPiece.canMove(this, i, j)) moveRange.add(new Coordinate(i, j));
                if(selectedPiece.canEat(this, i,j)) eatRange.add(new Coordinate(i, j));
            }
        }
        /* System.out.println("Can move:"+this.moveRange);
        System.out.println("Can eat:"+this.eatRange); */
    }
    public void caneclSelection(){
        this.selectedPiece = null;
    }
    public void tryMovePiece(int row, int col){
        if(this.selectedPiece==null) return;
        if(!isValidPosition(row, col)) return;
        if(moveRange != null && moveRange.contains(new Coordinate(row, col))){
            Step nowStep = new Step(selectedPiece.getType(),selectedPiece.getRow(),selectedPiece.getCol(), row, col, 0);
            updateBoards(nowStep,true);
            this.selectedPiece.moveTo(row, col);
            this.whoseTurn=!this.whoseTurn;
            setLastModTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            try{
                DBOperationBoard.updateBoardNowStatus(this.id, this.pieces);
                DBOperationBoard.updateBoardHistory(this.id,steps);
                DBOperationBoard.updateBoardDate(this.id, this.lastModTime);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    public void tryEatPiece(int row, int col){
        if(this.selectedPiece==null) return;
        if(!isValidPosition(row, col)) return;
        if(eatRange != null && eatRange.contains(new Coordinate(row, col))){
            AbstractPiece eatenPiece = this.getPieceAt(row, col);
            Step step1 = new Step(eatenPiece.getType(),eatenPiece.getRow(),eatenPiece.getCol(), -1, -1, 1);
            eatenPiece.setStatus(false);
            eatenPiece.moveTo(-1, -1);
            updateBoards(step1,true);
            Step step2 = new Step(selectedPiece.getType(),selectedPiece.getRow(),selectedPiece.getCol(), row, col, 0);
            updateBoards(step2,true);
            this.selectedPiece.moveTo(row, col);
            this.whoseTurn=!this.whoseTurn;
            setLastModTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            try{
                DBOperationBoard.updateBoardNowStatus(this.id, this.pieces);
                DBOperationBoard.updateBoardHistory(this.id,steps);
                DBOperationBoard.updateBoardDate(this.id, this.lastModTime);
            }catch(SQLException e){
                e.printStackTrace();
            }
            if(eatenPiece.getType()==7){
                this.boardType|=8;
                try{
                    DBOperationBoard.updateBoardType(this.id, boardType);
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void tryPlayBack(int StepIdx){
        this.pieces = new ArrayList<>();
        initPieces();initBoardStatus(pieces);
        for(int i = 0;i<=StepIdx;i++){
            Step nowStep = steps.get(i);
            updateBoards(nowStep,false);
            getPieceAt(nowStep.getFromRow(), nowStep.getFromCol()).moveTo(nowStep.getToRow(), nowStep.getToCol());
        }
    }

    public void resetBoard(){
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.boardType = (this.boardType&(1<<3))==0? this.boardType:(this.boardType^(1<<3));
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
        selectedPiece = null;
        boardStatus = new int[ROWS][COLS];
        this.moveRange=new ArrayList<>();
        this.eatRange = new ArrayList<>();
        try{
            DBOperationBoard.updateBoardById(id, this);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
