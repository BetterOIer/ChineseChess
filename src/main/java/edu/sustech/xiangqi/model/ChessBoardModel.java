package edu.sustech.xiangqi.model;
import edu.sustech.xiangqi.ui.SoundPlayer;

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
    private boolean playBackOn= false;
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
    private int selectedIdx;
    private int[][] boardStatus = new int[ROWS][COLS];

    //用户信息
    private User userRed;
    private User userBlack;
    private User userOwner;
    private boolean whoseTurn;

    //游戏结果
    private String gameResult;

    // 游戏状态
    private enum GameState {
        PLAYING, // 游戏进行中
        RED_WIN, // 红方胜利
        BLACK_WIN, // 黑方胜利
        DRAW // 和棋
    }
    private GameState gameState = GameState.PLAYING;

    @Override
    public String toString(){
        return name+" "+boardType+" "+description+" "+userRed+" "+userBlack+" "+whoseTurn;
    }

    //构造函数
    public ChessBoardModel(int id, int boardType, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
        this.playBackOn=false;
        this.selectedIdx = getTrueSteps().size()-1;
        this.gameResult = null;
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
        this.playBackOn=false;
        this.selectedIdx = getTrueSteps().size()-1;
    }
    public ChessBoardModel(int id, int boardType, String description, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = new ArrayList<>();
        initPieces();
        this.steps = new ArrayList<>();
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
        this.playBackOn=false;
        this.selectedIdx = getTrueSteps().size()-1;
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
        this.playBackOn=false;
        this.selectedIdx = getTrueSteps().size()-1;
    }
    public ChessBoardModel(int id, int boardType, List<AbstractPiece> pieces, List<Step> steps, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
        this.playBackOn=false;
        this.selectedIdx = getTrueSteps().size()-1;
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
        this.playBackOn=false;
        this.selectedIdx = getTrueSteps().size()-1;
    }
    public ChessBoardModel(int id, int boardType, String description, List<AbstractPiece> pieces, List<Step> steps, User userRed, User userBlack, User owner, boolean whoseTurn) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.pieces = pieces;
        this.steps = steps;
        initBoardStatus(pieces);
        this.userRed=userRed;
        this.userBlack=userBlack;
        this.userOwner=owner;
        this.whoseTurn = whoseTurn;
        this.playBackOn=false;
        this.selectedIdx = getTrueSteps().size()-1;
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
        this.playBackOn=false;
        this.selectedIdx = getTrueSteps().size()-1;
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
    public boolean getPlayBackOn(){
        return this.playBackOn;
    }
    public void setPlayBackOn(boolean playBackOn){
        this.playBackOn=playBackOn;
    }

    //历史
    public List<Step> getAllSteps(){
        return steps;
    }
    public List<Step> getTrueSteps(){
        List<Step> trueSteps = new ArrayList<>();
        for(Step step:steps){
            if(step.getMode()==1)continue;
            trueSteps.add(step);
        }
        return trueSteps;
    }
    public void setSteps(List<Step> steps){
        this.steps = steps;
    }
    public int getSelectedIdx(){
        return this.selectedIdx;
    }
    public void setSelectedIdx(int selectedIdx){
        this.selectedIdx=selectedIdx;
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
                if(selectedPiece.canMove(this, i, j) && !willCauseFacing(selectedPiece, i, j)) moveRange.add(new Coordinate(i, j));
                if(selectedPiece.canEat(this, i,j) && !willCauseFacing(selectedPiece, i, j)) eatRange.add(new Coordinate(i, j));
            }
        }
        /* System.out.println("Can move:"+this.moveRange);
        System.out.println("Can eat:"+this.eatRange); */
    }
    public void cancelSelection(){
        this.selectedPiece = null;
    }
    public boolean tryMovePiece(int row, int col){
        if(this.selectedPiece==null) return false;
        if(!isValidPosition(row, col)) return false;
        if(moveRange != null && moveRange.contains(new Coordinate(row, col))){
            Step nowStep = new Step(selectedPiece.getType(),selectedPiece.isRed(),selectedPiece.getRow(),selectedPiece.getCol(), row, col, 0);
            updateBoards(nowStep,true);
            this.selectedPiece.moveTo(row, col);
            this.whoseTurn=!this.whoseTurn;
            setLastModTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            try{
                DBOperationBoard.updateBoardNowStatus(this.id, this.pieces);
                DBOperationBoard.updateBoardHistory(this.id,steps);
                DBOperationBoard.updateBoardDate(this.id, this.lastModTime);
                DBOperationBoard.updateBoardWhoseTurn(this.id, whoseTurn);
            }catch(SQLException e){
                e.printStackTrace();
            }

            SoundPlayer.playMoveSound();

            return true;
        }
        return false;
    }
    
    public boolean tryEatPiece(int row, int col){
        if(this.selectedPiece==null) return false;
        if(!isValidPosition(row, col)) return false;
        if(eatRange != null && eatRange.contains(new Coordinate(row, col))){
            AbstractPiece eatenPiece = this.getPieceAt(row, col);
            Step step1 = new Step(eatenPiece.getType(),eatenPiece.isRed(),eatenPiece.getRow(),eatenPiece.getCol(), -1, -1, 1);
            eatenPiece.setStatus(false);
            eatenPiece.moveTo(-1, -1);
            updateBoards(step1,true);
            Step step2 = new Step(selectedPiece.getType(),selectedPiece.isRed(),selectedPiece.getRow(),selectedPiece.getCol(), row, col, 0);
            updateBoards(step2,true);
            this.selectedPiece.moveTo(row, col);
            this.whoseTurn=!this.whoseTurn;
            setLastModTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            try{
                DBOperationBoard.updateBoardNowStatus(this.id, this.pieces);
                DBOperationBoard.updateBoardHistory(this.id,steps);
                DBOperationBoard.updateBoardDate(this.id, this.lastModTime);
                DBOperationBoard.updateBoardWhoseTurn(this.id, whoseTurn);
            }catch(SQLException e){
                e.printStackTrace();
            }
            if(eatenPiece.getType() == 7){
                this.boardType |= 8;

                // 设置 gameState
                if (eatenPiece.isRed()) {
                    this.gameState = GameState.BLACK_WIN;  // 红方将被吃掉，黑方胜利
                } else {
                    this.gameState = GameState.RED_WIN;    // 黑方将被吃掉，红方胜利
                }

                try{
                    DBOperationBoard.updateBoardType(this.id, boardType);
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

            SoundPlayer.playMoveSound();

            return true;
        }
        return false;
    }

    public void tryPlayBack(int StepIdx){
        this.pieces = new ArrayList<>();
        initPieces();initBoardStatus(pieces);
        List<Step> trueSteps = getTrueSteps();
        for(int i = 0,j=0;i<=StepIdx;i++,j++){
            Step nowStep = trueSteps.get(i);
            if(nowStep.getMode()!=steps.get(j).getMode()){
                Step eatStep = steps.get(j);
                getPieceAt(eatStep.getFromRow(), eatStep.getFromCol()).setStatus(false);
                updateBoards(eatStep, false);
                getPieceAt(eatStep.getFromRow(), eatStep.getFromCol()).moveTo(eatStep.getToRow(), eatStep.getToCol());
                j++;
            }
            updateBoards(nowStep,false);
            getPieceAt(nowStep.getFromRow(), nowStep.getFromCol()).moveTo(nowStep.getToRow(), nowStep.getToCol());
        }
    }

    public void resetBoard(){
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.playBackOn=false;
        this.whoseTurn=!this.whoseTurn;
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

    private boolean isGeneralsFacing() {
        // 1. 找到红方帅和黑方将的位置
        AbstractPiece redGeneral = null;
        AbstractPiece blackGeneral = null;
        for (AbstractPiece piece : pieces) {
            if (piece.getType() == 7) {
                if (piece.isRed()) {
                    redGeneral = piece;
                } else {
                    blackGeneral = piece;
                }
            }
        }

        // 2. 检查是否在同一列
        int redCol = redGeneral.getCol();
        int blackCol = blackGeneral.getCol();
        if (redCol != blackCol) {
            return false; // 不在同一列，不违规
        }

        // 3. 检查同一列中间是否有棋子遮挡
        int redRow = redGeneral.getRow();
        int blackRow = blackGeneral.getRow();
        // 确定行数范围
        int minRow = Math.min(redRow, blackRow);
        int maxRow = Math.max(redRow, blackRow);

        // 遍历中间行，若有棋子则不违规
        for (int row = minRow + 1; row < maxRow; row++) {
            if (getPieceAt(row, redCol) != null) {
                return false; // 有棋子遮挡，不违规
            }
        }

        // 4. 同一列且中间无遮挡 → 违规
        return true;
    }
    private boolean willCauseFacing(AbstractPiece piece, int targetRow, int targetCol) {
        // 棋子原始位置
        int originalRow = piece.getRow();
        int originalCol = piece.getCol();

        // 模拟移动：将棋子临时移到目标位置
        piece.moveTo(targetRow, targetCol);

        // 检查移动后是否出现将帅对面
        boolean isFacing = isGeneralsFacing();

        // 恢复棋子原始位置
        piece.moveTo(originalRow, originalCol);

        return isFacing;
    }
}
