package edu.sustech.xiangqi.model;

import java.sql.*;

public class DBOperation {
    private static final String URL = "jdbc:sqlite:src/main/java/edu/sustech/xiangqi/database/db.db";

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS boards (\n"
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "    name TEXT NOT NULL,\n"
            + "    data TEXT,\n"
            + "    nowstatus TEXT,\n"
            + "    history TEXT,\n"
            + "    boardtype INTEGER,\n"
            + "    description TEXT\n"
            + ")";
        
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("表创建成功");
        }
    }
    public int insertBoard(String name, String data, String nowstatus, String history, int boardtype, String description) throws SQLException {
        String sql = "INSERT INTO boards(name, data, nowstatus, history, boardtype, description) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, data);
            ps.setString(3, nowstatus);
            ps.setString(4, history);
            ps.setInt(5, boardtype);
            ps.setString(6, description);
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

    public boolean deleteBoardById(int id2Del) throws SQLException {
        String sql = "DELETE FROM boards WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id2Del);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    public boolean updateBoardById(int id2Mod, String name, String data, String nowstatus, String history, int boardtype, String description) throws SQLException {
        String sql = "UPDATE boards SET name = ?, data = ?, nowstatus = ?, history = ?, boardtype = ?, description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, data);
            ps.setString(3, nowstatus);
            ps.setString(4, history);
            ps.setInt(5, boardtype);
            ps.setString(6, description);
            ps.setInt(7, id2Mod);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    public boolean updateBoardName(int id2Mod, String name) throws SQLException {
        String sql = "UPDATE boards SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateBoardData(int id2Mod, String data) throws SQLException {
        String sql = "UPDATE boards SET data = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateBoardNowStatus(int id2Mod, String nowstatus) throws SQLException {
        String sql = "UPDATE boards SET nowstatus = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nowstatus);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateBoardHistory(int id2Mod, String history) throws SQLException {
        String sql = "UPDATE boards SET history = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, history);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateBoardType(int id2Mod, int boardtype) throws SQLException {
        String sql = "UPDATE boards SET boardtype = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, boardtype);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateBoardDescription(int id2Mod, String description) throws SQLException {
        String sql = "UPDATE boards SET description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);
            ps.setInt(2, id2Mod);
            return ps.executeUpdate() > 0;
        }
    }
}
