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
            + "    type INTEGER,\n"
            + "    description TEXT\n"
            + ")";
        
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }

        if(getUserByName("Red")==null){
            insertUser(new User(getUserCount(), "Red", null, 2, true));
        }
        if(getUserByName("Black")==null){
            insertUser(new User(getUserCount(), "Black", null, 2, true));
        }
        if(getUserByName("null")==null){
            insertUser(new User(getUserCount(), "null", null, 2, true));
        }
    }

    public static int insertUser(User user) throws SQLException {
        String sql = "INSERT INTO users(id, name, pswordhash, type, description) VALUES(?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPswordHash());
            ps.setInt(4, user.getType());
            ps.setString(5, user.getDescription());
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
        String sql = "UPDATE users SET name = ?, pswordhash = ?, type = ?, description = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUser.getName());
            ps.setString(2, newUser.getPswordHash());
            ps.setString(3, newUser.getDescription());
            ps.setInt(4, newUser.getType());
            ps.setInt(5, id);
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

    public static boolean updateUserType(int id, int type) throws SQLException {
        String sql = "UPDATE users SET type = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, type);
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
                        rs.getInt("type"),
                        rs.getString("description"),
                        true
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
                        rs.getInt("type"),
                        rs.getString("description"),
                        true
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public static User getUserInUse() throws SQLException {
        int tot = getUserCount();
        for (int i = 0; i < tot; i++) {
            User u = getUserById(i);
            if ((u.getType()&4)!=0) return u;
        }
        return null;
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
        if (password == null) return null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //用户功能
    public static void logoutAll() throws SQLException{
        int tot = getUserCount();
        for (int i = 0; i < tot; i++) {
            User u = getUserById(i);
            if ((u.getType()&4)!=0){
                u.setType((u.getType()^4));
                updateUserType(i, u.getType());
            }
        }
    }
}
