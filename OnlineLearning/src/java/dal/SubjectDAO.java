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
import model.LessonType;
import model.SubjectCategoryCount;

/**
 *
 * @author Admin
 */
public class SubjectDAO extends DBContext {

    public List<Subject> getAllSubjects(int offset, int limit) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM Subjects s JOIN Users u ON s.OwnerID = u.UserID ORDER BY Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

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
        String sql = "SELECT * FROM Subjects s JOIN Users u ON s.OwnerID = u.UserID"; // Select all subjects

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
        String sql = "SELECT COUNT(*) FROM Subjects";
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
        String sql = "SELECT COUNT(*) FROM Subjects where Status = 'Active'";
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
        String sql = "SELECT COUNT(*) FROM Subjects WHERE Update_Date BETWEEN ? AND ?";
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
        String sql = "SELECT * FROM Subjects s JOIN Users u ON s.OwnerID = u.UserID WHERE Subject_CategoryID = ? "
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
        String sql = "SELECT COUNT(*) FROM Subjects WHERE Subject_CategoryId = ?";
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
        String query = "INSERT INTO Subjects (title, description, Subject_CategoryID, status, Update_Date, Thumbnail) VALUES (?, ?, ?, ?, ?, ?)";
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
        String query = "DELETE FROM Subjects WHERE SubjectId = ?";
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
        String query = "UPDATE Subjects SET title = ?, description = ?, Subject_CategoryID = ?, status = ?, Update_Date = ?, Thumbnail = ? WHERE SubjectId = ?";
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
        String query = "SELECT * FROM Subjects WHERE SubjectId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectId"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subject.setStatus(rs.getString("status"));
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
            String query = "SELECT title FROM Subjects WHERE SubjectID = ?";
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
        String sql = "SELECT * FROM Subjects WHERE title LIKE ? OR description LIKE ? "
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
        String sql = "SELECT COUNT(*) FROM Subjects WHERE title LIKE ? OR description LIKE ?";
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
        String sql = "SELECT TOP 5 * FROM Subjects ORDER BY Update_Date DESC";
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
            String sql = "SELECT * FROM Subjects";
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
                    + "FROM Subject_Category sc "
                    + "LEFT JOIN Subjects s ON sc.Subject_CategoryID = s.Subject_CategoryID "
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

