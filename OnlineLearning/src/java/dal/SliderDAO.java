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
import model.Slider;
import model.Subject;

public class SliderDAO extends DBContext {

    // Hàm lấy 5 blog mới nhất
    public List<Blog> getLatestBlogs(int n) {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT TOP " + n + " BlogId, Title, Content, Create_At "
                + "FROM Blogs "
                + "ORDER BY Create_At DESC";
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

    public List<Subject> getTopSubjects(int n) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT TOP " + n + " s.* "
                + "FROM Subjects s "
                + "JOIN (SELECT SubjectID, COUNT(*) as RegistrationCount "
                + "      FROM Registrations "
                + "      GROUP BY SubjectID) r ON s.SubjectID = r.SubjectID "
                + "ORDER BY r.RegistrationCount DESC";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
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

    public void autoCreateSliders() throws SQLException {
        List<Subject> topSubjects = getTopSubjects(5);
        List<Blog> latestBlogs = getLatestBlogs(3);
        String sql = "INSERT INTO Sliders (BlogID, SubjectID, Title, Image, Content) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Tạo slider từ khóa học bán chạy nhất
            for (Subject subject : topSubjects) {
                if (!isSliderExists(null, subject.getSubjectID())) {
                    st.setNull(1, java.sql.Types.INTEGER);
                    st.setInt(2, subject.getSubjectID());
                    st.setString(3, "Slider for " + subject.getTitle());
                    st.setString(4, subject.getThumbnail());
                    st.setString(5, subject.getDescription());
                    st.executeUpdate();
                }
            }

            // Tạo slider từ bài viết mới nhất
            for (Blog blog : latestBlogs) {
                if (!isSliderExists(blog.getBlogId(), null)) {
                    st.setInt(1, blog.getBlogId());
                    st.setNull(2, java.sql.Types.INTEGER);
                    st.setString(3, "Slider for " + blog.getTitle());
                    st.setString(4, "default_image.jpg");
                    st.setString(5, blog.getContent().substring(0, 100));
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while creating sliders: " + e.getMessage());
        }
    }

    public boolean isSliderExists(Integer blogId, Integer subjectId) {
        String sql = "SELECT COUNT(*) FROM Sliders WHERE BlogID = ? OR SubjectID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setObject(1, blogId); // có thể là null
            st.setObject(2, subjectId); // có thể là null
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public List<Slider> getAllSliders() {
        List<Slider> sliders = new ArrayList<>();
        String sql = "SELECT * FROM Sliders";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Slider slider = new Slider();
                slider.setSliderID(rs.getInt("SliderID"));
                slider.setTitle(rs.getString("Title"));
                slider.setImage(rs.getString("Image"));
                slider.setContent(rs.getString("Content"));
                slider.setStatus(rs.getString("Status"));
                sliders.add(slider);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching sliders: " + e.getMessage());
        }
        return sliders;
    }

    public void updateSliderStatus(int sliderID, String status) {
        String sql = "UPDATE Sliders SET status = ? WHERE sliderID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, sliderID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Slider> searchAndFilterSliders(String searchQuery, String statusFilter) {
        List<Slider> sliders = new ArrayList<>();

        String sql = "SELECT * FROM Sliders WHERE 1=1";

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql += " AND title LIKE ?"; // Chỉ tìm kiếm theo title
            // Bỏ phần tìm kiếm backlink
        }

        if (statusFilter != null && !statusFilter.equals("All")) {
            sql += " AND status = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int paramIndex = 1;

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchQuery + "%");
            }

            if (statusFilter != null && !statusFilter.equals("All")) {
                ps.setString(paramIndex++, statusFilter);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Slider slider = new Slider();
                slider.setSliderID(rs.getInt("sliderID"));
                slider.setTitle(rs.getString("title"));
                slider.setImage(rs.getString("image"));
                slider.setContent(rs.getString("content"));
                slider.setStatus(rs.getString("status"));
                sliders.add(slider);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sliders;
    }

    public static void main(String[] args) {
    }
}
