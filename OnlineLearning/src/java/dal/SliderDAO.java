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
    String sql = "SELECT TOP " + n + " s.*, u.userName AS ownerName "
               + "FROM Course s " // Added alias for Course here
               + "JOIN (SELECT SubjectID, COUNT(*) as RegistrationCount "
               + "      FROM Registrations "
               + "      GROUP BY SubjectID) r ON s.SubjectID = r.SubjectID "
               + "JOIN Users u ON s.ownerID = u.userID "
               + "ORDER BY r.RegistrationCount DESC";

    try (PreparedStatement st = connection.prepareStatement(sql);
         ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
            Subject subject = new Subject();
            subject.setSubjectID(rs.getInt("SubjectID"));
            subject.setTitle(rs.getString("Title"));
            subject.setOwnerName(rs.getString("ownerName"));
            subject.setDescription(rs.getString("Description"));
            subject.setThumbnail(rs.getString("Thumbnail"));
            subject.setUpdateDate(rs.getTimestamp("Update_Date"));
            subjects.add(subject);
        }
    }
    return subjects;
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
                slider.setBacklink(rs.getString("Backlink"));
                slider.setStatus(rs.getString("Status"));
                sliders.add(slider);
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching sliders: " + e.getMessage());
        }
        return sliders;
    }

    public boolean addSlider(Slider slider) {
        String sql = "INSERT INTO Sliders (title, image, backlink, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, slider.getTitle());
            ps.setString(2, slider.getImage());
            ps.setString(3, slider.getBacklink());
            ps.setString(4, slider.getStatus());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
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


    public static void main(String[] args) throws SQLException {
        

        
            // Assuming your DAO class is called SubjectDAO
            SliderDAO subjectDAO = new SliderDAO();

            // Test the getTopSubjects method with a specific value for 'n'
            int n = 5; // Number of top subjects to retrieve
            List<Subject> topSubjects = subjectDAO.getTopSubjects(6);

            // Print the retrieved subjects
            System.out.println("Top " + n + " Subjects:");
            for (Subject subject : topSubjects) {
                System.out.println("Subject ID: " + subject.getSubjectID());
                System.out.println("Title: " + subject.getTitle());
                System.out.println("Owner Name: " + subject.getOwnerName());
                System.out.println("Description: " + subject.getDescription());
                System.out.println("Thumbnail: " + subject.getThumbnail());
                System.out.println("Update Date: " + subject.getUpdateDate());
                System.out.println("-------------------------------------------------");
            }
    }

}
