/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.*;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

/**
 *
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
                    user.setRole(rs.getString("Role"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhone(rs.getString("Phone"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setStatus(rs.getString("Status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    user.setRole(rs.getString("Role"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhone(rs.getString("Phone"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setStatus(rs.getString("Status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        UserDAO u = new UserDAO();
        u.updateProfile("Tuan", "tuan1", null, null, null, 26, "images/banner.png");
        
    }

}
