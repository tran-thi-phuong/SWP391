package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//for model
import model.Question;

public class QuestionDAO extends DBContext {

    //method to add Question to database
    public void updateQuestion(int questionId, Question question) {
        String sql = "UPDATE Questions SET LessonID = ?, Status = ?, Content = ?, Level = ? WHERE QuestionID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, question.getLessonID());
            stmt.setString(2, question.getStatus());
            stmt.setString(3, question.getContent());
            stmt.setString(4, question.getLevel());
            stmt.setInt(5, questionId); // Use the passed questionId
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //method to get all question in database
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

    //method to get a list of question by lesson
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

    //method to get a list of question by subject
    public List<Question> getQuestionsBySubjectID(int subjectID) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.* FROM Questions q "
                + "JOIN Lessons l ON q.LessonID = l.LessonID "
                + "WHERE l.SubjectID = ?";

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

    //method to get a list of question by test
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

    //method to get a list of question by test
    public Question getQuestionById(int questionId) {
        Question question = null;
        String sql = "SELECT QuestionID, LessonID, Status, Content, Level FROM Questions WHERE QuestionID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                question = new Question();
                question.setQuestionID(rs.getInt("QuestionID"));
                question.setLessonID(rs.getInt("LessonID"));
                question.setStatus(rs.getString("Status"));
                question.setContent(rs.getString("Content"));
                question.setLevel(rs.getString("Level"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return question;
    }
    //delete question
    public boolean deleteQuestionById(int questionId) {
        String sql = "DELETE FROM Questions WHERE QuestionID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if a row was deleted
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Return false in case of an error
        }
    }
    //search and order for question List
    public List<Question> getAllQuestions(int pageNumber, int pageSize, String search, String lessonID, String status, String level, String orderBy, String direction) {
        List<Question> questions = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;

        // Base SQL query
        StringBuilder sql = new StringBuilder("SELECT * FROM Questions WHERE 1=1"); // Start with a basic query

        // Build query conditions based on provided parameters
        if (search != null && !search.isEmpty()) {
            sql.append(" AND Content LIKE ?");
        }
        if (lessonID != null && !lessonID.isEmpty()) {
            sql.append(" AND LessonID = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
        }
        if (level != null && !level.isEmpty()) {
            sql.append(" AND Level = ?");
        }

        // Add pagination using OFFSET and FETCH NEXT
        // Assuming orderBy and direction are already set based on user input
        sql.append(" ORDER BY ").append(orderBy).append(" ").append(direction).append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Set parameters for the query
            if (search != null && !search.isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%"); // Use LIKE for searching
            }
            if (lessonID != null && !lessonID.isEmpty()) {
                stmt.setString(paramIndex++, lessonID); // Set lesson ID
            }
            if (status != null && !status.isEmpty()) {
                stmt.setString(paramIndex++, status); // Set status
            }
            if (level != null && !level.isEmpty()) {
                stmt.setString(paramIndex++, level); // Set level
            }

            // Set pagination parameters
            stmt.setInt(paramIndex++, offset); // Offset for pagination
            stmt.setInt(paramIndex++, pageSize); // Number of items per page

            try (ResultSet rs = stmt.executeQuery()) {
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
            ex.printStackTrace(); // Handle exception appropriately
        }
        return questions; // Return the list of questions
    }
    //count question for pagination
    public int getTotalQuestionCount(String search, String lessonId, String status, String level) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Questions WHERE 1=1");

        // Build query conditions based on provided parameters
        if (search != null && !search.isEmpty()) {
            sql.append(" AND Content LIKE ?");
        }
        if (lessonId != null && !lessonId.isEmpty()) {
            sql.append(" AND LessonID = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
        }
        if (level != null && !level.isEmpty()) {
            sql.append(" AND Level = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Set parameters for the query
            if (search != null && !search.isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%"); // Use LIKE for searching
            }
            if (lessonId != null && !lessonId.isEmpty()) {
                stmt.setString(paramIndex++, lessonId); // Set lesson ID as a String
            }
            if (status != null && !status.isEmpty()) {
                stmt.setString(paramIndex++, status); // Set status
            }
            if (level != null && !level.isEmpty()) {
                stmt.setString(paramIndex++, level); // Set level
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the count
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle exception appropriately
        }
        return 0; // Return 0 if an error occurs
    }
    //add question
    public int addQuestion(Question question) {
        String sql = "INSERT INTO Questions (LessonID, Status, Content, Level) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, question.getLessonID());
            stmt.setString(2, question.getStatus());
            stmt.setString(3, question.getContent());
            stmt.setString(4, question.getLevel());

            // Execute the insert and obtain the generated keys
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1); // Get the generated ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId; // Return the generated question ID
    }
}
