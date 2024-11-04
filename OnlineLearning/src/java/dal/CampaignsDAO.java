/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.Date;
import model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class CampaignsDAO extends DBContext {

    private boolean throwError = false;

    public void setThrowError(boolean throwError) {
        this.throwError = throwError;
    }

    public Map<Integer, Campaigns> getAllCampaigns() {
        Map<Integer, Campaigns> list = new HashMap<>();
        String sql = "SELECT * FROM Campaigns"; // Sửa SQL cho đúng tên bảng

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Campaigns campaign = new Campaigns();
                campaign.setCampaignId(rs.getInt("CampaignID"));
                campaign.setCampaignName(rs.getString("CampaignName"));
                campaign.setDescription(rs.getString("Description"));
                campaign.setStartDate(rs.getDate("StartDate"));
                campaign.setEndDate(rs.getDate("EndDate"));
                campaign.setStatus(rs.getString("Status"));

                list.put(campaign.getCampaignId(), campaign);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Campaigns> getCampaignsesByPage(int currentPage, int pageSize, String campaignName, Date validFrom, Date validTo, String status) {
        List<Campaigns> campaigns = new ArrayList<>();
        int offset = (currentPage - 1) * pageSize;

        StringBuilder sql = new StringBuilder("SELECT * FROM Campaigns WHERE 1=1");

        List<Object> params = new ArrayList<>();
        if (campaignName != null && !campaignName.isEmpty()) {
            sql.append(" AND CampaignName LIKE ?");
            params.add("%" + campaignName + "%");
        }
        if (validFrom != null) {
            sql.append(" AND StartDate >= ?");
            params.add(new Timestamp(validFrom.getTime()));
        }
        if (validTo != null) {
            sql.append(" OR EndDate <= ?");
            params.add(new Timestamp(validTo.getTime()));
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status LIKE ?");
            params.add("%" + status + "%");
        }

        sql.append(" ORDER BY CampaignID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }
            stmt.setInt(paramIndex++, offset);
            stmt.setInt(paramIndex, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Campaigns c = new Campaigns();
                    c.setCampaignId(rs.getInt("CampaignID"));
                    c.setCampaignName(rs.getString("CampaignName"));
                    c.setDescription(rs.getString("Description")); // Fixed typo here
                    c.setStartDate(rs.getDate("StartDate"));
                    c.setEndDate(rs.getDate("EndDate"));
                    c.setStatus(rs.getString("Status"));
                    campaigns.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campaigns;
    }

    public int getTotalCampaign(String campaignName, Date validFrom, Date validTo, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Campaigns WHERE 1=1");

        if (campaignName != null && !campaignName.isEmpty()) {
            sql.append(" AND CampaignName LIKE ?");
        }
        if (validFrom != null) {
            sql.append(" AND StartDate >= ?");
        }
        if (validTo != null) {
            sql.append(" AND EndDate <= ?"); // Fixed typo: changed "EndDae" to "EndDate"
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status LIKE ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (campaignName != null && !campaignName.isEmpty()) {
                stmt.setString(paramIndex++, "%" + campaignName + "%");
            }
            if (validFrom != null) {
                stmt.setTimestamp(paramIndex++, new Timestamp(validFrom.getTime()));
            }
            if (validTo != null) {
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

    public List<Campaigns> getAllCampaign() {
        List<Campaigns> list = new ArrayList<>();
        try {
            String sql = "Select * from Campaigns";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Campaigns cam = new Campaigns();
                cam.setCampaignId(rs.getInt("CampaignID"));
                cam.setCampaignName(rs.getString("CampaignName"));
                cam.setDescription(rs.getString("Description"));
                cam.setStartDate(rs.getDate("StartDate"));
                cam.setStartDate(rs.getDate("EndDate"));
                cam.setStatus(rs.getString("Status"));
                list.add(cam);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public boolean stopCampaign(int campaignId) {
        String sql = "UPDATE Campaigns SET EndDate = GETDATE(), Status = 'End' WHERE CampaignID = ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, campaignId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean startCampaign(int campaignId) {
        if (throwError) {
            throw new RuntimeException("Simulated database error");
        }

        String sql = "UPDATE Campaigns SET StartDate = GETDATE(), Status = 'Processing' WHERE CampaignID = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, campaignId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } catch (RuntimeException e) {
            System.out.println(e);
            return false;
        }
    }

    public Campaigns getCampaignByID(int campaignId) {
        Campaigns campaign = null;
        String sql = "SELECT * FROM Campaigns WHERE CampaignID = ?";

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, campaignId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                campaign = new Campaigns();
                campaign.setCampaignId(rs.getInt("CampaignID"));
                campaign.setCampaignName(rs.getString("CampaignName"));
                campaign.setDescription(rs.getString("Description"));
                campaign.setStartDate(rs.getDate("StartDate"));
                campaign.setEndDate(rs.getDate("EndDate"));
                campaign.setStatus(rs.getString("Status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return campaign;
    }

    public boolean updateCampaign(Campaigns campaign) {
        String sql = "UPDATE Campaigns SET CampaignName = ?, Description = ?, StartDate = ?, EndDate = ?, Status = ? WHERE CampaignID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, campaign.getCampaignName());
            pstmt.setString(2, campaign.getDescription());

            // Set the start date and end date using java.sql.Date
            pstmt.setDate(3, new java.sql.Date(campaign.getStartDate().getTime()));
            pstmt.setDate(4, new java.sql.Date(campaign.getEndDate().getTime()));

            pstmt.setString(5, campaign.getStatus());
            pstmt.setInt(6, campaign.getCampaignId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly
            return false;
        }
    }

    public int addCampaign(Campaigns campaign) throws SQLException {
        String sql = "INSERT INTO Campaigns (CampaignName, Description, StartDate, EndDate, Status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, campaign.getCampaignName());
            stmt.setString(2, campaign.getDescription());
            stmt.setDate(3, (java.sql.Date) (Date) campaign.getStartDate());
            stmt.setDate(4, (java.sql.Date) (Date) campaign.getEndDate());
            stmt.setString(5, campaign.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating campaign failed, no rows affected.");
            }

            // Retrieve the generated campaign ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the ID of the newly added campaign
                } else {
                    throw new SQLException("Creating campaign failed, no ID obtained.");
                }
            }
        }
    }
 
}
