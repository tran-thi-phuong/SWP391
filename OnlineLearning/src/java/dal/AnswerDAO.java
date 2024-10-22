package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Answer;

public class AnswerDAO extends DBContext {

    public void addAnswer(Answer answer) {
        String sql = "INSERT INTO Answers (QuestionID, Content, Explanation, isCorrect) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, answer.getQuestionID());
            stmt.setString(2, answer.getContent());
            stmt.setString(3, answer.getExplanation());
            stmt.setBoolean(4, answer.isCorrect());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Answer> getAnswersByQuestionId(int questionID) {
        List<Answer> answers = new ArrayList<>();
        String sql = "SELECT * FROM Answers WHERE QuestionID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Answer answer = new Answer(rs.getInt("QuestionID"), 
                                                rs.getString("Content"), 
                                                rs.getString("Explanation"), 
                                                rs.getBoolean("isCorrect"));
                    answer.setAnswerID(rs.getInt("AnswerID"));
                    answers.add(answer);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return answers;
    }
}
