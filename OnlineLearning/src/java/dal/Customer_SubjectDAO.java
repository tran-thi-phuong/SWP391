/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
// Method to add subject for customer after registration is approved
public class Customer_SubjectDAO extends DBContext{
    public boolean addCustomerSubject(int courseId, int userId) {
        String sql = "INSERT INTO Customer_Subject (UserID, SubjectID, Progress) VALUES (?, ?, 0)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
