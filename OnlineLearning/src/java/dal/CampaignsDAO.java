/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

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

}
