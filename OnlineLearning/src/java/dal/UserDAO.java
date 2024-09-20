/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import Model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    

}
