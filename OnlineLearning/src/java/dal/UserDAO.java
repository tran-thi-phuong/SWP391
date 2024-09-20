/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import Model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.*;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class UserDAO extends DBContext {

    public User getUserByUsername(String username) {
        User user = null;
        try {
            String sql = "SELECT * FROM Users WHERE TRIM(Username) = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setEmail(rs.getString("Email"));
                user.setAddress(rs.getString("Address"));
                user.setAvatar(rs.getString("Avatar"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                user.setToken(rs.getString("Token"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByLogin(String login) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE Username = ? OR Email = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set parameters for the SQL query
            ps.setString(1, login);
            ps.setString(2, login);

            try (ResultSet rs = ps.executeQuery()) {
                // Process the result set
                if (rs.next()) {
                    user = new User();
 * @author DELL
 */
public class UserDAO extends DBContext {

    public Users getUser(String username, String password) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE (Username = ? OR Email = ?)"
                + "AND Password = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, username);
            st.setString(3, password);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setName(rs.getString("Name"));
                    user.setGender(rs.getString("Gender"));
                    user.setPhone(rs.getString("Phone"));
                    user.setEmail(rs.getString("Email"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(rs.getString("Status"));
                    user.setToken(rs.getString("Token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return user;
    }

    public boolean updateUserPassword(User user) {
        String sql = "UPDATE Users SET Password = ? WHERE Username = ?";
        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Update Members table
            try (PreparedStatement statementMembers = connection.prepareStatement(sql)) {
                statementMembers.setString(1, user.getPassword());
                statementMembers.setString(2, user.getUsername());
                statementMembers.executeUpdate();
            }
            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Reset auto-commit mode to its default state
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateUserStatus(User user) {
        String sql = "UPDATE Users SET Status = ? WHERE Username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getStatus());
            ps.setString(2, user.getUsername());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE Email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Chỉ cần lấy một bản ghi
                    user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setName(rs.getString("Name"));
                    user.setGender(rs.getString("Gender"));
                    user.setPhone(rs.getString("Phone"));
                    user.setEmail(rs.getString("Email"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(rs.getString("Status"));
                    user.setToken(rs.getString("Token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public void updateResetToken(String email, String token) {
        String sql = "UPDATE Users SET Token = ? WHERE Email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByToken(String token) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE Token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
        return user;
    }

    public int register(String fullname, String username, String email, String password, String phone, String address, String gender, String role, String status) {
        int userId = -1;

        String sql = "INSERT INTO Users (Name, Username, Email, Password, Phone, Address, Gender, Role, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            st.setString(1, fullname);
            st.setString(2, username);
            st.setString(3, email);
            st.setString(4, password);
            if (phone != null && !phone.isEmpty()) {
                st.setString(5, phone);
            } else {
                st.setNull(5, java.sql.Types.VARCHAR);
            }

            if (address != null && !address.isEmpty()) {
                st.setString(6, address);
            } else {
                st.setNull(6, java.sql.Types.VARCHAR);
            }

            if (gender != null && !gender.isEmpty()) {
                st.setString(7, gender);
            } else {
                st.setNull(7, java.sql.Types.VARCHAR);
            }

            st.setString(8, role);
            st.setString(9, status);

            int affectedRows = st.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1); // Retrieve the generated ID
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } else {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userId; // Return the generated ID
    }

    public Users getUserByInfo(String info, String content) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE " + info + " = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, content);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setName(rs.getString("Name"));
                    user.setGender(rs.getString("Gender"));
                    user.setPhone(rs.getString("Phone"));
                    user.setEmail(rs.getString("Email"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(rs.getString("Status"));
                    user.setToken(rs.getString("Token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Return the user object or null if not found
    }

    public void updatePassword(String token, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE Token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword); // Nên băm mật khẩu trước khi lưu
            ps.setString(2, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearResetToken(String token) {
        String sql = "UPDATE Users SET Token = NULL WHERE Token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        return user;
    }

    public void updateProfile(String name, String username, String phone, String address, String gender, int userID, String avatar) {
        try {
            String sql = "update Users set Name = ?, Username= ?, Phone = ?, Address = ?, Gender = ?, Avatar = ? "
                    + "where UserID = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, name);
            st.setString(2, username);
            if (phone != null && !phone.isEmpty()) {
                st.setString(3, phone);
            } else {
                st.setNull(3, java.sql.Types.VARCHAR);
            }
            if (address != null && !address.isEmpty()) {
                st.setString(4, address);
            } else {
                st.setNull(4, java.sql.Types.VARCHAR);
            }
            if (gender != null && !gender.isEmpty()) {
                st.setString(5, gender);
            } else {
                st.setNull(5, java.sql.Types.VARCHAR);
            }
            
            if (avatar != null && !avatar.isEmpty()) {
            st.setString(6, avatar);
        } else {
            st.setNull(6, java.sql.Types.VARCHAR);
        }
            st.setInt(7, userID);
            st.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // Token to test
        String testToken = "yvWo5jzXKOk"; // Ensure this token exists in the database

        // New password to set
        String newPassword = "Phuong1274@"; // Use a hashed password in a real scenario

        // Update the password
        userDAO.updatePassword(testToken, newPassword);

        // Verify the update
        User user = userDAO.getUserByToken(testToken);
        if (user != null) {
            System.out.println("Password updated successfully.");
            System.out.println("Updated Password: " + user.getPassword()); // This should show the new password
        } else {
            System.out.println("User not found with the given token.");
        }

    }
    
        UserDAO u = new UserDAO();
        u.updateProfile("Tuan", "tuan1", null, null, null, 26, "images/banner.png");
        
    }

}
