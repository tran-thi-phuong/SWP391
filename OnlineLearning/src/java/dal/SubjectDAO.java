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
public class SubjectDAO extends DBContext {

    public List<Subject> getAllSubjects(int offset, int limit) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM Subjects ORDER BY Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Set the offset and limit parameters
            ps.setInt(1, offset);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectId(rs.getInt("SubjectId"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    subject.setStatus(rs.getString("status"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
                    subjects.add(subject);
                }
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

    public List<Subject> getSubjectsByCategory(int categoryId, int offset, int limit) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Subjects WHERE Subject_CategoryId = ? "
                + "ORDER BY Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectId(rs.getInt("SubjectId"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    subject.setStatus(rs.getString("status"));
                    subject.setUpdateDate(rs.getDate("Update_Date"));
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
        String query = "INSERT INTO Subjects (SubjectId, title, description, Subject_CategoryId, status, updateDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, subject.getSubjectId());
            ps.setString(2, subject.getTitle());
            ps.setString(3, subject.getDescription());
            ps.setInt(4, subject.getSubjectCategoryId());
            ps.setString(5, subject.getStatus());
            ps.setDate(6, new java.sql.Date(subject.getUpdateDate().getTime()));
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
        String query = "UPDATE Subjects SET title = ?, description = ?, Subject_CategoryId = ?, status = ?, updateDate = ? WHERE SubjectId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, subject.getTitle());
            ps.setString(2, subject.getDescription());
            ps.setInt(3, subject.getSubjectCategoryId());
            ps.setString(4, subject.getStatus());
            ps.setDate(5, new java.sql.Date(subject.getUpdateDate().getTime()));
            ps.setInt(6, subject.getSubjectId());
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

    public List<Subject> searchSubjects(String query, int offset, int limit) {
    List<Subject> list = new ArrayList<>();
    String sql = "SELECT * FROM Subjects WHERE title LIKE ? OR description LIKE ? " +
                 "ORDER BY Update_Date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, "%" + query + "%");
        ps.setString(2, "%" + query + "%");
        ps.setInt(3, offset);
        ps.setInt(4, limit);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("SubjectId"));
                subject.setTitle(rs.getString("title"));
                subject.setDescription(rs.getString("description"));
                subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                subject.setStatus(rs.getString("status"));
                subject.setUpdateDate(rs.getTimestamp("Update_Date"));
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

    public static void main(String[] args) {
        SubjectDAO sDAO = new SubjectDAO();
        List<Subject> list = sDAO.getAllSubjects(1, 3);
        System.out.println(list);
        int a = sDAO.getTotalSearchSubjects("a");
        System.out.println(a);
        List<Subject> l = sDAO.searchSubjects("a",1, 3);
        System.out.println(l);
    }
}