    public boolean addSubject(String courseName, String category, String status, String description, String thumbnailPath) {
        boolean isAdded = false;
        String sql = "INSERT INTO Subjects (Title, Description, Subject_CategoryID, Status, Thumbnail, Update_Date, OwnerID) VALUES (?, ?, ?, ?, ?, GETDATE(),?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseName);
            ps.setString(2, description);
            ps.setInt(3, Integer.parseInt(category));
            ps.setString(4, status);
            ps.setString(5, thumbnailPath);
            ps.setInt(6, 1);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                isAdded = true;
            }
        } catch (SQLException e) {
            System.out.println("Error in addSubject: " + e.getMessage());
        }

        return isAdded;
    }

    public List<Subject> getSubjectDetailsByUserIdandSubjectID(int userId, int subjectId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = """
          SELECT s.SubjectID,staff.username,staff.UserID AS OwnerID,staff.Name AS OwnerName,s.Title AS SubjectTitle, 
          lt.TypeID, lt.Name AS LessonTypeName, 
          l.LessonID, l.Title AS LessonTitle, l.[Order] AS LessonOrder,
          lu.Status AS UserLessonStatus
          FROM Subjects s 
          JOIN Registrations r ON s.SubjectID = r.SubjectID 
          JOIN Lessons l ON l.SubjectID = s.SubjectID 
          JOIN LessonType lt ON lt.TypeID = l.TypeID
          JOIN Users staff ON s.OwnerID = staff.UserID
          LEFT JOIN Lesson_User lu ON l.LessonID = lu.LessonID AND lu.UserID = ?
          WHERE r.UserID = ?
          AND s.SubjectID = ?
          ORDER BY s.SubjectID, lt.TypeID, l.[Order], l.LessonID
          """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); // Đặt userId cho trạng thái bài học
            ps.setInt(2, userId); // Đặt userId cho điều kiện của bảng Registrations
            ps.setInt(3, subjectId); // Đặt subjectId cho điều kiện của bảng Subjects

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Kiểm tra xem môn học đã tồn tại trong danh sách hay chưa
                    Subject subject = null;
                    for (Subject existingSubject : subjects) {
                        if (existingSubject.getSubjectID() == rs.getInt("SubjectID")) {
                            subject = existingSubject;
                            break;
                        }
                    }

                    // Nếu môn học chưa tồn tại, tạo mới
                    if (subject == null) {
                        subject = new Subject();
                        subject.setSubjectID(rs.getInt("SubjectID"));
                        subject.setOwnerName(rs.getString("OwnerName"));
                        subject.setUserName(rs.getString("username"));
                        subject.setTitle(rs.getString("SubjectTitle"));
                        subject.setLessonTypes(new ArrayList<>());
                        subjects.add(subject);
                    }

                    // Tạo LessonType và Lesson
                    LessonType lessonType = new LessonType();
                    lessonType.setTypeID(rs.getInt("TypeID"));
                    lessonType.setName(rs.getString("LessonTypeName"));

                    Lesson lesson = new Lesson();
                    lesson.setLessonID(rs.getInt("LessonID"));
                    lesson.setTitle(rs.getString("LessonTitle"));
                    lesson.setStatus(rs.getString("UserLessonStatus"));

                    // Kiểm tra xem LessonType đã tồn tại trong Subject chưa
                    boolean lessonTypeExists = false;
                    for (LessonType existingLessonType : subject.getLessonTypes()) {
                        if (existingLessonType.getTypeID() == lessonType.getTypeID()) {
                            existingLessonType.getLessons().add(lesson);
                            lessonTypeExists = true;
                            break;
                        }
                    }

                    // Nếu LessonType chưa tồn tại, thêm vào
                    if (!lessonTypeExists) {
                        List<Lesson> lessons = new ArrayList<>();
                        lessons.add(lesson);
                        lessonType.setLessons(lessons);
                        subject.getLessonTypes().add(lessonType);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getSubjectDetailsByUserIdandSubjectID: " + e.getMessage());
        }
        return subjects;
    }

    public List<Subject> getSubjectDetailsBySubjectID(int subjectId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = """
          SELECT s.SubjectID, staff.username,staff.UserID AS OwnerID, staff.Name AS OwnerName, s.Title AS SubjectTitle, 
          lt.TypeID, lt.Name AS LessonTypeName, 
          l.LessonID, l.Title AS LessonTitle, l.[Order] AS LessonOrder
          FROM Subjects s 
          JOIN Registrations r ON s.SubjectID = r.SubjectID 
          JOIN Lessons l ON l.SubjectID = s.SubjectID 
          JOIN LessonType lt ON lt.TypeID = l.TypeID
          JOIN Users staff ON s.OwnerID = staff.UserID
          WHERE s.SubjectID = ?
          ORDER BY s.SubjectID, lt.TypeID, l.[Order], l.LessonID
          """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, subjectId); // Set subjectId for the Subjects table condition

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Check if the subject already exists in the list
                    Subject subject = null;
                    for (Subject existingSubject : subjects) {
                        if (existingSubject.getSubjectID() == rs.getInt("SubjectID")) {
                            subject = existingSubject;
                            break;
                        }
                    }

                    // If the subject doesn't exist, create a new one
                    if (subject == null) {
                        subject = new Subject();
                        subject.setSubjectID(rs.getInt("SubjectID"));
                        subject.setOwnerName(rs.getString("OwnerName"));
                        subject.setUserName(rs.getString("username"));
                        subject.setTitle(rs.getString("SubjectTitle"));
                        subject.setLessonTypes(new ArrayList<>());
                        subjects.add(subject);
                    }

                    // Create LessonType and Lesson
                    LessonType lessonType = new LessonType();
                    lessonType.setTypeID(rs.getInt("TypeID"));
                    lessonType.setName(rs.getString("LessonTypeName"));

                    Lesson lesson = new Lesson();
                    lesson.setLessonID(rs.getInt("LessonID"));
                    lesson.setTitle(rs.getString("LessonTitle"));

                    // Check if LessonType already exists in the Subject
                    boolean lessonTypeExists = false;
                    for (LessonType existingLessonType : subject.getLessonTypes()) {
                        if (existingLessonType.getTypeID() == lessonType.getTypeID()) {
                            existingLessonType.getLessons().add(lesson);
                            lessonTypeExists = true;
                            break;
                        }
                    }

                    // If LessonType doesn't exist, add it
                    if (!lessonTypeExists) {
                        List<Lesson> lessons = new ArrayList<>();
                        lessons.add(lesson);
                        lessonType.setLessons(lessons);
                        subject.getLessonTypes().add(lessonType);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getSubjectDetailsBySubjectID: " + e.getMessage());
        }
        return subjects;
    }

    public static void main(String[] args) {
        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> specificSubjects = subjectDAO.getSubjectDetailsBySubjectID(8);

        // Check if the list of specific subjects is not empty
        if (specificSubjects != null && !specificSubjects.isEmpty()) {
            System.out.println("Specific subjects found for Subject ID 8:");
            for (Subject subject : specificSubjects) {
                System.out.println("Subject ID: " + subject.getSubjectID());
                System.out.println("Owner Name: " + subject.getOwnerName());
                System.out.println("Title: " + subject.getTitle());
                System.out.println("Lesson Types:");

                if (subject.getLessonTypes() != null && !subject.getLessonTypes().isEmpty()) {
                    for (LessonType lessonType : subject.getLessonTypes()) {
                        System.out.println("  Type ID: " + lessonType.getTypeID());
                        System.out.println("  Type Name: " + lessonType.getName());

                        if (lessonType.getLessons() != null && !lessonType.getLessons().isEmpty()) {
                            for (Lesson lesson : lessonType.getLessons()) {
                                System.out.println("    Lesson ID: " + lesson.getLessonID());
                                System.out.println("    Lesson Title: " + lesson.getTitle());
                                System.out.println("    Lesson Status: " + (lesson.getStatus() != null ? lesson.getStatus() : "N/A"));
                            }
                        } else {
                            System.out.println("    No lessons available for this lesson type.");
                        }
                    }
                } else {
                    System.out.println("  No lesson types available for this subject.");
                }
                System.out.println();
            }
        } else {
            System.out.println("No specific subjects found for Subject ID 8.");
        }
    }

}
