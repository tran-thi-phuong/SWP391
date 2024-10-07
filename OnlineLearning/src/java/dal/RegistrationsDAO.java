package dal;

import java.util.ArrayList;
import java.util.List;
import model.Registrations;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;

public class RegistrationsDAO extends DBContext {

    public List<Registrations> getAllRegistrations() {
        List<Registrations> registrations = new ArrayList<>();
        String sql = "SELECT r.RegistrationID, u.Email, r.Registration_Time, s.Title AS Subject, "
                + "p.Name AS Package, r.Total_Cost, r.Status, r.Valid_From, r.Valid_To, "
                + "r.Note, r.UserID, r.SubjectID, r.PackageID, r.StaffID, c.CampaignName "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price p ON r.PackageID = p.PackageID "
                + "JOIN Users us ON r.StaffID = us.UserID "
                + "LEFT JOIN Campaign_Subject cs ON r.SubjectID = cs.SubjectID "
                + "LEFT JOIN Campaigns c ON cs.CampaignID = c.CampaignID";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Registrations registration = new Registrations();
                registration.setRegistrationId(rs.getInt("RegistrationID"));
                registration.setUserId(rs.getInt("UserID"));
                registration.setRegistrationTime(rs.getTimestamp("Registration_Time"));
                registration.setSubjectId(rs.getInt("SubjectID"));
                registration.setPackageId(rs.getInt("PackageID"));
                registration.setTotalCost(rs.getDouble("Total_Cost"));
                registration.setStatus(rs.getString("Status"));
                registration.setValidFrom(rs.getDate("Valid_From"));
                registration.setValidTo(rs.getDate("Valid_To"));
                registration.setStaffId(rs.getInt("StaffID"));
                registration.setNote(rs.getString("Note"));
                registration.setCampaignName(rs.getString("CampaignName"));
                registrations.add(registration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrations;
    }

    public int getTotalRegistrations() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM Registrations";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<Registrations> getRegistrationsByPage(int currentPage, int pageSize) {
        List<Registrations> registrations = new ArrayList<>();
        String sql = "SELECT r.RegistrationID, u.Email, r.Registration_Time, s.Title AS Subject, "
                + "p.Name AS Package, r.Total_Cost, r.Status, r.Valid_From, r.Valid_To, "
                + "r.Note, r.UserID, r.SubjectID, r.PackageID, r.StaffID, c.CampaignName "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price p ON r.PackageID = p.PackageID "
                + "JOIN Users us ON r.StaffID = us.UserID "
                + "LEFT JOIN Campaign_Subject cs ON r.SubjectID = cs.SubjectID "
                + "LEFT JOIN Campaigns c ON cs.CampaignID = c.CampaignID "
                + "ORDER BY r.Registration_Time "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, (currentPage - 1) * pageSize);
            ps.setInt(2, pageSize);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Registrations registration = new Registrations();
                registration.setRegistrationId(rs.getInt("RegistrationID"));
                registration.setUserId(rs.getInt("UserID"));
                registration.setRegistrationTime(rs.getTimestamp("Registration_Time"));
                registration.setSubjectId(rs.getInt("SubjectID"));
                registration.setPackageId(rs.getInt("PackageID"));
                registration.setTotalCost(rs.getDouble("Total_Cost"));
                registration.setStatus(rs.getString("Status"));
                registration.setValidFrom(rs.getDate("Valid_From"));
                registration.setValidTo(rs.getDate("Valid_To"));
                registration.setStaffId(rs.getInt("StaffID"));
                registration.setNote(rs.getString("Note"));
                registration.setCampaignName(rs.getString("CampaignName"));
                registrations.add(registration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrations;
    }

