package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Question;

public class QuestionDAO extends DBContext {

    public void addQuestion(Question question) {
        String sql = "INSERT INTO Questions (LessonID, Status, Content, Level) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, question.getLessonID());
            stmt.setString(2, question.getStatus());
            stmt.setString(3, question.getContent());
            stmt.setString(4, question.getLevel());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM Questions";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Question question = new Question();
                question.setQuestionID(rs.getInt("QuestionID"));
                question.setLessonID(rs.getInt("LessonID"));
                question.setStatus(rs.getString("Status"));
                question.setContent(rs.getString("Content"));
                question.setLevel(rs.getString("Level"));
                questions.add(question);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return questions;
    }

    public List<Question> getQuestionsByLessonID(int lessonID) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM Questions WHERE LessonID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lessonID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestionID(rs.getInt("QuestionID"));
                    question.setLessonID(rs.getInt("LessonID"));
                    question.setStatus(rs.getString("Status"));
                    question.setContent(rs.getString("Content"));
                    question.setLevel(rs.getString("Level"));
                    questions.add(question);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return questions;
    }
    public List<Question> getQuestionsBySubjectID(int subjectID) {
    List<Question> questions = new ArrayList<>();
    String sql = "SELECT q.* FROM Questions q " +
                 "JOIN Lessons l ON q.LessonID = l.LessonID " +
                 "WHERE l.SubjectID = ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, subjectID);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Question question = new Question();
                question.setQuestionID(rs.getInt("QuestionID"));
                question.setLessonID(rs.getInt("LessonID"));
                question.setStatus(rs.getString("Status"));
                question.setContent(rs.getString("Content"));
                question.setLevel(rs.getString("Level"));
                questions.add(question);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return questions;
}


    public List<Question> getQuestionsByTestId(int testID) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.* FROM Questions q "
                + "JOIN Test_Question tq ON q.QuestionID = tq.QuestionID "
                + "WHERE tq.TestID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, testID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestionID(rs.getInt("QuestionID"));
                    question.setLessonID(rs.getInt("LessonID"));
                    question.setStatus(rs.getString("Status"));
                    question.setContent(rs.getString("Content"));
                    question.setLevel(rs.getString("Level"));
                    questions.add(question);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return questions;
    }
}
