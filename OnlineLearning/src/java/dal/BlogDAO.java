/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author sonna
 */
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Blog;
import model.Users;
import model.BlogCategory;
import model.BlogMedia;
import java.util.regex.*;

public class BlogDAO extends DBContext {

    public List<Blog> getBlogsByPage(int page, int pageSize) {
        List<Blog> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT b.*, u.Username, u.Name, bc.Title as CategoryTitle "
                + "FROM Blogs b "
                + "JOIN Users u ON b.UserID = u.UserID "
                + "JOIN Blog_Category bc ON b.Blog_CategoryID = bc.Blog_CategoryID "
                + "WHERE b.Status!='Hide'"
                + "ORDER BY b.Create_At DESC "
                + "OFFSET ? ROWS "
                + "FETCH NEXT ? ROWS ONLY";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, offset);
            st.setInt(2, pageSize);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Blog b = new Blog();
                b.setBlogId(rs.getInt("BlogId"));
                b.setTitle(rs.getString("Title"));
                b.setContent(rs.getString("Content"));
                b.setCreateAt(rs.getDate("Create_At"));

                Users u = new Users();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setName(rs.getString("Name"));
                b.setUserId(u);

                BlogCategory c = new BlogCategory();
                c.setBlogCategoryId(rs.getInt("Blog_CategoryID"));
                c.setTitle(rs.getString("CategoryTitle"));
                b.setBlogCategoryId(c);

                list.add(b);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public int getTotalBlogs() {
        String sql = "SELECT COUNT(*) FROM Blogs where Status!='Hide'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public List<BlogCategory> getAllCategories() {
        List<BlogCategory> list = new ArrayList<>();
        String sql = "SELECT Blog_CategoryID, Title FROM Blog_Category";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                BlogCategory c = new BlogCategory();
                c.setBlogCategoryId(rs.getInt("Blog_CategoryID"));
                c.setTitle(rs.getString("Title"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public void createBlog(Blog blog) {
        String sql = "INSERT INTO Blogs (Title, Content, UserID, Blog_CategoryID, Create_At) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, blog.getTitle());
            st.setString(2, blog.getContent());
            st.setInt(3, blog.getUserId().getUserID());
            st.setInt(4, blog.getBlogCategoryId().getBlogCategoryId());
            st.setDate(5, new java.sql.Date(System.currentTimeMillis()));

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Blog successfully added.");
                // Optionally, retrieve generated keys if needed
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        blog.setBlogId(generatedId);
                        System.out.println("Generated Blog ID: " + generatedId);
                    }
                }
            } else {
                System.out.println("Failed to add blog.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding blog: " + e.getMessage());
        }
    }

    public Blog getBlogById(int blogId) {
        Blog blog = null;
        String sql = "SELECT b.*, u.Username, u.Name, bc.Title as CategoryTitle, bm.MediaID, bm.MediaType, bm.MediaLink, bm.Description "
                + "FROM Blogs b "
                + "JOIN Users u ON b.UserID = u.UserID "
                + "JOIN Blog_Category bc ON b.Blog_CategoryID = bc.Blog_CategoryID "
                + "LEFT JOIN Blog_Media bm ON b.BlogID = bm.BlogID " // Thêm JOIN để lấy media
                + "WHERE b.BlogID = ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, blogId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                blog = new Blog();
                blog.setBlogId(rs.getInt("BlogID"));
                blog.setTitle(rs.getString("Title"));
                blog.setContent(rs.getString("Content"));
                blog.setCreateAt(rs.getDate("Create_At"));

                Users u = new Users();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setName(rs.getString("Name"));
                blog.setUserId(u);

                BlogCategory c = new BlogCategory();
                c.setBlogCategoryId(rs.getInt("Blog_CategoryID"));
                c.setTitle(rs.getString("CategoryTitle"));
                blog.setBlogCategoryId(c);
           
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return blog;
    }

    public List<Blog> getAllBlogs() {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT b.*, u.Username, u.Name, bc.Title as CategoryTitle, bm.MediaID, bm.MediaType, bm.MediaLink, bm.Description "
                + "FROM Blogs b "
                + "JOIN Users u ON b.UserID = u.UserID "
                + "JOIN Blog_Category bc ON b.Blog_CategoryID = bc.Blog_CategoryID "
                + "LEFT JOIN Blog_Media bm ON b.BlogID = bm.BlogID "
                + "ORDER BY b.Create_At DESC";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Blog b = new Blog();
                b.setBlogId(rs.getInt("BlogID"));
                b.setTitle(rs.getString("Title"));
                b.setContent(rs.getString("Content"));
                b.setCreateAt(rs.getDate("Create_At"));
                b.setStatus(rs.getString("Status"));

                Users u = new Users();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setName(rs.getString("Name"));
                b.setUserId(u);

                BlogCategory c = new BlogCategory();
                c.setBlogCategoryId(rs.getInt("Blog_CategoryID"));
                c.setTitle(rs.getString("CategoryTitle"));
                b.setBlogCategoryId(c);

                list.add(b);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public void updateStatus(String blogId, String newStatus) {
        String sql = "UPDATE Blogs SET Status = ? WHERE BlogID = ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, newStatus);
            st.setString(2, blogId);
            int affectedRows = st.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Blog status updated successfully.");
            } else {
                System.out.println("No blog found with ID: " + blogId);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public boolean updateBlog(Blog blog) {
        String sql = "UPDATE Blogs SET Title = ?, Content = ?, Blog_CategoryID = ? WHERE BlogID = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, blog.getTitle());
            st.setString(2, blog.getContent());
            st.setInt(3, blog.getBlogCategoryId().getBlogCategoryId()); // Assuming the category ID is retrieved from BlogCategory object
            st.setInt(4, blog.getBlogId());

            int affectedRows = st.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public static void main(String[] args) {
        BlogDAO blogDAO = new BlogDAO();

        // Replace with a valid BlogID you want to test
        int testBlogId = 8; // Change this to the ID of an existing blog in your database

        System.out.println("------------------------------");
        System.out.println("Testing getBlogById:");

        Blog blog = blogDAO.getBlogById(testBlogId);
        if (blog != null) {
            System.out.println("Blog ID: " + blog.getBlogId());
            System.out.println("Title: " + blog.getTitle());
            System.out.println("Content: " + blog.getContent());
            System.out.println("Created At: " + blog.getCreateAt());
            System.out.println("Author: " + blog.getUserId().getName() + " (Username: " + blog.getUserId().getUsername() + ")");
            System.out.println("Category: " + blog.getBlogCategoryId().getTitle());      
        } else {
            System.out.println("No blog found with ID: " + testBlogId);
        }
        System.out.println("------------------------------");
    }

}
