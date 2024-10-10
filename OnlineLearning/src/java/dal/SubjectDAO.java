 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import model.SubjectCategoryCount;


    public List<Subject> getAllSubjects(int offset, int limit) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM Subjects s JOIN Users u ON s.OwnerID = u.UserID ORDER BY Update_Date OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Set the offset and limit parameters
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectID"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("Title"));
                    subject.setDescription(rs.getString("Description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryID"));

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
                    subject.setUserName(rs.getString("Username"));
                    list.add(subject);
                }
            }
                if (rs.next()) {
                    subject = new Subject();
                    subject.setSubjectID(rs.getInt("SubjectId"));
                    subject.setUserID(rs.getInt("OwnerID"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
    }
    public List<Subject> searchSubjects(String query, int offset, int limit) {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Subjects s JOIN Users u ON s.OwnerID = u.UserID WHERE title LIKE ? OR description LIKE ? "
                + "ORDER BY Update_Date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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
                    subject.setUserName(rs.getString("Username"));
                    list.add(subject);
                }
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
    public static void main(String[] args) {
        SubjectDAO subjectDAO = new SubjectDAO();
         
    }
}
