/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.sql.ResultSet;

/**
 *
 * @author Admin
 */
// Method to add lesson of subject for customer
public class Lesson_UserDAO extends DBContext {
    public boolean addLesson(int lessonId, int userId, Date deadline) {
        String sql = "INSERT INTO Lesson_User (LessonID, UserID, Status, Deadline) VALUES (?, ?, 'Not Completed',?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lessonId);
            stmt.setInt(2, userId);
            stmt.setDate(3, (java.sql.Date) deadline);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Debug: Successfully added LessonID = " + lessonId + ", UserID = " + userId);
                return true;
            } else {
                System.out.println("Debug: No rows inserted for LessonID = " + lessonId + ", UserID = " + userId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error: Failed to add LessonID = " + lessonId + ", UserID = " + userId);
            e.printStackTrace();
            return false;
        }
    }
    
    public Date getDeadline(int lessonID, int userID) throws SQLException {
    String query = "SELECT Deadline FROM Lesson_User WHERE LessonID = ? AND UserID = ? AND Status = 'Not Completed'";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, lessonID);
        stmt.setInt(2, userID);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDate("Deadline"); // Return the deadline
            }
        }
    }
    return null; // No deadline found or the status is not 'Not Completed'
}



    
}

