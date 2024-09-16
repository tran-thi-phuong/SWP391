/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Subject;
import java.sql.PreparedStatement;
import java.sql.*;
/**
 *
 * @author Admin
 */
public class SubjectDAO extends DBContext{
    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Subjects";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("SubjectId"));
                subject.setTitle(rs.getString("title"));
                subject.setDescription(rs.getString("description"));
                subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                subject.setStatus(rs.getString("status"));
                list.add(subject);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Subject> getAllSubjectsByCategory(int categoryId) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Subjects WHERE Subject_CategoryId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectId(rs.getInt("SubjectId"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    subject.setStatus(rs.getString("status"));
                    list.add(subject);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllSubjectsByCategory: " + e.getMessage());
        }
        return list;
    }
    
    public boolean insertSubject(Subject subject) {
        String query = "INSERT INTO Subjects (SubjectId, title, description, Subject_CategoryId, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, subject.getSubjectId());
            ps.setString(2, subject.getTitle());
            ps.setString(3, subject.getDescription());
            ps.setInt(4, subject.getSubjectCategoryId());
            ps.setString(5, subject.getStatus());
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
        String query = "UPDATE Subjects SET title = ?, description = ?, Subject_CategoryId = ?, status = ? WHERE SubjectId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, subject.getTitle());
            ps.setString(2, subject.getDescription());
            ps.setInt(3, subject.getSubjectCategoryId());
            ps.setString(4, subject.getStatus());
            ps.setInt(5, subject.getSubjectId());
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
                    subject.setSubjectId(rs.getInt("SubjectId"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    subject.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subject;
    }

    public List<Subject> searchSubjects(String query) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Subjects WHERE title LIKE ? OR description LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("subjectId"));
                subject.setTitle(rs.getString("title"));
                subject.setDescription(rs.getString("description"));
                subject.setSubjectCategoryId(rs.getInt("subjectCategoryId"));
                subject.setStatus(rs.getString("status"));
                list.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        SubjectDAO sDAO = new SubjectDAO();
        List<Subject> list = sDAO.getAllSubjects();
        System.out.println(list);
        
        int testCategoryId = 1; // Giả sử category ID 1 tồn tại
        List<Subject> subjectsByCategory = sDAO.getAllSubjectsByCategory(testCategoryId);
        System.out.println("Subjects in category " + testCategoryId + ": " + subjectsByCategory);
    }
}
