package edu.sustech.xiangqi.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardModel {
    // 储存棋盘上所有的棋子，要实现吃子的话，直接通过pieces.remove(被吃掉的棋子)删除就可以
    private List<AbstractPiece> pieces;
    private static final int ROWS = 10;
    private static final int COLS = 9;
    private String name;
    private String lastModTime;
    private Status status;
    private int id;
    private int boardType;
    //采用位运算叠加
    //xxx 仅棋盘1 远程2 AI4
    private int description;
    //预留，同boardType,位运算；

    public ChessBoardModel(int id, int boardType) {
        this.id=id;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        pieces = new ArrayList<>();
        initializePieces();
        this.status = new Status(pieces);
    }
    public ChessBoardModel(int id, int boardType, String name) {
        this.id=id;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        pieces = new ArrayList<>();
        initializePieces();
        this.status = new Status(pieces);
    }
    public ChessBoardModel(int id, int boardType, int description) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.lastModTime = this.name;
        pieces = new ArrayList<>();
        initializePieces();
        this.status = new Status(pieces);
    }
    public ChessBoardModel(int id, int boardType, String name, int description) {
        this.id=id;
        this.description = description;
        this.boardType = boardType;
        this.name = name;
        this.lastModTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        pieces = new ArrayList<>();
        initializePieces();
        this.status = new Status(pieces);
    }

    private void initializePieces() {
        // 黑方棋子
        pieces.add(new GeneralPiece(7, 0, 4, false));
        pieces.add(new SoldierPiece(6, 3, 0, false));
        pieces.add(new SoldierPiece(6, 3, 2, false));
        pieces.add(new SoldierPiece(6, 3, 4, false));
        pieces.add(new SoldierPiece(6, 3, 6, false));
        pieces.add(new SoldierPiece(6, 3, 8, false));
        pieces.add(new HorsePiece(2, 0, 6, false));
        pieces.add(new HorsePiece(2, 0, 2, false));


        // 红方棋子
        pieces.add(new GeneralPiece(7, 9, 4, true));
        pieces.add(new SoldierPiece(6, 6, 0, true));
        pieces.add(new SoldierPiece(6, 6, 2, true));
        pieces.add(new SoldierPiece(6, 6, 4, true));
        pieces.add(new SoldierPiece(6, 6, 6, true));
        pieces.add(new SoldierPiece(6, 6, 8, true));
        pieces.add(new HorsePiece(2, 9, 6, true));
        pieces.add(new HorsePiece(2, 9, 2, true));
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

        piece.moveTo(newRow, newCol);
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
    public Status getStatus(){
        return this.status;
    }
    public void setStatus(Status status){
        this.status=status;
    }
    public int getType(){
        return this.boardType;
    }
    public int getDescription(){
        return this.description;
    }
    public void setDescription(int description){
        this.description = description;
    }
}
