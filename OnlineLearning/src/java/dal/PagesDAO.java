/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author Admin
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Pages;

public class PagesDAO extends DBContext {

   

    // Phương thức thêm mới một trang
    public boolean addPage(String pageName, String pageUrl, String status) {
        String sql = "INSERT INTO Pages (PageName, PageUrl, Status, Update_At) VALUES (?, ?, ?, GETDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pageName);
            stmt.setString(2, pageUrl);
            stmt.setString(3, status);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức lấy tất cả các trang
    public List<Pages> getAllPages() {
        String sql = "SELECT * FROM Pages";
        List<Pages> pages = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pages page = new Pages(
                    rs.getInt("PageID"),
                    rs.getString("PageName"),
                    rs.getString("PageUrl"),
                    rs.getString("Status"),
                    rs.getTimestamp("Update_At")
                );
                pages.add(page);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pages;
    }

    // Phương thức cập nhật thông tin của một trang
    public boolean updatePage(int pageID, String pageName, String pageUrl, String status) {
        String sql = "UPDATE Pages SET PageName = ?, PageUrl = ?, Status = ? WHERE PageID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pageName);
            stmt.setString(2, pageUrl);
            stmt.setString(3, status);
            stmt.setInt(4, pageID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức xóa một trang
    public boolean deletePage(int pageID) {
        String sql = "DELETE FROM Pages WHERE PageID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức lấy trang theo ID
    public Pages getPageByID(int pageID) {
        String sql = "SELECT * FROM Pages WHERE PageID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Pages(
                    rs.getInt("PageID"),
                    rs.getString("PageName"),
                    rs.getString("PageUrl"),
                    rs.getString("Status"),
                    rs.getTimestamp("Update_At")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
   
    
}
    
    public Integer getPageIDFromUrl(String pageUrl) {
        String sql = "SELECT PageID FROM Pages WHERE PageUrl = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pageUrl);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Trả về PageID dưới dạng Integer
                    return resultSet.getInt("PageID");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception while fetching PageID for URL: " + pageUrl);
            e.printStackTrace();
        }
        
        return null; // Trả về null nếu không tìm thấy
    }
}
