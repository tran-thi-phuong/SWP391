package dal;

import java.util.ArrayList;
import java.util.List;
import model.Registrations;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                +"WHERE r.RegistrationID = ?";

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
    
    public static void main(String[] args) {
        RegistrationsDAO r = new RegistrationsDAO();
        r.getRegistrationById(3);
                
    }
}
