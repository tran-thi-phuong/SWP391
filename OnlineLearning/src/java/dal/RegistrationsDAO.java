package dal;

import java.util.ArrayList;
import java.util.List;
import model.Registrations;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Connection;
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
                + "WHERE r.UserID = ? AND r.Status != 'Cancelled'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setRegistrationId(rs.getInt("RegistrationID"));
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
                    + "WHERE r.UserID = ? AND r.Status != 'Cancelled'"; // Không lọc theo trạng thái
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

    public boolean updateStatusToCancelled(int registrationId) {
        String sql = "UPDATE Registrations SET Status = 'Cancelled' WHERE RegistrationID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, registrationId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất 1 hàng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        RegistrationsDAO registrationsDAO = new RegistrationsDAO();

        // ID của đăng ký cần hủy (có thể thay đổi giá trị này theo nhu cầu)
        int registrationId = 3; // Ví dụ với RegistrationID = 1

        // Gọi phương thức hủy đăng ký và kiểm tra kết quả
        boolean isCancelled = registrationsDAO.updateStatusToCancelled(registrationId);

        // Kiểm tra nếu trạng thái đã được cập nhật
        if (isCancelled) {
            System.out.println("Registration with ID = " + registrationId + " has been cancelled successfully.");
        } else {
            System.out.println("Failed to cancel registration with ID = " + registrationId + ".");
        }
        int userId = 1; // Thay đổi userId nếu cần
    List<Registrations> registrationsList = registrationsDAO.getRegistrationsByUserId(userId);

    // In thông tin các đăng ký
    for (Registrations registration : registrationsList) {
        System.out.println("Registration ID: " + registration.getRegistrationId());
        System.out.println("Subject Name: " + registration.getSubjectName());
        System.out.println("Total Cost: " + registration.getTotalCost());
        System.out.println("Status: " + registration.getStatus());
        System.out.println("Valid From: " + registration.getValidFrom());
        System.out.println("Valid To: " + registration.getValidTo());
        System.out.println("Note: " + registration.getNote());
        System.out.println("Staff Name: " + registration.getStaffName());
        System.out.println("--------------------------------------------------");
    }
    }
}
