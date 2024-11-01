/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.CampaignMedia;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CampaignMediaDAO extends DBContext {

    public boolean addCampaignMedia(CampaignMedia media) {
        String sql = "INSERT INTO CampaignMedia (CampaignID, MediaLink, Description) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, media.getCampaignID());
            stmt.setString(2, media.getMediaLink());
            stmt.setString(3, media.getDescription());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

public List<CampaignMedia> getCampaignMediaByCampaignID(int campaignId) {
    List<CampaignMedia> mediaList = new ArrayList<>();
    String sql = "SELECT * FROM CampaignMedia WHERE CampaignID = ?";

    try (
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

        pstmt.setInt(1, campaignId);
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the ResultSet to fetch all campaign media
        while (rs.next()) {
            CampaignMedia campaignMedia = new CampaignMedia();
            campaignMedia.setMediaID(rs.getInt("MediaID"));
            campaignMedia.setMediaLink(rs.getString("MediaLink"));
            campaignMedia.setDescription(rs.getString("Description"));
            campaignMedia.setCampaignID(rs.getInt("CampaignID"));

            // Add the campaignMedia object to the mediaList
            mediaList.add(campaignMedia);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return mediaList;
}

    public boolean removeCampaignMedia(int campaignID) {
        String sql = "DELETE FROM CampaignMedia WHERE CampaignID = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            // Đặt giá trị cho tham số trong truy vấn SQL
            preparedStatement.setInt(1, campaignID);

            // Thực thi truy vấn và kiểm tra số lượng bản ghi bị ảnh hưởng
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // Nếu có ít nhất một bản ghi bị xóa thì trả về true

        } catch (SQLException e) {
            // Xử lý ngoại lệ (có thể log lỗi hoặc ném ra một ngoại lệ mới)
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

}
