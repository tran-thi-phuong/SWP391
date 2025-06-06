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

    // get all topic of the subject by id
    public List<SubjectTopic> getAllLessonTopicBySubjectId(int subjectID) {
        List<SubjectTopic> list = new ArrayList<>();
        String sql = "select lt.Name, ls.TopicID, ls.SubjectID, ls.[Order] from Course_LessonTopic ls join LessonTopic lt "
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

    // get all lesson of subject by id
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
                    lesson.setDuration(rs.getInt("Duration"));
                    list.add(lesson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Lesson> getLessonBySubjectId(int subjectID) {
        List<Lesson> list = new ArrayList<>();
        String sql = "select * from Lessons where SubjectID = ? ";
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
                    lesson.setDuration(rs.getInt("Duration"));
                    list.add(lesson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // func for searching lesson by name
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
                    lesson.setDuration(rs.getInt("Duration"));
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
                    lu.Status 
                FROM 
                    Lessons l 
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
                lesson.setDescription(rs.getString("Description"));
                lesson.setStatus(rs.getString("Status"));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesson;
    }

    public boolean updateLessonStatusToCompleted(int lessonID, int userID) {
        String sql = "UPDATE Lesson_User SET Status = 'Completed' WHERE LessonID = ? AND UserID = ? AND Status = 'Not Completed'";
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
                lesson.setDuration(rs.getInt("Duration"));
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
                    lesson.setDuration(rs.getInt("Duration"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lesson;
    }

    // get lesson information by id
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
                lesson.setDuration(rs.getInt("Duration"));
                lesson.setDuration(rs.getInt("Duration"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesson;
    }

    // func for adding a new lesson
    public void addLesson(int subjectID, String title, int topicID, String content, int order, String description, String status, int duration) {
        String sql = "INSERT INTO Lessons (SubjectID, Title, TopicID, Content, [Order], Description, Status, Duration) "
                + "VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, subjectID);
            pstmt.setString(2, title);
            pstmt.setInt(3, topicID);
            pstmt.setString(4, content);
            pstmt.setInt(5, order);
            pstmt.setString(6, description);
            pstmt.setString(7, status);
            pstmt.setInt(8, duration);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // func for updating lesson by id
    public void updateLesson(int lessonID, String title, int topicID, String content, int order, String description, String status, int duration) {
        String sql = "update Lessons set Title = ?, TopicID = ?, Content = ?, [Order] = ?, Description = ?, Status = ?, Duration = ? where LessonID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, topicID);
            pstmt.setString(3, content);
            pstmt.setInt(4, order);
            pstmt.setString(5, description);
            pstmt.setString(6, status);
            pstmt.setInt(7, duration);
            pstmt.setInt(8, lessonID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // check if a lesson with the order is existed or not
    public boolean validLessonOrder(int subjectID, int topicID, int order) {
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

    // func for deleting a lesson
    public void deleteLesson(int lessonID) {
        String sql = "delete Lessons where LessonID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lessonID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addLessonTopic(SubjectTopic topic) {
        String insertLessonTopicSQL = "INSERT INTO LessonTopic (Name) VALUES (?)";
        String selectLastInsertedTopicIDSQL = "SELECT MAX(TopicID) AS TopicID FROM LessonTopic";
        String insertSubjectLessonTopicSQL = "INSERT INTO Course_LessonTopic (TopicID, SubjectID, [Order]) VALUES (?, ?, ?)";

        try (PreparedStatement ps1 = connection.prepareStatement(insertLessonTopicSQL)) {
            // Thêm vào bảng LessonTopic
            ps1.setString(1, topic.getTopicName());
            ps1.executeUpdate();

            // Lấy TopicID vừa được thêm
            try (PreparedStatement ps2 = connection.prepareStatement(selectLastInsertedTopicIDSQL); ResultSet rs = ps2.executeQuery()) {
                if (rs.next()) {
                    int generatedTopicID = rs.getInt("TopicID");
                    topic.setTopicID(generatedTopicID);

                    // Thêm vào bảng Subject_LessonTopic với TopicID mới
                    try (PreparedStatement ps3 = connection.prepareStatement(insertSubjectLessonTopicSQL)) {
                        ps3.setInt(1, topic.getTopicID());
                        ps3.setInt(2, topic.getSubjectID());
                        ps3.setInt(3, topic.getOrder());
                        ps3.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteLessonTopic(int topicID, int subjectID) {
        String deleteSubjectLessonTopicSQL = "DELETE FROM Course_LessonTopic WHERE TopicID = ? AND SubjectID = ?";
        String deleteLessonTopicSQL = "DELETE FROM LessonTopic WHERE TopicID = ? AND NOT EXISTS (SELECT 1 FROM Course_LessonTopic WHERE TopicID = ?)";

        try (PreparedStatement ps1 = connection.prepareStatement(deleteSubjectLessonTopicSQL)) {
            ps1.setInt(1, topicID);
            ps1.setInt(2, subjectID);
            ps1.executeUpdate();

            try (PreparedStatement ps2 = connection.prepareStatement(deleteLessonTopicSQL)) {
                ps2.setInt(1, topicID);
                ps2.setInt(2, topicID);
                ps2.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateLessonTopic(SubjectTopic topic) {
        String updateTopicNameSQL = "UPDATE LessonTopic SET Name = ? WHERE TopicID = ?";
        String updateOrderSQL = "UPDATE Course_LessonTopic SET [Order] = ? WHERE TopicID = ? AND SubjectID = ?";

        try (
                PreparedStatement ps1 = connection.prepareStatement(updateTopicNameSQL); PreparedStatement ps2 = connection.prepareStatement(updateOrderSQL)) {

            ps1.setString(1, topic.getTopicName());
            ps1.setInt(2, topic.getTopicID());
            ps1.executeUpdate();

            ps2.setInt(1, topic.getOrder());
            ps2.setInt(2, topic.getTopicID());
            ps2.setInt(3, topic.getSubjectID());
            ps2.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countLessonsBySubjectId(int subjectID) {
        int count = 0;
        String sql = "SELECT COUNT(*) AS Total FROM Lessons WHERE SubjectID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean isOrderUnique(int subjectID, int order) {
        String query = "select Count(*) from [Course_LessonTopic] where SubjectID=? and [Order]=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, subjectID);
            ps.setInt(2, order);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Trả về true nếu không có bản ghi nào
            }
        } catch (SQLException e) {
            System.out.println("Error in isOrderUnique: " + e.getMessage());
        }
        return false;
    }

    public SubjectTopic getLessonTopicById(int topicID) {
        // Truy vấn để lấy dữ liệu từ cả hai bảng
        String query = "SELECT lt.TopicID, slt.SubjectID, lt.Name, slt.[Order] "
                + "FROM LessonTopic lt "
                + "JOIN Course_LessonTopic slt ON lt.TopicID = slt.TopicID "
                + "WHERE lt.TopicID = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, topicID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new SubjectTopic(
                        rs.getString("Name"), // Lấy tên chủ đề từ bảng LessonTopic
                        rs.getInt("TopicID"), // Lấy TopicID từ bảng LessonTopic
                        rs.getInt("SubjectID"), // Lấy SubjectID từ bảng Subject_LessonTopic
                        rs.getInt("Order") // Lấy thứ tự từ bảng Subject_LessonTopic
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Assuming you have a method to get the first lesson for a subject
    public Lesson getFirstLessonBySubjectId(int subjectId) {
        Lesson firstLesson = null;
        String sql = "SELECT TOP 1* FROM Lessons WHERE subjectId = ? ORDER BY LessonID ASC "; // Or based on created_at

        try (
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                firstLesson = new Lesson();
                firstLesson.setLessonID(rs.getInt("LessonID"));
                firstLesson.setTitle(rs.getString("Title"));
                firstLesson.setContent(rs.getString("Content"));
                firstLesson.setDescription(rs.getString("Description"));
                // Any other lesson details you want to map here
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firstLesson;
    }


    public static void main(String[] args) {
   
      

            // Test the updateLesson method
          LessonDAO test = new LessonDAO();
            test.addLesson(1, "title", 1, "content", 2, "description", "Active", 50);

          
    }


}
