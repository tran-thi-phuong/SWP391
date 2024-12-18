package dal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Registrations;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import model.SubjectCategoryCount;

public class RegistrationsDAO extends DBContext {

    // Method to count total registration
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

    // Method to get all registration
    public List<Registrations> getAllRegistrations() {
        List<Registrations> registrations = new ArrayList<>();
        String sql = "SELECT r.RegistrationID, u.Email, r.Registration_Time, s.Title AS Subject, "
                + "p.Name AS Package, r.Total_Cost, r.Status, r.Valid_From, r.Valid_To, "
                + "r.Note, r.UserID, r.SubjectID, r.PackageID, r.StaffID, c.CampaignName "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Course s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price p ON r.PackageID = p.PackageID "
                + "JOIN Users us ON r.StaffID = us.UserID "
                + "LEFT JOIN Campaign_Course cs ON r.SubjectID = cs.SubjectID "
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

    /**
     * 
     * @param email
     * @param title
     * @param campaignId
     * @param registrationTimeFrom
     * @param validTo
     * @param status
     * @return 
     */
    // Method to count registration after filter
    public int getTotalRegistrationsCount(String email, String title, String campaignId, Date registrationTimeFrom, Date validTo, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Campaign_Course cs ON r.SubjectID = cs.SubjectID "
                + "JOIN Course s ON cs.SubjectID = s.SubjectID "
                + "JOIN Campaigns c ON cs.CampaignID = c.CampaignID WHERE 1=1");

        // Build query conditions based on provided parameters
        if (email != null && !email.isEmpty()) {
            sql.append(" AND u.Email LIKE ?");
        }
        if (title != null && !title.isEmpty()) {
            sql.append(" AND s.Title LIKE ?"); 
        }

        if (campaignId != null && !campaignId.isEmpty()) {
            sql.append(" AND c.CampaignID = ?");
        }
        if (registrationTimeFrom != null) {
            sql.append(" AND r.Registration_Time >= ?");
        }
        if (validTo != null) {
            sql.append(" OR r.Valid_To <= ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.Status = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (email != null && !email.isEmpty()) {
                stmt.setString(paramIndex++, "%" + email + "%");
            }
            if (title != null && !title.isEmpty()) {
                stmt.setString(paramIndex++, "%" + title + "%");
            }
            if (campaignId != null && !campaignId.isEmpty()) {
                stmt.setInt(paramIndex++, Integer.parseInt(campaignId));
            }
            if (registrationTimeFrom != null) {
                // Convert java.util.Date to java.sql.Timestamp
                stmt.setTimestamp(paramIndex++, new Timestamp(registrationTimeFrom.getTime()));
            }
            if (validTo != null) {
                // Convert java.util.Date to java.sql.Timestamp
                stmt.setTimestamp(paramIndex++, new Timestamp(validTo.getTime()));
            }
            if (status != null && !status.isEmpty()) {
                stmt.setString(paramIndex++, "%" + status + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0; // Return 0 if an error occurs
    }
    
    /**
     * 
     * @param currentPage
     * @param pageSize
     * @return 
     */
    //Method to paging list

    public List<Registrations> getRegistrationsByPage(int currentPage, int pageSize) {
        List<Registrations> registrations = new ArrayList<>();
        String sql = "SELECT r.RegistrationID, u.Email, r.Registration_Time, s.Title AS Subject, "
                + "p.Name AS Package, r.Total_Cost, r.Status, r.Valid_From, r.Valid_To, "
                + "r.Note, r.UserID, r.SubjectID, r.PackageID, r.StaffID, "
                + "(CASE WHEN cs.discount != 0 AND r.Registration_Time <= c.EndDate AND r.Registration_Time >= c.StartDate THEN c.CampaignName ELSE NULL END) AS CampaignName "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Course s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price p ON r.PackageID = p.PackageID "
                + "JOIN Users us ON r.StaffID = us.UserID "
                + "LEFT JOIN Campaign_Course cs ON r.SubjectID = cs.SubjectID "
                + "LEFT JOIN Campaigns c ON cs.CampaignID = c.CampaignID "
                + "WHERE 1=1 "
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
                registration.setCampaignName(rs.getString("CampaignName"));
                registrations.add(registration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrations;
    }

    /**
     *
     * @param currentPage
     * @param pageSize
     * @param email
     * @param title
     * @param campaignId
     * @param registrationTime
     * @param validTo
     * @param status
     * @return
     */
    // Method to filter and search
    public List<Registrations> filterRegistration(int currentPage, int pageSize, String email,
            String title, String campaignId,
            Date registrationTime, Date validTo,
            String status) {
        List<Registrations> registrations = new ArrayList<>();
        int offset = (currentPage - 1) * pageSize;

        StringBuilder sql = new StringBuilder("SELECT r.RegistrationID, u.Email, r.Registration_Time, s.Title AS Subject, "
                + "p.Name AS Package, r.Total_Cost, r.Status, r.Valid_From, r.Valid_To, "
                + "r.Note, r.UserID, r.SubjectID, r.PackageID, r.StaffID, "
                + "(CASE WHEN cs.discount != 0 AND r.Registration_Time <= c.EndDate AND r.Registration_Time >= c.StartDate THEN c.CampaignName ELSE NULL END) AS CampaignName "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Course s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price p ON r.PackageID = p.PackageID "
                + "LEFT JOIN Users us ON r.StaffID = us.UserID "
                + "LEFT JOIN Campaign_Course cs ON r.SubjectID = cs.SubjectID "
                + "LEFT JOIN Campaigns c ON cs.CampaignID = c.CampaignID "
                + "WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (email != null && !email.isEmpty()) {
            sql.append(" AND u.Email LIKE ?");
            params.add("%" + email + "%");
        }
        if (title != null && !title.isEmpty()) {
            sql.append(" AND s.Title LIKE ?");
            params.add("%" + title + "%");
        }

        if (campaignId != null && !campaignId.isEmpty()) {
            sql.append(" AND c.CampaignID = ?");
            params.add(Integer.valueOf(campaignId));
        }
        if (registrationTime != null) {
            sql.append(" AND r.Registration_Time >= ?");
            params.add(new Timestamp(registrationTime.getTime()));
        }
        if (validTo != null) {
            sql.append(" OR r.Valid_To <= ?");
            params.add(new Timestamp(validTo.getTime()));
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.Status = ?");
            params.add(status);
        }

        sql.append(" ORDER BY r.Registration_Time DESC, "
                + "(CASE WHEN r.Status = 'Processing' THEN 0 ELSE 1 END), "
                + "r.Status "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }
            stmt.setInt(paramIndex++, offset);
            stmt.setInt(paramIndex, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrations;
    }
/**
 * 
 * @param id
 * @return 
 */
    //Method to get registration by id
    public Registrations getRegistrationById(int id) {
        Registrations registration = null;
        String sql = "SELECT r.*, c.CampaignName "
                + "FROM Registrations r "
                + "LEFT JOIN Campaign_Course cs ON r.SubjectID = cs.SubjectID "
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

    //Mthod to add a registration
    /**
     * 
     * @param userId
     * @param subjectId
     * @param packageId
     * @param totalCost
     * @throws SQLException 
     */
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
                        throw e; 
        }
    }

//Method for customer she their register
    /**
     * 
     * @param userId
     * @param status
     * @param page
     * @param pageSize
     * @return 
     */
    public List<Registrations> getRegistrationsByUserIdAndStatus(int userId, String status, int page, int pageSize) {
        List<Registrations> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql;

        if ("All".equals(status)) {
            sql = """
                  SELECT r.RegistrationID,
                         u.UserID, 
                         s.SubjectID, 
                        s.Title AS SubjectName, 
                        s.Thumbnail
                         pp.PackageID, "
                         pp.Name AS PackageName, 
                         r.Registration_Time, 
                         r.Total_Cost, 
                          r.Status, 
                          r.Valid_From, 
                         r.Valid_To, 
                          staff.UserID AS StaffID, 
                          staff.Name AS StaffName, 
                          r.Note 
                    FROM Registrations r 
                    JOIN Users u ON r.UserID = u.UserID 
                    JOIN Course s ON r.SubjectID = s.SubjectID 
                    JOIN Package_Price pp ON r.PackageID = pp.PackageID 
                    LEFT JOIN Users staff ON r.StaffID = staff.UserID 
                    WHERE r.UserID = ? AND r.Status != 'Cancelled' 
                    ORDER BY r.Registration_Time DESC 
                    OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                  """; // Thêm phân trang
        } else {
            sql = "SELECT r.RegistrationID, "
                    + "       u.UserID, "
                    + "       s.SubjectID, "
                    + "       s.Title AS SubjectName, "
                    + "       s.Thumbnail,"
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
                    + "JOIN Course s ON r.SubjectID = s.SubjectID "
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
                reg.setThumbnail(rs.getString("Thumbnail"));
                reg.setStaffName(rs.getString("StaffName"));
                list.add(reg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
/**
 * 
 * @param userId
 * @param status
 * @return 
 */
    // Method to count register of customer
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
/**
 * 
 * @param userId
 * @param searchQuery
 * @param page
 * @param pageSize
 * @return 
 */
    // Method to search registration by user id
    public List<Registrations> searchRegistrationsByUserId(int userId, String searchQuery, int page, int pageSize) {
        List<Registrations> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       s.Thumbnail,"
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
                + "JOIN Course s ON r.SubjectID = s.SubjectID "
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
                reg.setThumbnail(rs.getString("Thumbnail"));
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
                + "JOIN Course s ON r.subjectId = s.subjectId "
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
        String sql = """
                     SELECT r.RegistrationID, 
                            u.UserID, 
                            s.SubjectID, 
                            s.Title AS SubjectName, 
                            s.Thumbnail,  
                            pp.PackageID, 
                            pp.Name AS PackageName, 
                            r.Registration_Time, 
                            r.Total_Cost, 
                            r.Status, 
                            r.Valid_From, 
                            r.Valid_To, 
                            staff.UserID AS StaffID, 
                            staff.Name AS StaffName, 
                            r.Note 
                     FROM Registrations r 
                     JOIN Users u ON r.UserID = u.UserID 
                     JOIN Course s ON r.SubjectID = s.SubjectID 
                     JOIN Package_Price pp ON r.PackageID = pp.PackageID 
                     LEFT JOIN Users staff ON r.StaffID = staff.UserID 
                     WHERE r.UserID = ? 
                     AND r.Status != 'Cancelled' 
                     ORDER BY r.Registration_Time DESC 
                     OFFSET ? ROWS 
                     FETCH NEXT ? ROWS ONLY;
                     """;
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
                reg.setThumbnail(rs.getString("Thumbnail"));
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
                + "       s.Thumbnail, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Registration_Time, "
                + "       r.Total_Cost, "
                + "       r.Status, "
                + "       r.Valid_From, "
                + "       r.Valid_To, "
                + "       staff.UserID AS StaffID, "
                + "       staff.Name AS StaffName, "
                + "       r.Note ,"
                + "       COALESCE(cs.Progress, 0) AS Progress "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Course s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price pp ON r.PackageID = pp.PackageID "
                + "LEFT JOIN Users staff ON r.StaffID = staff.UserID "
                + "LEFT JOIN Customer_Course cs ON r.SubjectID = cs.SubjectID AND r.UserID = cs.UserID " 
                + "WHERE r.UserID = ? AND r.Status = 'Active'"
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
                reg.setThumbnail(rs.getString("Thumbnail"));
                reg.setStaffName(rs.getString("StaffName"));
                reg.setProgress(rs.getDouble("Progress"));
                list.add(reg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
/**
 * 
 * @param userId
 * @param searchQuery
 * @param page
 * @param pageSize
 * @return 
 */
    public List<Registrations> searchCourseByUserId(int userId, String searchQuery, int page, int pageSize) {
        List<Registrations> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT r.RegistrationID, "
                + "       u.UserID, "
                + "       s.SubjectID, "
                + "       s.Title AS SubjectName, "
                + "       s.Thumbnail, "
                + "       pp.PackageID, "
                + "       pp.Name AS PackageName, "
                + "       r.Registration_Time, "
                + "       r.Total_Cost, "
                + "       r.Status, "
                + "       r.Valid_From, "
                + "       r.Valid_To, "
                + "       staff.UserID AS StaffID, "
                + "       staff.Name AS StaffName, "
                + "       r.Note ,"
                + "       COALESCE(cs.Progress, 0) AS Progress "
                + "FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "JOIN Course s ON r.SubjectID = s.SubjectID "
                + "JOIN Package_Price pp ON r.PackageID = pp.PackageID "
                + "LEFT JOIN Users staff ON r.StaffID = staff.UserID  "
                + "LEFT JOIN Customer_Course cs ON r.SubjectID = cs.SubjectID AND r.UserID = cs.UserID " 
                + "WHERE r.UserID = ? AND (s.Title LIKE ? OR pp.Name LIKE ?)AND r.Status = 'Active' "
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
                reg.setThumbnail(rs.getString("Thumbnail"));
                reg.setStaffName(rs.getString("StaffName"));
                reg.setProgress(rs.getDouble("Progress"));
                list.add(reg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 
     * @param userId
     * @param searchQuery
     * @return 
     */
    public int getTotalSearchResultsCourseByUserId(int userId, String searchQuery) {
        String sql = "SELECT COUNT(*) FROM Registrations r "
                + "JOIN Course s ON r.subjectId = s.subjectId "
                + "WHERE r.userId = ? AND (s.subjectName LIKE ? OR r.status LIKE ?) AND r.Status = 'Active'";
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

    /**
     * 
     * @param userId
     * @return 
     */
    public int getTotalCourseByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM Registrations WHERE userId = ? AND Status = 'Active'";
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
/**
 * 
 * @return 
 */
    public List<SubjectCategoryCount> getRegistrationAllocation() {
        List<SubjectCategoryCount> list = new ArrayList<>();
        try {
            String sql = "SELECT sc.Title AS CategoryName, COUNT(r.RegistrationID) AS RegistrationCount "
                    + "FROM Subject_Category sc "
                    + "JOIN Course s ON sc.Subject_CategoryID = s.Subject_CategoryID "
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
/**
 * 
 * @param startDate
 * @param endDate
 * @return 
 */
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
/**
 * 
 * @param n
 * @return 
 */
    public List<SubjectCategoryCount> getBestSeller(int n) {
        List<SubjectCategoryCount> list = new ArrayList<>();
        String sql = "SELECT top " + n + "count(r.SubjectID) as Amount, s.Title FROM Registrations r join Course s on r.SubjectID = s.SubjectID group by s.Title order by Amount desc";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
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
/**
 * 
 * @param status
 * @param startDate
 * @param endDate
 * @return 
 */
    public int getTotalRegistrationByStatus(String status, Date startDate, Date endDate) {
        String sql = "select count(*) from Registrations where Status = '" + status + "' and Registration_Time BETWEEN '" + startDate + "' AND '" + endDate + "'";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
/**
 * 
 * @param registration
 * @return 
 */
public boolean updateRegistration(Registrations registration) {
    // Validate date range
    if (registration.getValidFrom().after(registration.getValidTo())) {
        return false; // Invalid date range
    }

    // SQL update statement
    String sql = "UPDATE Registrations SET Valid_From = ?, Valid_To = ?, Status = ?, StaffID = ? WHERE RegistrationID = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        // Set parameters in the PreparedStatement
        ps.setDate(1, new java.sql.Date(registration.getValidFrom().getTime()));
        ps.setDate(2, new java.sql.Date(registration.getValidTo().getTime()));
        ps.setString(3, registration.getStatus());
        ps.setInt(4, registration.getStaffId()); // Staff ID should be set here
        ps.setInt(5, registration.getRegistrationId()); // Registration ID should be set here

        // Execute the update and return the result
        int rowsUpdated = ps.executeUpdate();
        return rowsUpdated > 0; // Return true if at least one row was updated

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false; // Return false if update fails or exception occurs
}


    /**
     * 
     * @param userId
     * @param subjectId
     * @param packageId
     * @param totalCost
     * @param registrationTime
     * @param validFrom
     * @param validTo
     * @param staffId
     * @param note
     * @throws SQLException 
     */
    // Method to create new a registration
    public void addNewRegistration(int userId, int subjectId, int packageId, double totalCost,
            Date registrationTime, Date validFrom, Date validTo,
            int staffId, String note) throws SQLException {
        if (registrationTime == null) {
            throw new SQLException("Date parameters cannot be null");
        }
        if (validTo.before(validFrom)) {
            throw new SQLException("Valid to date cannot be before valid from date");
        }
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
/**
 * 
 * @param registrationId
 * @return 
 */
    // Method to get customer email by registration id
    public String getCustomerEmailByRegistrationId(String registrationId) {
        String email = null;
        String sql = "SELECT u.email FROM Registrations r "
                + "JOIN Users u ON r.UserID = u.UserID "
                + "WHERE r.RegistrationID = ?";
        try (
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, registrationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }
/**
 * 
 * @param registrationId
 * @return 
 */
    // Method to get package id by registration id
    public int getPackageIdByRegistrationId(int registrationId) {
        int packageId = -1; // Default value if not found
        String sql = "SELECT PackageID FROM Registrations WHERE RegistrationID = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, registrationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                packageId = rs.getInt("PackageID"); // Get the PackageID
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }

        return packageId; // Return the package ID or -1 if not found
    }

/**
 * 
 * @param registrationId
 * @return 
 */    
    // Method to get status of registration
    public String getStatusByRegistrationId(int registrationId) {
        String status = null;
        String sql = "SELECT Status FROM Registrations WHERE RegistrationID = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, registrationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                status = rs.getString("Status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
    
    /**
     * 
     * @param registrationId
     * @return 
     */
    // Method to get time register

    public Date getValidFromByRegistrationId(String registrationId) {
        Date validFrom = null;
        String sql = "SELECT ValidFrom FROM Registrations WHERE RegistrationID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, registrationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    validFrom = rs.getDate("Valid_From");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately (e.g., log the error, rethrow it, etc.)
        }

        return validFrom;
    }
    /**
     * 
     * @param userId
     * @param subjectId
     * @param packageId
     * @return
     * @throws SQLException 
     */
    //Method to check exist registration
public boolean hasExistingRegistration(int userId, int subjectId, int packageId) throws SQLException {
    String query = "SELECT COUNT(*) FROM Registrations WHERE UserID = ? AND SubjectID = ? AND PackageID = ? AND Status IN ('processing', 'Active', 'Inactive')";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, userId);
        stmt.setInt(2, subjectId);
        stmt.setInt(3, packageId);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0; // True if any matching record is found
            }
        }
    }
    return false;
}

}
