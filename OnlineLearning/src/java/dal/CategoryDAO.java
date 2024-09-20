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
import model.SubjectCategory;

public class CategoryDAO extends DBContext {

    public List<SubjectCategory> getAllCategories() {
        List<SubjectCategory> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Subject_Category";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                SubjectCategory category = new SubjectCategory();
                category.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                category.setTitle(rs.getString("title"));
                list.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllCategories: " + e.getMessage());
        }
        return list;
    }

    public boolean insertCategory(SubjectCategory category) {
        String query = "INSERT INTO Subject_Category (title) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getTitle());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setSubjectCategoryId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error in insertCategory: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteCategory(int categoryId) {
        String query = "DELETE FROM Subject_Category WHERE Subject_CategoryId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Error in deleteCategory: " + e.getMessage());
        }
        return false;
    }

    public boolean updateCategory(SubjectCategory category) {
        String query = "UPDATE Subject_Category SET title = ? WHERE Subject_CategoryId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, category.getTitle());
            ps.setInt(2, category.getSubjectCategoryId());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error in updateCategory: " + e.getMessage());
        }
        return false;
    }

    public SubjectCategory getCategoryById(int categoryId) {
        SubjectCategory category = null;
        String query = "SELECT * FROM Subject_Category WHERE Subject_CategoryId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    category = new SubjectCategory();
                    category.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                    category.setTitle(rs.getString("title"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getCategoryById: " + e.getMessage());
        }
        return category;
    }

    public List<SubjectCategory> searchCategories(String query) {
        List<SubjectCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM Subject_Category WHERE title LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SubjectCategory category = new SubjectCategory();
                category.setSubjectCategoryId(rs.getInt("Subject_CategoryId"));
                category.setTitle(rs.getString("title"));
                list.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Error in searchCategories: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {
        CategoryDAO cDAO = new CategoryDAO();
        List<SubjectCategory> list = cDAO.getAllCategories();
        System.out.println(list);
        
        // Test other methods here
    }
}