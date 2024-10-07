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
}

