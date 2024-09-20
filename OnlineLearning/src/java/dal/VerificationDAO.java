/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.*;

/**
 *
 * @author tuant
 */
public class VerificationDAO extends DBContext{
    public Verification checkCode(int userID, String code) {
        Verification veri = null;
        String sql = "SELECT * FROM VerificationCode WHERE UserID = ? and Code = ?";               
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userID);
            st.setString(2, code);
           

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    veri = new Verification();
                    veri.setUserID(rs.getInt("UserID"));
                    veri.setCode(rs.getString("Code"));
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veri;
    }
    public void generateCode(int userID, String code) {
        try {
            String sql = "insert into VerificationCode(UserID, Code) values(?, ?)";
            PreparedStatement st = connection.prepareStatement(sql);

            st.setInt(1, userID);
            st.setString(2, code);
            
            st.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteCode(int userID) {
        try {
            String sql = "delete from VerificationCode where UserID = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userID);             
            st.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void activeUser(int userID) {
        try {
            String sql = "update Users set Status = 'Active' where UserID = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userID);             
            st.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        VerificationDAO veri = new VerificationDAO();
        veri.activeUser(22);
        System.out.println(veri.checkCode(1, "203123"));
    }
}