    public Registrations getRegistrationById(int id) {
        Registrations registration = null;
        String sql = "SELECT r.*, c.CampaignName "
                + "FROM Registrations r "
                + "LEFT JOIN Campaign_Subject cs ON r.SubjectID = cs.SubjectID "
                + "LEFT JOIN Campaigns c ON cs.CampaignID = c.CampaignID "
                + "WHERE r.RegistrationID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                registration = new Registrations();
                registration.setRegistrationId(rs.getInt("RegistrationID"));
                registration.setUserId(rs.getInt("UserID"));
                registration.setSubjectId(rs.getInt("SubjectID"));
                registration.setPackageId(rs.getInt("PackageID"));
                registration.setTotalCost(rs.getDouble("Total_Cost"));
                registration.setRegistrationTime(rs.getTimestamp("Registration_Time"));
                registration.setValidFrom(rs.getDate("Valid_From"));
                registration.setValidTo(rs.getDate("Valid_To"));
                registration.setStatus(rs.getString("Status"));
                registration.setStaffId(rs.getInt("StaffID"));
                registration.setNote(rs.getString("Note"));

                // Assuming there's a setter for campaign name in Registrations class
                registration.setCampaignName(rs.getString("CampaignName")); // Add campaignName field in your Registrations class
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return registration;
    }

