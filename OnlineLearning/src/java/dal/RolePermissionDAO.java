package dal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.RolePermission;

public class RolePermissionDAO extends DBContext {

    

    // Phương thức thêm phân quyền cho một vai trò trên một trang
    public boolean addRolePermission(String role, int pageID) {
        String sql = "INSERT INTO Role_Permission (Role, PageID, Update_At) VALUES (?, ?, GETDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role);
            stmt.setInt(2, pageID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức lấy tất cả phân quyền của một vai trò
    public List<Integer> getPagesByRole(String role) {
        String sql = "SELECT PageID FROM Role_Permission WHERE Role = ?";
        List<Integer> pageIds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pageIds.add(rs.getInt("PageID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pageIds;
    }


    // Phương thức xóa toàn bộ quyền truy cập của vai trò trong bảng Role_Permission
public boolean deleteAllRolePermissions() {
    String sql = "DELETE FROM Role_Permission";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        return stmt.executeUpdate() > 0; // Trả về true nếu có ít nhất một hàng bị xóa
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Trả về false nếu có lỗi xảy ra
    }
}


    // Phương thức lấy tất cả phân quyền
    public List<RolePermission> getAllRolePermissions() {
        String sql = "SELECT * FROM Role_Permission";
        List<RolePermission> permissions = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                RolePermission permission = new RolePermission(
                    rs.getInt("RolePermissionID"),
                    rs.getString("Role"),
                    rs.getInt("PageID"),
                    rs.getTimestamp("Update_At")
                );
                permissions.add(permission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }
    
    public boolean hasPermission(String role, int pageId) {
    String sql = "SELECT COUNT(*) FROM Role_Permission WHERE Role = ? AND PageID = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, role);
        stmt.setInt(2, pageId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

  public List<String> getAllowedPagesForRole(String role) {
        List<String> allowedPages = new ArrayList<>();
        String sql = "SELECT p.PageURL FROM Pages p " +
                    "INNER JOIN RolePermission rp ON p.PageID = rp.PageID " +
                    "WHERE rp.Role = ? AND p.Status = 'Active'";
        
        try (
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                allowedPages.add(rs.getString("PageURL"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting allowed pages: " + e.getMessage());
        }
        return allowedPages;
    }
}
