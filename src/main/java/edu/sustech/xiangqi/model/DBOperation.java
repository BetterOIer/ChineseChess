package edu.sustech.xiangqi.model;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class DBOperation {
    private static final String URL = "jdbc:sqlite:src/main/java/edu/sustech/xiangqi/database/ChineseChess.db";

    public static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS boards (\n"
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "    name TEXT NOT NULL,\n"
            + "    date TEXT,\n"
            + "    nowstatus TEXT,\n"
            + "    history TEXT,\n"
            + "    boardtype INTEGER,\n"
            + "    description INTEGER\n"
            + ")";
        
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            /* System.out.println("表创建成功"); */
        }
    }
    public static int insertBoard(ChessBoardModel board) throws SQLException {
        String sql = "INSERT INTO boards(name, date, nowstatus, history, boardtype, description) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, board.getName());
            ps.setString(2, board.getLastModTime());
            ps.setString(3, piece2Str(board.getPieces()));
            ps.setString(4, status2Str(board.getStatus()));
            ps.setInt(5, board.getType());
            ps.setInt(6, board.getDescription());
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
        String sql = "DELETE FROM boards WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id2Del);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    public static boolean updateBoardById(int id2Mod, ChessBoardModel newBoard) throws SQLException {
        String sql = "UPDATE boards SET name = ?, date = ?, nowstatus = ?, history = ?, boardtype = ?, description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newBoard.getName());
            ps.setString(2, newBoard.getLastModTime());
            ps.setString(3, piece2Str(newBoard.getPieces()));
            ps.setString(4, status2Str(newBoard.getStatus()));
            ps.setInt(5, newBoard.getType());
            ps.setInt(6, newBoard.getDescription());
            ps.setInt(7, id2Mod);
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

    public static boolean updateBoardHistory(int id2Mod, Status status) throws SQLException {
        String sql = "UPDATE boards SET history = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status2Str(status));
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

    public static boolean updateBoardDescription(int id2Mod, int description) throws SQLException {
        String sql = "UPDATE boards SET description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, description);
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
                    ChessBoardModel board = new ChessBoardModel(rs.getInt("id"), rs.getInt("boardtype"));
                    board.setName(rs.getString("name"));
                    board.setLastModTime(rs.getString("date"));
                    
                    // 将数据库中的字符串转换为棋子列表
                    String piecesStr = rs.getString("nowstatus");
                    List<AbstractPiece> pieces = str2Piece(piecesStr);
                    board.setPieces(pieces);
                    
                    // 将数据库中的字符串转换为状态
                    String statusStr = rs.getString("history");
                    Status status = str2Status(statusStr, pieces);
                    board.setStatus(status);
                    
                    board.setDescription(rs.getInt("description"));
                    
                    return board;
                } else {
                    return null; // 没有找到对应的记录
                }
            }
        }
    }

    public static List<ChessBoardModel> getAllBoards(){
        List<ChessBoardModel> boards = new ArrayList<>();
        //TODO: write for loop.
        return boards; 
    }

    private static String piece2Str(List<AbstractPiece> pieces){
        String s="";
        return s;

        //TODO : write this part.
    }
    private static String status2Str(Status status){
        String s="";
        return s;

        //TODO : write this part.
    }
    // 需要添加的辅助方法，用于将字符串转换回棋子列表和状态
    private static List<AbstractPiece> str2Piece(String piecesStr) {
        List<AbstractPiece> pieces = new ArrayList<>();
        // TODO: 实现字符串到棋子列表的转换逻辑
        // 根据你在piece2Str方法中定义的格式来解析
        return pieces;
    }

    private static Status str2Status(String statusStr, List<AbstractPiece> pieces) {
        Status status = new Status(pieces);
        // TODO: 实现字符串到Status对象的转换逻辑
        // 根据你在status2Str方法中定义的格式来解析
        return status;
    }
}
