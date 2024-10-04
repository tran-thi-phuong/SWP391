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
import model.Answer;
public class AnswerDAO extends DBContext {

    public void addAnswer(Answer answer) {
        String sql = "INSERT INTO Answers (QuestionID, Content, isCorrect) VALUES (?, ?, ?)";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, answer.getQuestionID());
            stmt.setString(2, answer.getContent());
            stmt.setBoolean(3, answer.isCorrect());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Answer> getAnswersByQuestionId(int questionID) {
        List<Answer> answers = new ArrayList<>();
        String sql = "SELECT * FROM Answers WHERE QuestionID = ?";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Answer answer = new Answer();
                    answer.setAnswerID(rs.getInt("AnswerID"));
                    answer.setQuestionID(rs.getInt("QuestionID"));
                    answer.setContent(rs.getString("Content"));
                    answer.setCorrect(rs.getBoolean("isCorrect"));
                    answers.add(answer);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return answers;
    }
}
