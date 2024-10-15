package dal;

import java.sql.Timestamp;
import model.SubjectCategoryCount;
import java.util.ArrayList;
import java.util.List;
import model.Registrations;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Statement;

import model.SubjectCategoryCount;

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
                + "r.Note, r.UserID, r.SubjectID, r.PackageID, r.StaffID, "
                + "(CASE WHEN cs.discount != 0 AND r.Registration_Time <= c.EndDate  AND r.Registration_Time >= c.StartDate THEN c.CampaignName ELSE NULL END) AS CampaignName "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Subjects s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price p ON r.PackageID = p.PackageID "
                + "JOIN Users us ON r.StaffID = us.UserID "
                + "LEFT JOIN Campaign_Subject cs ON r.SubjectID = cs.SubjectID "
                + "LEFT JOIN Campaigns c ON cs.CampaignID = c.CampaignID "
                + "ORDER BY r.Registration_Time DESC, "
                + "(CASE WHEN r.Status = 'Processing' THEN 0 ELSE 1 END), "
                + "r.Status "
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
                registration.setCampaignName(rs.getString("CampaignName")); // Set campaign name
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
                + "WHERE r.UserID = ? AND (s.Title LIKE ? OR pp.Name LIKE ?)"; 

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

    
    public List<Registrations> getCourseByUserId(int userId) {
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
                + "WHERE r.UserID = ? AND r.Status = 'In-progress'";
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
    
    public List<Registrations> searchCourseByUserId(int userId, String searchQuery) {
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
                + "WHERE r.UserID = ? AND r.Status = 'In-progress' "
                + "AND (s.Title LIKE ? OR pp.Name LIKE ?)";

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
    public List<SubjectCategoryCount> getRegistrationAllocation() {
        List<SubjectCategoryCount> list = new ArrayList<>();
        try {
            String sql = "SELECT sc.Title AS CategoryName, COUNT(r.RegistrationID) AS RegistrationCount "
                    + "FROM Subject_Category sc "
                    + "JOIN Subjects s ON sc.Subject_CategoryID = s.Subject_CategoryID "
                    + "JOIN Registrations r ON s.SubjectID = r.SubjectID "
                    + "GROUP BY sc.Subject_CategoryID, sc.Title "
                    + "HAVING COUNT(r.RegistrationID) > 0 "
                    + "ORDER BY sc.Title";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                SubjectCategoryCount subCount = new SubjectCategoryCount();
                subCount.setCategory(rs.getString("CategoryName"));
                subCount.setCount(rs.getInt("RegistrationCount"));
                list.add(subCount);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public int getNewRegistrationByTime(java.util.Date startDate, java.util.Date endDate) {
        String sql = "SELECT COUNT(*) FROM Registrations WHERE Registration_Time BETWEEN ? AND ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTotalUpdatedSubjects: " + e.getMessage());
        }
        return 0;
    }
    public List<SubjectCategoryCount> getBestSeller(int n) {
        List<SubjectCategoryCount> list = new ArrayList<>();
        String sql = "SELECT top " + n + "count(r.SubjectID) as Amount, s.Title FROM Registrations r join Subjects s on r.SubjectID = s.SubjectID group by s.Title order by Amount desc";
        try (
                PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                SubjectCategoryCount subCount = new SubjectCategoryCount();
                subCount.setCategory(rs.getString("Title"));
                subCount.setCount(rs.getInt("Amount"));
                list.add(subCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public int getTotalRegistrationByStatus(String status, Date startDate, Date endDate){
        String sql = "select count(*) from Registrations where Status = '" + status + "' and Registration_Time BETWEEN '" + startDate + "' AND '" + endDate +"'";
        try (PreparedStatement ps = connection.prepareStatement(sql); 
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
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
    List<Registrations> courseList = registrationsDAO.getCourseByUserId(userId);

    // In thông tin các đăng ký
    for (Registrations course : courseList) {
        System.out.println("Registration ID: " + course.getRegistrationId());
        System.out.println("Subject Name: " + course.getSubjectName());
        System.out.println("Total Cost: " + course.getTotalCost());
        System.out.println("Status: " + course.getStatus());
        System.out.println("Valid From: " + course.getValidFrom());
        System.out.println("Valid To: " + course.getValidTo());
        System.out.println("Note: " + course.getNote());
        System.out.println("Staff Name: " + course.getStaffName());
        System.out.println("--------------------------------------------------");
    }
    }

    public void addNewRegistration(int userId, int subjectId, int packageId, double totalCost,
            Date registrationTime, Date validFrom, Date validTo,
            int staffId, String note) throws SQLException {
        String sql = "INSERT INTO Registrations (UserID, SubjectID, PackageID, Total_Cost, "
                + "Registration_Time, Valid_From, Valid_To, Status, StaffID, Note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, subjectId);
            preparedStatement.setInt(3, packageId);
            preparedStatement.setDouble(4, totalCost);
            preparedStatement.setTimestamp(5, new java.sql.Timestamp(registrationTime.getTime()));
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(validFrom.getTime()));
            preparedStatement.setTimestamp(7, new java.sql.Timestamp(validTo.getTime()));
            preparedStatement.setString(8, "Active"); // Set status to "Active"
            preparedStatement.setInt(9, staffId);
            preparedStatement.setString(10, note);

            preparedStatement.executeUpdate(); // Execute the insert operation
        } catch (SQLException e) {
            throw new SQLException("Error adding registration: " + e.getMessage(), e);
        }
    }
    //myRegistration DAO
    public List<Registrations> getRegistrationsByUserIdAndStatus(int userId, String status, int page, int pageSize) {
        List<Registrations> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql;

        if ("All".equals(status)) {
            sql = "SELECT r.RegistrationID, "
                    + "       u.UserID, "
                    + "       s.SubjectID, "
                    + "       s.Title AS SubjectName, "
                    + "       pp.PackageID, "
                    + "       pp.Name AS PackageName, "
                    + "       r.Registration_Time, "
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
                    + "WHERE r.UserID = ? AND r.Status != 'Cancelled' "
                    + "ORDER BY r.Registration_Time DESC "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"; // Thêm phân trang
        } else {
            sql = "SELECT r.RegistrationID, "
                    + "       u.UserID, "
                    + "       s.SubjectID, "
                    + "       s.Title AS SubjectName, "
                    + "       pp.PackageID, "
                    + "       pp.Name AS PackageName, "
                    + "       r.Registration_Time, "
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
                    + "ORDER BY r.Registration_Time DESC "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"; // Thêm phân trang
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); // Set user ID
            if (!"All".equals(status)) {
                ps.setString(2, status); // Set trạng thái
ps.setInt(3, offset); // Set giá trị offset
                ps.setInt(4, pageSize); // Set số lượng bản ghi cần lấy
            } else {
                ps.setInt(2, offset); // Set giá trị offset
                ps.setInt(3, pageSize); // Set số lượng bản ghi cần lấy
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setSubjectId(rs.getInt("SubjectID"));
                reg.setPackageId(rs.getInt("PackageID"));
                reg.setTotalCost(rs.getDouble("Total_Cost"));
                reg.setRegistrationTime(rs.getDate("Registration_Time"));
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
        String sql = "SELECT COUNT(*) FROM Registrations WHERE userId = ? AND status = ? AND Status != 'Cancelled'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            st.setString(2, status);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public List<Registrations> searchRegistrationsByUserId(int userId, String searchQuery, int page, int pageSize) {
        List<Registrations> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Registration_Time, "
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
                + "LEFT JOIN Users staff ON r.StaffID = staff.UserID  "
+ "WHERE r.UserID = ? AND (s.Title LIKE ? OR pp.Name LIKE ?)AND r.Status != 'Cancelled' "
                + "ORDER BY r.Registration_Time DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"; // Thêm phân trang

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); // Set user ID
            ps.setString(2, "%" + searchQuery + "%"); // Set từ khóa tìm kiếm
            ps.setString(3, "%" + searchQuery + "%"); // Set từ khóa tìm kiếm
            ps.setInt(4, offset); // Set giá trị offset
            ps.setInt(5, pageSize); // Set số lượng bản ghi cần lấy
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setSubjectId(rs.getInt("SubjectID"));
                reg.setPackageId(rs.getInt("PackageID"));
                reg.setTotalCost(rs.getDouble("Total_Cost"));
                reg.setRegistrationTime(rs.getDate("Registration_Time"));
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

    public int getTotalSearchResultsByUserId(int userId, String searchQuery) {
        String sql = "SELECT COUNT(*) FROM Registrations r "
                + "JOIN Subjects s ON r.subjectId = s.subjectId "
                + "WHERE r.userId = ? AND (s.subjectName LIKE ? OR r.status LIKE ?) AND r.Status != 'Cancelled'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            st.setString(2, "%" + searchQuery + "%");
            st.setString(3, "%" + searchQuery + "%");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public List<Registrations> getRegistrationsByUserId(int userId, int page, int pageSize) {
        List<Registrations> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Registration_Time, "
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
                + "WHERE r.UserID = ? AND r.Status != 'Cancelled'"
                + "ORDER BY r.Registration_Time DESC "
                + "OFFSET ? ROWS "
                + "FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setRegistrationId(rs.getInt("RegistrationID"));
                reg.setSubjectId(rs.getInt("SubjectID"));
                reg.setPackageId(rs.getInt("PackageID"));
                reg.setTotalCost(rs.getDouble("Total_Cost"));
                reg.setRegistrationTime(rs.getDate("Registration_Time"));
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

    public int getTotalRegistrationsByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM Registrations WHERE userId = ? AND Status != 'Cancelled'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    //Cancel summitted course
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
//End myRegistrationDAO

//myCourseDAO
    public List<Registrations> getCourseByUserId(int userId, int page, int pageSize) {
List<Registrations> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Registration_Time, "
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
                + "WHERE r.UserID = ? AND r.Status = 'In-progress'"
                + "ORDER BY r.Registration_Time DESC "
                + "OFFSET ? ROWS "
                + "FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setRegistrationId(rs.getInt("RegistrationID"));
                reg.setSubjectId(rs.getInt("SubjectID"));
                reg.setPackageId(rs.getInt("PackageID"));
                reg.setTotalCost(rs.getDouble("Total_Cost"));
                reg.setRegistrationTime(rs.getDate("Registration_Time"));
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

    public List<Registrations> searchCourseByUserId(int userId, String searchQuery, int page, int pageSize) {
        List<Registrations> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Registration_Time, "
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
                + "LEFT JOIN Users staff ON r.StaffID = staff.UserID  "
                + "WHERE r.UserID = ? AND (s.Title LIKE ? OR pp.Name LIKE ?)AND r.Status = 'In-progress' "
                + "ORDER BY r.Registration_Time DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"; // Thêm phân trang

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); // Set user ID
            ps.setString(2, "%" + searchQuery + "%"); // Set từ khóa tìm kiếm
            ps.setString(3, "%" + searchQuery + "%"); // Set từ khóa tìm kiếm
            ps.setInt(4, offset); // Set giá trị offset
            ps.setInt(5, pageSize); // Set số lượng bản ghi cần lấy
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Registrations reg = new Registrations();
                reg.setSubjectId(rs.getInt("SubjectID"));
                reg.setPackageId(rs.getInt("PackageID"));
                reg.setTotalCost(rs.getDouble("Total_Cost"));
                reg.setRegistrationTime(rs.getDate("Registration_Time"));
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
    
    public int getTotalSearchResultsCourseByUserId(int userId, String searchQuery) {
        String sql = "SELECT COUNT(*) FROM Registrations r "
                + "JOIN Subjects s ON r.subjectId = s.subjectId "
                + "WHERE r.userId = ? AND (s.subjectName LIKE ? OR r.status LIKE ?) AND r.Status = 'In-progress'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            st.setString(2, "%" + searchQuery + "%");
            st.setString(3, "%" + searchQuery + "%");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public int getTotalCourseByUserId(int userId) {
String sql = "SELECT COUNT(*) FROM Registrations WHERE userId = ? AND Status = 'In-progress'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }
//end myCourseDAO
}
