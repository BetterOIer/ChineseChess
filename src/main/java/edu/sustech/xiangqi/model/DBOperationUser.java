package edu.sustech.xiangqi.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBOperationUser {
    private static final String URL = "jdbc:sqlite:src/main/java/edu/sustech/xiangqi/database/ChineseChess.db";

    public static void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + "    id INTEGER,\n"
            + "    name TEXT NOT NULL,\n"
            + "    pswordhash TEXT,\n"
            + "    description TEXT\n"
            + ")";
        
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }

        if(getUserByName("Red")==null){
            insertUser(new User(getUserCount(), "Red", null));
        }
        if(getUserByName("Black")==null){
            insertUser(new User(getUserCount(), "Black", null));
        }
    }

    public static int insertUser(User user) throws SQLException {
        String sql = "INSERT INTO users(id, name, pswordhash, description) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPswordHash());
            ps.setString(4, user.getDescription());
            int affected = ps.executeUpdate();
            return affected > 0 ? user.getId() : -1;
        }
    }

    public static boolean deleteUserById(int id) throws SQLException {//!暂时别用这个！！
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
    
    public static boolean updateUserById(int id, User newUser) throws SQLException {
        String sql = "UPDATE users SET name = ?, pswordhash = ?, description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUser.getName());
            ps.setString(2, newUser.getPswordHash());
            ps.setString(3, newUser.getDescription());
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateUserName(int id, String name) throws SQLException {
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateUserPasswordHash(int id, String hash) throws SQLException {
        String sql = "UPDATE users SET pswordhash = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static boolean updateUserDescription(int id, String description) throws SQLException {
        String sql = "UPDATE users SET description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public static User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("pswordhash"),
                        rs.getString("description")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public static User getUserByName(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("pswordhash"),
                        rs.getString("description")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        int tot = getUserCount();
        for (int i = 0; i < tot; i++) {
            User u = getUserById(i);
            if (u != null) users.add(u);
        }
        return users;
    }

    // 用户数量
    public static int getUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM users";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("cnt");
            else return 0;
        }
    }

    public static String calHash(String password){
        String hash=password;
        return hash;
    }
}
