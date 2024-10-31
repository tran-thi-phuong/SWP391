/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Lesson;
import model.SubjectTopic;

/**
 *
 * @author tuant
 */
public class LessonDAO extends DBContext {

    public List<SubjectTopic> getAllLessonTopicBySubjectId(int subjectID) {
        List<SubjectTopic> list = new ArrayList<>();
        String sql = "select lt.Name, ls.TopicID, ls.SubjectID, ls.[Order] from Subject_LessonTopic ls join LessonTopic lt "
                + "on ls.TopicID = lt.TopicID where SubjectID = ? order by [Order]";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SubjectTopic lessonSubject = new SubjectTopic();
                    lessonSubject.setTopicName(rs.getString("Name"));
                    lessonSubject.setTopicID(rs.getInt("TopicID"));
                    lessonSubject.setSubjectID(rs.getInt("SubjectID"));
                    lessonSubject.setOrder(rs.getInt("Order"));
                    list.add(lessonSubject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Lesson> getAllLessonBySubjectId(int subjectID) {
        List<Lesson> list = new ArrayList<>();
        String sql = "select * from Lessons where SubjectID = ? order by TopicID, [Order]";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lesson lesson = new Lesson();
                    lesson.setLessonID(rs.getInt("LessonID"));
                    lesson.setSubjectID(rs.getInt("SubjectID"));
                    lesson.setTitle(rs.getString("Title"));
                    lesson.setTopicID(rs.getInt("TopicID"));
                    lesson.setContent(rs.getString("Content"));
                    lesson.setOrder(rs.getInt("Order"));
                    lesson.setDescription(rs.getString("Description"));
                    lesson.setStatus(rs.getString("Status"));
                    list.add(lesson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Lesson> searchLesson(int subjectID, String searchValue) {
        List<Lesson> list = new ArrayList<>();
        String sql = "select * from Lessons where SubjectID = ? and Title like ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectID);
            ps.setString(2, "%" + searchValue + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lesson lesson = new Lesson();
                    lesson.setLessonID(rs.getInt("LessonID"));
                    lesson.setSubjectID(rs.getInt("SubjectID"));
                    lesson.setTitle(rs.getString("Title"));
                    lesson.setTopicID(rs.getInt("TopicID"));
                    lesson.setContent(rs.getString("Content"));
                    lesson.setOrder(rs.getInt("Order"));
                    lesson.setDescription(rs.getString("Description"));
                    lesson.setStatus(rs.getString("Status"));
                    list.add(lesson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateLessonStatus(int lessonID, String status) {
        String sql = "update Lessons set Status = ? where LessonID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, lessonID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Lesson getLessonByLessonIDAndUserID(int lessonID, int userID) {
        Lesson lesson = null;
        String sql = """
                SELECT 
                    l.Title, 
                    l.Content, 
                    l.LessonID, 
                    l.Description, 
                    lm.Media_Link, 
                    lu.Status 
                FROM 
                    Lessons l 
                LEFT JOIN 
                    LessonMedia lm ON l.LessonID = lm.LessonID 
                LEFT JOIN 
                    Lesson_User lu ON l.LessonID = lu.LessonID 
                WHERE 
                    lu.UserID = ? 
                AND l.LessonID = ?""";


        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.setInt(2, lessonID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lesson = new Lesson();
                lesson.setTitle(rs.getString("Title"));
                lesson.setContent(rs.getString("Content"));
                lesson.setLessonID(rs.getInt("LessonID"));
                lesson.setMediaLink(rs.getString("Media_Link"));
                lesson.setDescription(rs.getString("Description"));
                lesson.setStatus(rs.getString("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesson;
    }

    public boolean updateLessonStatusToCompleted(int lessonID, int userID) {
        String sql = "UPDATE Lesson_User SET Status = 'Completed' WHERE LessonID = ? AND UserID = ? AND Status = 'Not Started'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, lessonID);
            ps.setInt(2, userID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //Get all lesson
    public List<Lesson> getAllLessons() {
        List<Lesson> list = new ArrayList<>();
        String sql = "SELECT * FROM Lessons";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Lesson lesson = new Lesson();
                lesson.setLessonID(rs.getInt("LessonID"));
                lesson.setSubjectID(rs.getInt("SubjectID"));
                lesson.setTitle(rs.getString("Title"));
                lesson.setTopicID(rs.getInt("TopicID"));
                lesson.setContent(rs.getString("Content"));
                lesson.setOrder(rs.getInt("Order"));
                lesson.setDescription(rs.getString("Description"));
                lesson.setStatus(rs.getString("Status"));
                list.add(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    //search by ID
    public Lesson getLessonById(int lessonID) {
        Lesson lesson = null;
        String sql = "SELECT * FROM Lessons WHERE LessonID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, lessonID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lesson = new Lesson();
                    lesson.setLessonID(rs.getInt("LessonID"));
                    lesson.setSubjectID(rs.getInt("SubjectID"));
                    lesson.setTitle(rs.getString("Title"));
                    lesson.setTopicID(rs.getInt("TopicID"));
                    lesson.setContent(rs.getString("Content"));
                    lesson.setOrder(rs.getInt("Order"));
                    lesson.setDescription(rs.getString("Description"));
                    lesson.setStatus(rs.getString("Status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    public Lesson getLessonByLessonID(int lessonID) {
        Lesson lesson = null;
        String sql = "select * from Lessons where LessonID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, lessonID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lesson = new Lesson();
                lesson.setTitle(rs.getString("Title"));
                lesson.setContent(rs.getString("Content"));
                lesson.setLessonID(rs.getInt("LessonID"));
                lesson.setDescription(rs.getString("Description"));
                lesson.setStatus(rs.getString("Status"));
                lesson.setOrder(rs.getInt("Order"));
                lesson.setSubjectID(rs.getInt("SubjectID"));
                lesson.setTopicID(rs.getInt("TopicID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesson;
    }

    public void addLesson(int subjectID, String title, int topicID, String content, int order, String description, String status) {
        String sql = "INSERT INTO Lessons (SubjectID, Title, TopicID, Content, [Order], Description, Status) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, subjectID);
            pstmt.setString(2, title);
            pstmt.setInt(3, topicID);
            pstmt.setString(4, content);
            pstmt.setInt(5, order);
            pstmt.setString(6, description);
            pstmt.setString(7, status);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateLesson(int lessonID, String title, int topicID, String content, int order, String description, String status) {
        String sql = "update Lessons set Title = ?, TopicID = ?, Content = ?, [Order] = ?, Description = ?, Status = ? where LessonID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, topicID);
            pstmt.setString(3, content);
            pstmt.setInt(4, order);
            pstmt.setString(5, description);
            pstmt.setString(6, status);
            pstmt.setInt(7, lessonID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean validLessonOrder(int subjectID, int topicID, int order){
        String sql = "select * from Lessons where SubjectID = ? and TopicID = ? and [Order] = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectID);
            ps.setInt(2, topicID);
            ps.setInt(3, order);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
        // Tạo một đối tượng DBContext để lấy kết nối
        // Tạo đối tượng DAO (giả định bạn đã có lớp DAO phù hợp)
        LessonDAO lessonDAO = new LessonDAO();
        System.out.println(lessonDAO.validLessonOrder(1, 1, 1));
    }
}