    //Add a registration
    public void addRegistration(int userId, int subjectId, int packageId, double totalCost) throws SQLException {
        String sql = "INSERT INTO Registrations (UserID, SubjectID, PackageID, Total_Cost, Registration_Time, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, packageId);
            pstmt.setDouble(4, totalCost);
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // Current date and time
            pstmt.setString(6, "Processing");

            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
            throw e; // Re-throw exception after logging
        }
    }

    public List<Registrations> getRegistrationsByUserId(int userId) {
        List<Registrations> list = new ArrayList<>();
        String sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Total_Cost, "
                + "       r.Status, "
                + "       r.Valid_From, "
                + "       r.Valid_To, "
                + "       staff.UserID AS StaffID, "
                + "       staff.Name AS StaffName, "
                + "       r.Note "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price pp ON r.PackageID = pp.PackageID "
                + "LEFT JOIN Users staff ON r.StaffID = staff.UserID "
                + "WHERE r.UserID = ?"; 
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); 
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setSubjectId(rs.getInt("SubjectID"));
                reg.setPackageId(rs.getInt("PackageID"));
                reg.setTotalCost(rs.getDouble("Total_Cost"));
                reg.setStatus(rs.getString("Status"));
                reg.setValidFrom(rs.getDate("Valid_From"));
                reg.setValidTo(rs.getDate("Valid_To"));
                reg.setNote(rs.getString("Note"));
                reg.setSubjectName(rs.getString("SubjectName"));
                reg.setStaffName(rs.getString("StaffName"));
                list.add(reg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean cancelRegistration(int registrationId) {
        // SQL query to update the registration status to 'cancelled'
        String sql = "UPDATE registrations SET status = 'cancelled' WHERE registrationId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, registrationId); // Set the registration ID parameter
            int rowsAffected = stmt.executeUpdate(); // Execute the update
            return rowsAffected > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return false; // Return false if there was an error
        }
    }

    public List<Registrations> searchRegistrationsByUserId(int userId, String searchQuery) {
        List<Registrations> list = new ArrayList<>();
        String sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Total_Cost, "
                + "       r.Status, "
                + "       r.Valid_From, "
                + "       r.Valid_To, "
                + "       staff.UserID AS StaffID, "
                + "       staff.Name AS StaffName, "
                + "       r.Note "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price pp ON r.PackageID = pp.PackageID "
                + "LEFT JOIN Users staff ON r.StaffID = staff.UserID "
                + "WHERE r.UserID = ? AND (s.Title LIKE ? OR pp.Name LIKE ?)"; // Tìm kiếm theo tiêu đề môn học và tên gói

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); // Set user ID trong truy vấn
            ps.setString(2, "%" + searchQuery + "%"); // Tìm kiếm theo tên môn học
            ps.setString(3, "%" + searchQuery + "%"); // Tìm kiếm theo tên gói
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setSubjectId(rs.getInt("SubjectID"));
                reg.setPackageId(rs.getInt("PackageID"));
                reg.setTotalCost(rs.getDouble("Total_Cost"));
                reg.setStatus(rs.getString("Status"));
                reg.setValidFrom(rs.getDate("Valid_From"));
                reg.setValidTo(rs.getDate("Valid_To"));
                reg.setNote(rs.getString("Note"));
                reg.setSubjectName(rs.getString("SubjectName"));
                reg.setStaffName(rs.getString("StaffName"));
                list.add(reg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Registrations> getRegistrationsByUserIdAndStatus(int userId, String status) {
        List<Registrations> list = new ArrayList<>();
        String sql;

        if ("All".equals(status)) {
            // Lấy tất cả đăng ký của người dùng
            sql = "SELECT r.RegistrationID, "
                    + "       u.UserID, "
                    + "       s.SubjectID, "
                    + "       s.Title AS SubjectName, "
                    + "       pp.PackageID, "
                    + "       pp.Name AS PackageName, "
                    + "       r.Total_Cost, "
                    + "       r.Status, "
                    + "       r.Valid_From, "
                    + "       r.Valid_To, "
                    + "       staff.UserID AS StaffID, "
                    + "       staff.Name AS StaffName, "
                    + "       r.Note "
                    + "FROM Registrations r "
                    + "JOIN Users u ON r.UserID = u.UserID "
                    + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                    + "JOIN Package_Price pp ON r.PackageID = pp.PackageID "
                    + "LEFT JOIN Users staff ON r.StaffID = staff.UserID "
                    + "WHERE r.UserID = ?"; // Không lọc theo trạng thái
        } else {
            // Lấy đăng ký theo trạng thái cụ thể
            sql = "SELECT r.RegistrationID, "
                    + "       u.UserID, "
                    + "       s.SubjectID, "
                    + "       s.Title AS SubjectName, "
                    + "       pp.PackageID, "
                    + "       pp.Name AS PackageName, "
                    + "       r.Total_Cost, "
                    + "       r.Status, "
                    + "       r.Valid_From, "
                    + "       r.Valid_To, "
                    + "       staff.UserID AS StaffID, "
                    + "       staff.Name AS StaffName, "
                    + "       r.Note "
                    + "FROM Registrations r "
                    + "JOIN Users u ON r.UserID = u.UserID "
                    + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                    + "JOIN Package_Price pp ON r.PackageID = pp.PackageID "
                    + "LEFT JOIN Users staff ON r.StaffID = staff.UserID "
                    + "WHERE r.UserID = ? AND r.Status = ?"; // Lọc theo trạng thái
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); // Set user ID trong truy vấn
            if (!"All".equals(status)) {
                ps.setString(2, status); // Set trạng thái trong truy vấn
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setSubjectId(rs.getInt("SubjectID"));
                reg.setPackageId(rs.getInt("PackageID"));
                reg.setTotalCost(rs.getDouble("Total_Cost"));
                reg.setStatus(rs.getString("Status"));
                reg.setValidFrom(rs.getDate("Valid_From"));
                reg.setValidTo(rs.getDate("Valid_To"));
                reg.setNote(rs.getString("Note"));
                reg.setSubjectName(rs.getString("SubjectName"));
                reg.setStaffName(rs.getString("StaffName"));
                list.add(reg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Registrations> getRegistrationsByUserIdAndStatusWithPagination(int userId, String status, int page, int pageSize) {
    List<Registrations> list = new ArrayList<>();
    String sql;

    // Tính toán chỉ số bắt đầu
    int start = (page - 1) * pageSize;

    if ("All".equals(status)) {
        // Lấy tất cả đăng ký của người dùng với phân trang
        sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Total_Cost, "
                + "       r.Status, "
                + "       r.Valid_From, "
                + "       r.Valid_To, "
                + "       staff.UserID AS StaffID, "
                + "       staff.Name AS StaffName, "
                + "       r.Note "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price pp ON r.PackageID = pp.PackageID "
                + "LEFT JOIN Users staff ON r.StaffID = staff.UserID "
                + "WHERE r.UserID = ? "
                + "LIMIT ?, ?"; // Thêm LIMIT cho phân trang
    } else {
        // Lấy đăng ký theo trạng thái cụ thể với phân trang
        sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Total_Cost, "
                + "       r.Status, "
                + "       r.Valid_From, "
                + "       r.Valid_To, "
                + "       staff.UserID AS StaffID, "
                + "       staff.Name AS StaffName, "
                + "       r.Note "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price pp ON r.PackageID = pp.PackageID "
                + "LEFT JOIN Users staff ON r.StaffID = staff.UserID "
                + "WHERE r.UserID = ? AND r.Status = ? "
                + "LIMIT ?, ?"; // Thêm LIMIT cho phân trang
    }

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, userId); // Set user ID
        if (!"All".equals(status)) {
            ps.setString(2, status); // Set trạng thái
            ps.setInt(3, start); // Set chỉ số bắt đầu
            ps.setInt(4, pageSize); // Set số lượng mục
        } else {
            ps.setInt(2, start); // Set chỉ số bắt đầu
            ps.setInt(3, pageSize); // Set số lượng mục
        }
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Registrations reg = new Registrations();
            reg.setSubjectId(rs.getInt("SubjectID"));
            reg.setPackageId(rs.getInt("PackageID"));
            reg.setTotalCost(rs.getDouble("Total_Cost"));
            reg.setStatus(rs.getString("Status"));
            reg.setValidFrom(rs.getDate("Valid_From"));
            reg.setValidTo(rs.getDate("Valid_To"));
            reg.setNote(rs.getString("Note"));
            reg.setSubjectName(rs.getString("SubjectName"));
            reg.setStaffName(rs.getString("StaffName"));
            list.add(reg);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
    
    public int getTotalRegistrationsByUserIdAndStatus(int userId, String status) {
    int total = 0;
    String sql;

    if ("All".equals(status)) {
        sql = "SELECT COUNT(*) FROM Registrations WHERE UserID = ?";
    } else {
        sql = "SELECT COUNT(*) FROM Registrations WHERE UserID = ? AND Status = ?";
    }

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, userId);
        if (!"All".equals(status)) {
            ps.setString(2, status);
        }
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            total = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return total;
}



    public static void main(String[] args) {
        // Create DAO object
        RegistrationsDAO registrationsDAO = new RegistrationsDAO();

        // User ID to check (you can change this value as needed)
        int userId = 1; // Example with user ID = 1

        // Call getRegistrationsByUserId and get results
        List<Registrations> registrations = registrationsDAO.getRegistrationsByUserId(userId);

        // Check if data is returned
        if (registrations.isEmpty()) {
            System.out.println("No registrations found for user ID = " + userId);
        } else {
            // Print user registrations
            System.out.println("User registrations for user ID = " + userId + ":");
            for (Registrations registration : registrations) {
                System.out.println(
                        " Subject Name: " + registration.getSubjectName()
                        + ", Total Cost: " + registration.getTotalCost()
                        + ", Status: " + registration.getStatus()
                        + ", Valid From: " + registration.getValidFrom()
                        + ", Valid To: " + registration.getValidTo()
                        + ", Staff Name: " + registration.getStaffName()
                        + ", Note: " + registration.getNote());
            }
        }

        int registrationIdToCancel = registrations.get(5).getRegistrationId(); // Assuming your model has a method getRegistrationId()
        boolean isCancelled = registrationsDAO.cancelRegistration(registrationIdToCancel); // Call the cancel method

        // Check if the cancellation was successful
        if (isCancelled) {
            System.out.println("Registration with ID " + registrationIdToCancel + " has been cancelled successfully.");
        } else {
            System.out.println("Failed to cancel registration with ID " + registrationIdToCancel + ".");
        }
    }
}
