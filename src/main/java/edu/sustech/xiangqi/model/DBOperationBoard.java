package edu.sustech.xiangqi.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DBOperationBoard {
    private static final String URL = "jdbc:sqlite:src/main/java/edu/sustech/xiangqi/database/ChineseChess.db";

    public static void createTable() throws SQLException {
        createDatabaseFolder("src/main/java/edu/sustech/xiangqi/database");
        String sql = "CREATE TABLE IF NOT EXISTS boards (\n"
            + "    id INTEGER,\n"
            + "    name TEXT NOT NULL,\n"
            + "    date TEXT,\n"
            + "    nowstatus TEXT,\n"
            + "    history TEXT,\n"
            + "    boardtype INTEGER,\n"
            + "    description TEXT,\n"
            + "    userred TEXT,\n"
            + "    userblack TEXT,\n"
            + "    onwer TEXT,\n"
            + "    whoseturn BOOL\n"
            + ")";
        
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            /* System.out.println("表创建成功"); */
        }
    }

    public static boolean createDatabaseFolder(String path) {
        try {
            // 方法1: 使用java.nio.file (推荐)
            File directory = new File(path);
            if(directory.exists()) return true;
            Path directoryPath = Paths.get(path);
            Files.createDirectories(directoryPath);
            
            // 验证文件夹是否创建成功
            directory = new File(path);
            if (directory.exists() && directory.isDirectory()) {
                return true;
            } else {
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("创建文件夹时发生错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static int insertBoard(ChessBoardModel board) throws SQLException {
        String sql = "INSERT INTO boards(id, name, date, nowstatus, history, boardtype, description, userred, userblack, onwer, whoseturn) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, board.getId());
            ps.setString(2, board.getName());
            ps.setString(3, board.getLastModTime());
            ps.setString(4, piece2Str(board.getPieces()));
            ps.setString(5, steps2Str(board.getSteps()));
            ps.setInt(6, board.getType());
            ps.setString(7, board.getDescription());
            ps.setString(8, board.getUserRed().getName());
            ps.setString(9, board.getUserBlack().getName());
            ps.setString(10, board.getUserOwner().getName());
            ps.setBoolean(11, board.getWhoseTurn());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                return -1;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return -1;
                }
            }
        }
    }

    public static boolean deleteBoardById(int id2Del) throws SQLException {
        String sqlDelete = "DELETE FROM boards WHERE id = ?";
        String sqlShift = "UPDATE boards SET id = id - 1 WHERE id > ?";
        try (Connection conn = DriverManager.getConnection(URL)) {
            boolean prevAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                // 删除指定记录
                try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
                    psDelete.setInt(1, id2Del);
                    int affected = psDelete.executeUpdate();
                    if (affected == 0) {
                        conn.rollback();
                        return false;
                    }
                }
                // 将后续记录 id 全部 -1
                try (PreparedStatement psShift = conn.prepareStatement(sqlShift)) {
                    psShift.setInt(1, id2Del);
                    psShift.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(prevAuto);
            }
        }
    }

    public static void deleteBoardsOfNull() throws SQLException{
        int totBoard = getBoardCount();
        for(int i = 0;i<totBoard;i++){
            ChessBoardModel tmp = getBoardById(i);
            if(tmp.getUserOwner().getName().equals("null")){
                deleteBoardById(i);
                i--;totBoard--;
            }
        }
    }

    public static boolean updateBoardById(int id2Mod, ChessBoardModel newBoard) throws SQLException {
        String sql = "UPDATE boards SET name = ?, date = ?, nowstatus = ?, history = ?, boardtype = ?, description = ?, whoseturn = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newBoard.getName());
            ps.setString(2, newBoard.getLastModTime());
            ps.setString(3, piece2Str(newBoard.getPieces()));
            ps.setString(4, steps2Str(newBoard.getSteps()));
            ps.setInt(5, newBoard.getType());
            ps.setString(6, newBoard.getDescription());
            ps.setBoolean(7, newBoard.getWhoseTurn());
            ps.setInt(8, id2Mod);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    public static boolean updateBoardName(int id2Mod, String name) throws SQLException {
        String sql = "UPDATE boards SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateBoardDate(int id2Mod, String date) throws SQLException {
        String sql = "UPDATE boards SET date = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateBoardNowStatus(int id2Mod, List<AbstractPiece> nowstatus) throws SQLException {
        String sql = "UPDATE boards SET nowstatus = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, piece2Str(nowstatus));
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateBoardHistory(int id2Mod, List<Step> steps) throws SQLException {
        String sql = "UPDATE boards SET history = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, steps2Str(steps));
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateBoardType(int id2Mod, int boardtype) throws SQLException {
        String sql = "UPDATE boards SET boardtype = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, boardtype);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateBoardDescription(int id2Mod, String description) throws SQLException {
        String sql = "UPDATE boards SET description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateBoardWhoseTurn(int id2Mod, boolean whoseTurn) throws SQLException {
        String sql = "UPDATE boards SET whoseturn = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, whoseTurn);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public static ChessBoardModel getBoardById(int id2Get) throws SQLException {
        String sql = "SELECT * FROM boards WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id2Get);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChessBoardModel board = new ChessBoardModel(rs.getInt("id"), rs.getInt("boardtype"), DBOperationUser.getUserByName(rs.getString("userred")), DBOperationUser.getUserByName(rs.getString("userblack")),DBOperationUser.getUserByName(rs.getString("onwer")),rs.getBoolean("whoseturn"));
                    board.setName(rs.getString("name"));
                    board.setLastModTime(rs.getString("date"));
                    
                    // 将数据库中的字符串转换为棋子列表
                    String piecesStr = rs.getString("nowstatus");
                    List<AbstractPiece> pieces = str2Piece(piecesStr);
                    board.setPieces(pieces);
                    
                    // 将数据库中的字符串转换为状态
                    String stepsStr = rs.getString("history");
                    List<Step> steps = str2Steps(stepsStr);
                    board.setSteps(steps);
                    
                    board.setDescription(rs.getString("description"));
                    
                    return board;
                } else {
                    return null; // 没有找到对应的记录
                }
            }
        }
    }

    public static List<ChessBoardModel> getAllBoards() throws SQLException {
        List<ChessBoardModel> boards = new ArrayList<>();
        int totBoard = getBoardCount();
        for(int i = 0;i<totBoard;i++){
            boards.add(getBoardById(i));
        }
        return boards; 
    }

    public static List<ChessBoardModel> getBoardsByUser(User user) throws SQLException {
        List<ChessBoardModel> boards = new ArrayList<>();
        int totBoard = getBoardCount();
        for(int i = 0;i<totBoard;i++){
            ChessBoardModel tmp = getBoardById(i);
            if(tmp.getUserOwner().getName().equals(user.getName()))boards.add(tmp);
        }
        return boards; 
    }

    public static int getBoardCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM boards";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if(rs.next()) return rs.getInt("cnt");
            else return 0;
        }
    }

    public static String piece2Str(List<AbstractPiece> pieces){
        String s="";
        for(AbstractPiece i:pieces){
            s+=(i.getType()+" ");
            s+=(i.getRow()+" ");
            s+=(i.getCol()+" ");
            s+=(i.isRed()+" ");
            s+=(i.getStatus()+" ");
        }
        return s;
    }
    private static String steps2Str(List<Step> steps){
        String s="";
        for(Step step:steps){
            s+=(step.getPieceType()+" ");
            s+=(step.getFromRow()+" ");
            s+=(step.getFromCol()+" ");
            s+=(step.getToRow()+" ");
            s+=(step.getToCol()+" ");
            s+=(step.getMode()+" ");
        }
        return s;
    }
    // 需要添加的辅助方法，用于将字符串转换回棋子列表和状态
    private static List<AbstractPiece> str2Piece(String piecesStr) {
        Scanner in = new Scanner(piecesStr);
        List<AbstractPiece> pieces = new ArrayList<>();
        while(in.hasNext()){
            int type = in.nextInt();
            int row = in.nextInt();
            int col = in.nextInt();
            boolean isRed = in.nextBoolean();
            boolean status = in.nextBoolean();
            if(isRed){
                if(type==1)pieces.add(new RookPiece(type, row, col, isRed, status));
                else if(type==2)pieces.add(new HorsePiece(type, row, col, isRed, status));
                else if(type==3)pieces.add(new CannonPiece(type, row, col, isRed, status));
                else if(type==4)pieces.add(new ElephantPiece(type, row, col, isRed, status));
                else if(type==5)pieces.add(new AdvisorPiece(type, row, col, isRed, status));
                else if(type==6)pieces.add(new SoldierPiece(type, row, col, isRed, status));
                else if(type==7)pieces.add(new GeneralPiece(type, row, col, isRed, status));
            }else{
                if(type==1)pieces.add(new RookPiece(type, row, col, isRed, status));
                else if(type==2)pieces.add(new HorsePiece(type, row, col, isRed, status));
                else if(type==3)pieces.add(new CannonPiece(type, row, col, isRed, status));
                else if(type==4)pieces.add(new ElephantPiece(type, row, col, isRed, status));
                else if(type==5)pieces.add(new AdvisorPiece(type, row, col, isRed, status));
                else if(type==6)pieces.add(new SoldierPiece(type, row, col, isRed, status));
                else if(type==7)pieces.add(new GeneralPiece(type, row, col, isRed, status));
            }
        }
        in.close();
        return pieces;
    }

    private static List<Step> str2Steps(String stepsStr) {
        List<Step> steps = new ArrayList<>();
        Scanner in = new Scanner(stepsStr);
        while(in.hasNext()){
            int pieceType = in.nextInt();
            int fromRow = in.nextInt();
            int fromCol = in.nextInt();
            int toRow = in.nextInt();
            int toCol = in.nextInt();
            int mode = in.nextInt();
            steps.add(new Step(pieceType, fromRow, fromCol, toRow, toCol, mode));
        }
        in.close();
        return steps;
    }
}
