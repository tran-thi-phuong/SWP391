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
// Method to add lesson of subject for customer
public class Lesson_UserDAO extends DBContext {
    public boolean addLesson(int lessonId, int userId) {
        String sql = "INSERT INTO Lesson_User (LessonID, UserID, Status) VALUES (?, ?, 'Not Completed')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lessonId);
            stmt.setInt(2, userId);

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
}

