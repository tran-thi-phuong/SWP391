/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author 84336
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.TestQuestion;

public class TestQuestionDAO extends DBContext {
    //add question to test
    public void addTestQuestion(TestQuestion testQuestion) {
        String sql = "INSERT INTO Test_Question (TestID, QuestionID) VALUES (?, ?)";
        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, testQuestion.getTestID());
            stmt.setInt(2, testQuestion.getQuestionID());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    //delete all question
    public void clearQuestionsByTestId(int testId) {
        String sql = "DELETE FROM Test_Question WHERE TestID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, testId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    //this test's question quantities
    public int countQuestionsByTestId(int testId) {
        String sql = "SELECT COUNT(*) FROM Test_Question WHERE TestID = ?";
        int count = 0;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, testId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1); // Get the count from the first column
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return count; // Return the count
    }
    //looking for test contain this question
    public List<Integer> getTestIDsByQuestionID(int questionID) {
        List<Integer> testIDs = new ArrayList<>();
        String sql = "SELECT TestID FROM Test_Question WHERE QuestionID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionID); // Set the questionID parameter
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                testIDs.add(rs.getInt("TestID")); // Retrieve and add each TestID to the list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return testIDs; // Return the list of TestIDs
    }
    //delete question from a test
    public void deleteTestQuestionsByQuestionID(int questionID) {
        String sql = "DELETE FROM Test_Question WHERE QuestionID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionID); // Set the questionID parameter
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully deleted " + rowsAffected + " entries with QuestionID: " + questionID);
            } else {
                System.out.println("No entries found with QuestionID: " + questionID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
