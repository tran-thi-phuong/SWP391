/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.CustomerCourse;
import model.Users;
import dal.*;


/**
 *
 * @author Admin
 */
// Method to add subject for customer after registration is approved
public class Customer_SubjectDAO extends DBContext{
    public boolean addCustomerSubject(int courseId, int userId) {
        String sql = "INSERT INTO Customer_Course (UserID, SubjectID, Progress) VALUES (?, ?, 0)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int countUsersByCourseID(int courseID) throws SQLException {
        String sql = "SELECT COUNT(*) AS UserCount "
                   + "FROM Customer_Course "
                   + "WHERE SubjectID = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, courseID); // Set the course ID as a parameter
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("UserCount"); // Return the count of users
                }
            }
        }
        return 0; // Return 0 if no users are found
    }

    /**
     * Calculates the average progress of all users for a given SubjectID.
     *
     * @param subjectID The ID of the subject.
     * @return The average progress, or 0 if no data is found.
     * @throws SQLException If a database error occurs.
     */
    public double calculateAverageProgress(int subjectID) throws SQLException {
        String sql = "SELECT AVG(Progress) AS AverageProgress FROM Customer_Course WHERE SubjectID = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, subjectID); // Set the SubjectID parameter
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("AverageProgress"); // Return the average progress
                }
            }
        }
        return 0.0; // Return 0 if no progress data is found for the SubjectID
    }
    
   public List<CustomerCourse> getCustomerProgressBySubjectID(int subjectID) throws SQLException {
        List<CustomerCourse> customerCourseList = new ArrayList<>();
        UserDAO userDAO = new UserDAO();

        // SQL query to join Customer_Course and Users tables (no need to join here as we will fetch user details later)
        String sql = "SELECT userID, progress FROM Customer_Course WHERE SubjectID = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, subjectID);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int userID = rs.getInt("userID");
                    int progress = rs.getInt("progress");

                    // Get user details using the UserDAO
                    Users user = userDAO.getUserById(userID);

                    // Create a CustomerCourse object and populate it with the data
                    CustomerCourse customerCourse = new CustomerCourse();
                    customerCourse.setUserID(userID);
                    customerCourse.setUserName(user.getUsername());
                    customerCourse.setEmail(user.getEmail());
                    customerCourse.setPhone(user.getPhone());
                    customerCourse.setProgress(progress);

                    customerCourseList.add(customerCourse);
                }
            }
        }

        return customerCourseList;
    }
    // Method to count the number of users following a specific SubjectID
    public int countUsersBySubjectID(int subjectID) {
        int userCount = 0;
        String query = "SELECT COUNT(DISTINCT UserID) AS userCount FROM Customer_Course WHERE SubjectID = ?";

        try (
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, subjectID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userCount = resultSet.getInt("userCount");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userCount;
    }
}


