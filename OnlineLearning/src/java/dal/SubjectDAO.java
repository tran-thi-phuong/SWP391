/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Subject;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import model.Lesson;
import model.LessonTopic;
import model.SubjectCategoryCount;
import model.SubjectTopic;

/**
 *
 * @author Admin
 */
public class SubjectDAO extends DBContext {

    public List<Subject> getAllSubjects(int offset, int limit) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM Course s JOIN Users u ON s.OwnerID = u.UserID ORDER BY Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Set the offset and limit parameters
            ps.setInt(1, offset);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("Title"));
                    subject.setDescription(rs.getString("Description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));
                    subject.setStatus(rs.getString("Status"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subject.setUserName(rs.getString("Username"));
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllSubjects: " + e.getMessage());
        }

        return subjects;
    }

    // Method to get subjects with status "Active"
    public List<Subject> getActiveSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT SubjectID, Title, Status FROM Course WHERE Status = 'Active'"; // Include Status in SELECT statement

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectID(rs.getInt("SubjectID"));
                subject.setTitle(rs.getString("Title"));
                subject.setStatus(rs.getString("Status")); // Fetch Status from ResultSet
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions if needed
        }

        return subjects;
    }

    public List<Subject> getAllActiveSubjects(int offset, int limit) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM Course s JOIN Users u ON s.OwnerID = u.UserID where s.Status = 'Active' ORDER BY Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Set the offset and limit parameters
            ps.setInt(1, offset);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("Title"));
                    subject.setDescription(rs.getString("Description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));
                    subject.setStatus(rs.getString("Status"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subject.setUserName(rs.getString("Username"));
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllSubjects: " + e.getMessage());
        }

        return subjects;
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM Course s JOIN Users u ON s.OwnerID = u.UserID"; // Select all subjects

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectID(rs.getInt("SubjectID"));
                subject.setUserID(rs.getInt("OwnerID"));
                subject.setTitle(rs.getString("Title"));
                subject.setDescription(rs.getString("Description"));
                subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));
                subject.setStatus(rs.getString("Status"));
                subject.setThumbnail(rs.getString("Thumbnail"));
                subject.setUpdateDate(rs.getDate("Update_Date"));
                subject.setUserName(rs.getString("Username"));
                subjects.add(subject); // Add the subject to the list
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllSubjects: " + e.getMessage());
        }

        return subjects;
    }

    public int getTotalSubjects() {
        String sql = "SELECT COUNT(*) FROM Course";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalSubjects: " + e.getMessage());
        }
        return 0;
    }

    public int getTotalActiveSubjects() {
        String sql = "SELECT COUNT(*) FROM Course where Status = 'Active'";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalSubjects: " + e.getMessage());
        }
        return 0;
    }

    public int getNewSubjectByTime(Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM Course WHERE Update_Date BETWEEN ? AND ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalUpdatedSubjects: " + e.getMessage());
        }
        return 0;
    }

    public List<Subject> getSubjectsByCategory(int categoryId, int offset, int limit) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Course s JOIN Users u ON s.OwnerID = u.UserID WHERE Subject_CategoryID = ? "
                + "ORDER BY Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("Title"));
                    subject.setDescription(rs.getString("Description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));
                    subject.setStatus(rs.getString("Status"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                    list.add(subject);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getSubjectsByCategory: " + e.getMessage());
        }
        return list;
    }

    public List<Subject> getActiveSubjectsByCategory(int categoryId, int offset, int limit) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Course s JOIN Users u ON s.OwnerID = u.UserID WHERE Subject_CategoryID = ? and s.Status = 'Active' "
                + "ORDER BY Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("Title"));
                    subject.setDescription(rs.getString("Description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));
                    subject.setStatus(rs.getString("Status"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                    list.add(subject);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getSubjectsByCategory: " + e.getMessage());
        }
        return list;
    }

    public int getTotalSubjectsByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Course WHERE Subject_CategoryId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalSubjectsByCategory: " + e.getMessage());
        }
        return 0;
    }

    public int getTotalActiveSubjectsByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Course WHERE Subject_CategoryId = ? and Status = 'Active'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalSubjectsByCategory: " + e.getMessage());
        }
        return 0;
    }

    public boolean insertSubject(Subject subject) {
        String query = "INSERT INTO Course (Title, Description, Subject_CategoryID, Dtatus, Update_Date, Thumbnail) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, subject.getTitle());
            ps.setString(2, subject.getDescription());
            ps.setInt(3, subject.getSubjectCategoryId());
            ps.setString(4, subject.getStatus());
            ps.setDate(5, new java.sql.Date(subject.getUpdateDate().getTime()));
            ps.setString(6, subject.getThumbnail());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSubject(int subjectId) {
        String query = "DELETE FROM Course WHERE SubjectID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, subjectId);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSubject(Subject subject) {
        String query = "UPDATE Course SET Title = ?, Description = ?, Subject_CategoryID = ?, Status = ?, Update_Date = ?, Thumbnail = ? WHERE SubjectID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, subject.getTitle());
            ps.setString(2, subject.getDescription());
            ps.setInt(3, subject.getSubjectCategoryId());
            ps.setString(4, subject.getStatus());
            ps.setDate(5, new java.sql.Date(subject.getUpdateDate().getTime()));
            ps.setString(6, subject.getThumbnail());
            ps.setInt(7, subject.getSubjectID());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Subject getSubjectById(int subjectId) {
        Subject subject = null;
        String query = "SELECT * FROM Course WHERE SubjectID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("Description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subject.setStatus(rs.getString("Status"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subject;
    }

    public String getSubjectNameById(int subjectId) {
        String title = "";

        try {
            String query = "SELECT Title FROM Course WHERE SubjectID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, subjectId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        title = resultSet.getString("title");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return title;
    }

    public List<Subject> searchSubjects(String query, int offset, int limit) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE Title LIKE ? OR Description LIKE ? "
                + "ORDER BY Update_Date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ps.setInt(3, offset);
            ps.setInt(4, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectId"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    subject.setStatus(rs.getString("status"));
                    subject.setUpdateDate(rs.getTimestamp("Update_Date"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                    list.add(subject);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchSubjects: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<Subject> searchActiveSubjects(String query, int offset, int limit) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE Title LIKE ? OR Description LIKE ? and Status = 'Active' "
                + "ORDER BY Update_Date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ps.setInt(3, offset);
            ps.setInt(4, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectId"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    subject.setStatus(rs.getString("status"));
                    subject.setUpdateDate(rs.getTimestamp("Update_Date"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                    list.add(subject);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchSubjects: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalSearchSubjects(String query) {
        String sql = "SELECT COUNT(*) FROM Course WHERE Title LIKE ? OR Description LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalSubjectsBySearch: " + e.getMessage());
        }
        return 0;
    }

    public int getTotalActiveSearchSubjects(String query) {
        String sql = "SELECT COUNT(*) FROM Course WHERE Title LIKE ? OR Description LIKE ? and Status = 'Active' ";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalSubjectsBySearch: " + e.getMessage());
        }
        return 0;
    }

    public List<Subject> getFeaturedSubjects() {
        List<Subject> featuredSubjects = new ArrayList<>();
        String sql = "SELECT TOP 5 * FROM Course ORDER BY Update_Date DESC";
        try (
                PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Subject subject = new Subject();
                // Set subject properties from ResultSet
                featuredSubjects.add(subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return featuredSubjects;
    }

    public Map<Integer, Subject> getAllSubject() {
        Map<Integer, Subject> list = new HashMap<>();
        try {
            String sql = "SELECT * FROM Course";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectID(rs.getInt("SubjectID"));
                subject.setTitle(rs.getString("Title"));
                subject.setDescription(rs.getString("Description"));
                subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));
                subject.setStatus(rs.getString("Status"));
                subject.setUpdateDate(rs.getDate("Update_Date"));
                subject.setThumbnail(rs.getString("Thumbnail"));
                list.put(subject.getSubjectID(), subject);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<SubjectCategoryCount> getSubjectAllocation() {
        List<SubjectCategoryCount> list = new ArrayList<>();
        try {
            String sql = "SELECT sc.Title, COUNT(s.SubjectID) as SubjectCount "
                    + "FROM Course_Category sc "
                    + "LEFT JOIN Course s ON sc.Subject_CategoryID = s.Subject_CategoryID "
                    + "GROUP BY sc.Title HAVING COUNT(s.SubjectID) > 0";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                SubjectCategoryCount subCount = new SubjectCategoryCount();
                subCount.setCategory(rs.getString("Title"));
                subCount.setCount(rs.getInt("SubjectCount"));
                list.add(subCount);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public boolean updateSubject(String courseName, String category, String status, String description, String subjectID, String thumbnailPath) {
        String query = "UPDATE Course SET Title = ?, Description = ?, Subject_CategoryID = ?, Status = ?, Update_Date = GETDATE(), Thumbnail = ? WHERE SubjectID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, courseName);
            ps.setString(2, description);
            ps.setInt(3, Integer.parseInt(category));
            ps.setString(4, status);
            ps.setString(5, thumbnailPath);
            ps.setInt(6, Integer.parseInt(subjectID));

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int addSubject(String courseName, String category, String status, String description, String thumbnailPath, String ownerId) {
        int newSubjectId = 0;

        // Câu lệnh SQL để thêm Subject
        String insertSql = "INSERT INTO Course (Title, Description, Subject_CategoryID, Status, Thumbnail, Update_Date, OwnerID) "
                + "VALUES (?, ?, ?, ?, ?, GETDATE(), ?)";

        // Câu lệnh SQL để lấy SubjectID mới nhất
        String selectSql = "SELECT TOP 1 SubjectID FROM Subjects ORDER BY SubjectID DESC";

        try {
            // Thực hiện câu lệnh INSERT
            PreparedStatement psInsert = connection.prepareStatement(insertSql);
            psInsert.setString(1, courseName);
            psInsert.setString(2, description);
            psInsert.setInt(3, Integer.parseInt(category));
            psInsert.setString(4, status);
            psInsert.setString(5, thumbnailPath);
            psInsert.setInt(6, Integer.parseInt(ownerId));
            psInsert.executeUpdate();

            // Thực hiện câu lệnh SELECT để lấy SubjectID mới nhất
            PreparedStatement psSelect = connection.prepareStatement(selectSql);
            ResultSet rs = psSelect.executeQuery();
            if (rs.next()) {
                newSubjectId = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error in addSubject: " + e.getMessage());
        }

        return newSubjectId;
    }

    public List<Subject> getSubjectDetailsByUserIdAndSubjectID(int userId, int subjectId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = """
        SELECT s.SubjectID, s.Title AS SubjectTitle, 
               l.LessonID, l.Title AS LessonTitle, l.[Order] AS LessonOrder,
               lt.TopicID, lt.Name AS TopicName,
               lu.Status AS LessonStatus 
        FROM Lessons l
        JOIN Lesson_User lu ON l.LessonID = lu.LessonID
        JOIN Course s ON l.SubjectID = s.SubjectID
        JOIN LessonTopic lt ON l.TopicID = lt.TopicID
        WHERE l.SubjectID = ? AND lu.UserID = ? 
                 AND l.Status != 'Inactive'
        ORDER BY lt.TopicID, l.[Order]
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Set parameters dynamically instead of hardcoding them
            ps.setInt(1, subjectId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                Subject currentSubject = null;
                LessonTopic currentTopic = null;

                while (rs.next()) {
                    int subjectID = rs.getInt("SubjectID");
                    if (currentSubject == null || currentSubject.getSubjectID() != subjectID) {
                        currentSubject = new Subject();
                        currentSubject.setSubjectID(subjectID);
                        currentSubject.setTitle(rs.getString("SubjectTitle"));
                        currentSubject.setLessonTopics(new ArrayList<>());
                        subjects.add(currentSubject);
                    }

                    int topicID = rs.getInt("TopicID");
                    if (currentTopic == null || currentTopic.getTopicID() != topicID) {
                        currentTopic = new LessonTopic();
                        currentTopic.setTopicID(topicID);
                        currentTopic.setName(rs.getString("TopicName"));
                        currentTopic.setLessons(new ArrayList<>());
                        currentSubject.getLessonTopics().add(currentTopic);
                    }

                    // Create lesson and add it to the topic
                    Lesson lesson = new Lesson();
                    lesson.setLessonID(rs.getInt("LessonID"));
                    lesson.setTitle(rs.getString("LessonTitle"));
                    lesson.setOrder(rs.getInt("LessonOrder"));
                    lesson.setStatus(rs.getString("LessonStatus"));
                    currentTopic.getLessons().add(lesson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public List<Subject> getSubjectDetailsBySubjectID(int subjectID) {
        List<Subject> subjects = new ArrayList<>();
        String sql = """
        SELECT s.SubjectID, staff.username, staff.UserID AS OwnerID, staff.Name AS OwnerName, s.Title AS SubjectTitle, 
               lt.TopicID, lt.Name AS LessonTopicName, 
               l.LessonID, l.Title AS LessonTitle
        FROM Course s 
        JOIN Lessons l ON l.SubjectID = s.SubjectID 
        JOIN LessonTopic lt ON lt.TopicID = l.TopicID
        JOIN Users staff ON s.OwnerID = staff.UserID
        WHERE s.SubjectID = ? AND l.status != 'Inactive'
        ORDER BY lt.TopicID, l.LessonID
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, subjectID);
            ResultSet rs = pstmt.executeQuery();

            Subject subject = null;
            while (rs.next()) {
                if (subject == null) {
                    // Initialize the subject object only once
                    subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setTitle(rs.getString("SubjectTitle"));
                    subject.setOwnerName(rs.getString("OwnerName"));
                    subject.setLessonTopics(new ArrayList<>()); // Initialize lesson topics list
                    subjects.add(subject);
                }

                // Get LessonTopic information
                int topicID = rs.getInt("TopicID");
                LessonTopic lessonTopic = null;

                // Check if the LessonTopic already exists in the subject's list
                for (LessonTopic existingTopic : subject.getLessonTopics()) {
                    if (existingTopic.getTopicID() == topicID) {
                        lessonTopic = existingTopic;
                        break;
                    }
                }

                // If the LessonTopic doesn't exist, create a new one
                if (lessonTopic == null) {
                    lessonTopic = new LessonTopic();
                    lessonTopic.setTopicID(topicID);
                    lessonTopic.setName(rs.getString("LessonTopicName"));
                    lessonTopic.setLessons(new ArrayList<>()); // Initialize lessons list
                    subject.getLessonTopics().add(lessonTopic);
                }

                // Create and add Lesson to the LessonTopic
                Lesson lesson = new Lesson();
                lesson.setLessonID(rs.getInt("LessonID"));
                lesson.setTitle(rs.getString("LessonTitle"));
                lessonTopic.getLessons().add(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return subjects;
    }

    public List<Subject> searchSubjectsWithFilters(String query, String categoryId, String status,
            int offset, int limit, Integer ownerID) {
        List<Subject> subjects = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT s.*, u.Username FROM Course s JOIN Users u ON s.OwnerID = u.UserID WHERE 1=1");

        // Add owner filter for instructors
        if (ownerID != null) {
            sql.append(" AND s.OwnerID = ?");
        }

        // Add search filters
        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND s.Title LIKE ?");
        }
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            sql.append(" AND s.Subject_CategoryID = ?");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND s.Status = ?");
        }

        sql.append(" ORDER BY s.Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Set owner parameter first if it exists
            if (ownerID != null) {
                ps.setInt(paramIndex++, ownerID);
            }

            // Set other filter parameters
            if (query != null && !query.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + query + "%");
            }
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(categoryId));
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            // Set pagination parameters
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("Title"));
                    subject.setDescription(rs.getString("Description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));
                    subject.setStatus(rs.getString("Status"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subject.setUserName(rs.getString("Username"));
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in searchSubjectsWithFilters: " + e.getMessage());
        }
        return subjects;
    }

    public int getTotalSubjectsWithFilters(String query, String categoryId, String status, Integer ownerID) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Course s WHERE 1=1");

        // Add owner filter for instructors
        if (ownerID != null) {
            sql.append(" AND s.OwnerID = ?");
        }

        // Add search filters
        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND s.Title LIKE ?");
        }
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            sql.append(" AND s.Subject_CategoryID = ?");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND s.Status = ?");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Set owner parameter first if it exists
            if (ownerID != null) {
                ps.setInt(paramIndex++, ownerID);
            }

            // Set other filter parameters
            if (query != null && !query.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + query + "%");
            }
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(categoryId));
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalSubjectsWithFilters: " + e.getMessage());
        }
        return 0;
    }

    public int getTotalSubjects(Integer ownerId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Course WHERE 1=1");

        // Add filter for owner if present
        if (ownerId != null) {
            sql.append(" AND OwnerID = ?");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            if (ownerId != null) {
                ps.setInt(1, ownerId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalSubjects: " + e.getMessage());
        }
        return 0;
    }

    public List<Subject> getAllSubjects(int offset, int recordsPerPage, Integer ownerId) {
        List<Subject> subjects = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT s.*, u.Username FROM Course s JOIN Users u ON s.OwnerID = u.UserID WHERE 1=1");

        // Thêm điều kiện lọc theo owner nếu có
        if (ownerId != null) {
            sql.append(" AND s.OwnerID = ?");
        }

        // Thêm điều kiện phân trang
        sql.append(" ORDER BY s.SubjectID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Thiết lập tham số ownerId nếu có
            if (ownerId != null) {
                ps.setInt(paramIndex++, ownerId);
            }

            // Thiết lập tham số phân trang
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, recordsPerPage);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("Title"));
                    subject.setDescription(rs.getString("Description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));
                    subject.setStatus(rs.getString("Status"));
                    subject.setThumbnail(rs.getString("Thumbnail"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subject.setUserName(rs.getString("Username"));
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllSubjects: " + e.getMessage());
        }
        return subjects;
    }
    public String getSubjectTitleByID(int subjectID) throws SQLException {
        String sql = "SELECT Title FROM Course WHERE SubjectID = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, subjectID);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Title");
                } else {
                    return null; // Return null if no subject is found
                }
            }
        }
    }
    
}
