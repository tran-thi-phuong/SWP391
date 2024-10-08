/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author sonna
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Blog;
import model.Users;
import model.BlogCategory;

public class BlogDAO extends DBContext {

    public List<Blog> getBlogsByPage(int page, int pageSize) {
        List<Blog> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT b.*, u.Username, u.Name, bc.Title as CategoryTitle "
                + "FROM Blogs b "
                + "JOIN Users u ON b.UserID = u.UserID "
                + "JOIN Blog_Category bc ON b.Blog_CategoryID = bc.Blog_CategoryID "
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
        String sql = "SELECT COUNT(*) FROM Blogs";
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

    public Blog getBlogById(int blogId) {
        Blog blog = null;
        String sql = "SELECT b.*, u.Username, u.Name, bc.Title as CategoryTitle "
                + "FROM Blogs b "
                + "JOIN Users u ON b.UserID = u.UserID "
                + "JOIN Blog_Category bc ON b.Blog_CategoryID = bc.Blog_CategoryID "
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

    public static void main(String[] args) {
        BlogDAO blogDAO = new BlogDAO();

        // Test pagination
        int page = 1; // Example page number
        int pageSize = 8; // Number of blogs per page
        List<Blog> blogs = blogDAO.getBlogsByPage(page, pageSize);
        int totalBlogs = blogDAO.getTotalBlogs();
        int totalPages = (int) Math.ceil((double) totalBlogs / pageSize);

        // Print blog details
        System.out.println("Total Blogs: " + totalBlogs);
        System.out.println("Total Pages: " + totalPages);
        System.out.println("Blogs on Page " + page + ":");
        for (Blog blog : blogs) {
            System.out.println("------------------------------");
            System.out.println("Blog ID: " + blog.getBlogId());
            System.out.println("Title: " + blog.getTitle());
            System.out.println("Content: " + (blog.getContent().length() > 50 ? blog.getContent().substring(0, 50) + "..." : blog.getContent()));
            System.out.println("Created At: " + blog.getCreateAt());
            System.out.println("Author: " + blog.getUserId().getName() + " (Username: " + blog.getUserId().getUsername() + ")");
            System.out.println("Category: " + blog.getBlogCategoryId().getTitle());
        }
        System.out.println("------------------------------");
        
        // Kiểm tra getBlogById
    System.out.println("------------------------------");
    System.out.println("Testing getBlogById:");

    int testBlogId = 4; // Thay bằng một blogId hợp lệ từ cơ sở dữ liệu của bạn
    Blog blogById = blogDAO.getBlogById(testBlogId);
    
    if (blogById != null) {
        System.out.println("Blog ID: " + blogById.getBlogId());
        System.out.println("Title: " + blogById.getTitle());
        System.out.println("Content: " + (blogById.getContent().length() > 50 ? blogById.getContent().substring(0, 50) + "..." : blogById.getContent()));
        System.out.println("Created At: " + blogById.getCreateAt());
        System.out.println("Author: " + blogById.getUserId().getName());
        System.out.println("Category: " + blogById.getBlogCategoryId().getTitle());
    } else {
        System.out.println("No blog found with ID: " + testBlogId);
    }
    }
}
