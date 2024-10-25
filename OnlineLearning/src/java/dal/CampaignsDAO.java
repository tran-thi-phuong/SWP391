/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
                campaign.setImage(rs.getString("Image"));
                campaign.setStatus(rs.getString("Status"));

                list.put(campaign.getCampaignId(), campaign);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                campaign.setImage(rs.getString("Image"));
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

    public void addCampaign(Campaign campaign) throws SQLException {
        String sql = "INSERT INTO campaigns (CampaignName, Description, StartDate, EndDate, Image, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, campaign.getCampaignName());
            stmt.setString(2, campaign.getDescription());
            stmt.setDate(3, (Date) campaign.getStartDate());
            stmt.setDate(4, (Date) campaign.getEndDate());
            stmt.setString(5, campaign.getImage());
            stmt.setString(6, campaign.getStatus());
            stmt.executeUpdate();
        }
    }
}
