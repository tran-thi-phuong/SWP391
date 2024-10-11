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
import model.TestQuestion;

public class TestQuestionDAO extends DBContext {

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

    public void clearQuestionsByTestId(int testId) {
        String sql = "DELETE FROM Test_Question WHERE TestID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, testId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

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

}
