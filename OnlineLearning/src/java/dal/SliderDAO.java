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
import model.Blog;
import model.Subject;

public class SliderDAO extends DBContext {

    // Hàm lấy 5 blog mới nhất
    public List<Blog> getLatestBlogs() {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT TOP 5 BlogId, Title, Content, Create_At " +
                     "FROM Blogs " +
                     "ORDER BY Create_At DESC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Blog b = new Blog();
                b.setBlogId(rs.getInt("BlogId"));
                b.setTitle(rs.getString("Title"));
                b.setContent(rs.getString("Content"));
                b.setCreateAt(rs.getDate("Create_At"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
    
    public List<Subject> getTopSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT TOP 5 * FROM Subjects ORDER BY SubjectID"; 
        
        try (
             PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

             while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectID(rs.getInt("SubjectID"));
                subject.setTitle(rs.getString("Title"));
                subject.setDescription(rs.getString("Description"));
                subject.setThumbnail(rs.getString("Thumbnail"));
                subject.setUpdateDate(rs.getTimestamp("Update_Date"));
                subjects.add(subject);
            }
        }
        return subjects;
    }

    public static void main(String[] args) {
        SliderDAO sliderDAO = new SliderDAO();

        // Test lấy 5 blog mới nhất
        List<Blog> latestBlogs = sliderDAO.getLatestBlogs();

        // In thông tin các blog mới nhất
        System.out.println("Latest 5 Blogs:");
        for (Blog blog : latestBlogs) {
            System.out.println("------------------------------");
            System.out.println("Blog ID: " + blog.getBlogId());
            System.out.println("Title: " + blog.getTitle());
            System.out.println("Content: " + (blog.getContent().length() > 50 ? blog.getContent().substring(0, 50) + "..." : blog.getContent()));
            System.out.println("Created At: " + blog.getCreateAt());
        }
        System.out.println("------------------------------");
        
        try {
            List<Subject> topSubjects = sliderDAO.getTopSubjects();
            System.out.println("Top 5 Subjects:");
            for (Subject subject : topSubjects) {
                System.out.println("------------------------------");
                System.out.println("Subject ID: " + subject.getSubjectID());
                System.out.println("Title: " + subject.getTitle());
                System.out.println("Description: " + subject.getDescription());
                System.out.println("Thumbnail: " + subject.getThumbnail());
                System.out.println("Update Date: " + subject.getUpdateDate());
            }
            System.out.println("------------------------------");
        } catch (SQLException e) {
            System.out.println("Error fetching subjects: " + e.getMessage());
        }
    }
}
