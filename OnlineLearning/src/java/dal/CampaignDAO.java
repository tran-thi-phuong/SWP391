/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Campaign;

/**
 *
 * @author tuant
 */
public class CampaignDAO extends DBContext{
    public List<Campaign> getAllCampaign(){
        List<Campaign> list = new ArrayList<>();
        try {
            String sql = "Select * from Campaigns";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Campaign cam = new Campaign();
                cam.setCampaignID(rs.getInt("CampaignID"));
                cam.setCampaignName(rs.getString("CampaignName"));
                cam.setDescription(rs.getString("Description"));
                cam.setStartDate(rs.getDate("StartDate"));
                cam.setStartDate(rs.getDate("EndDate"));
                cam.setImage(rs.getString("Image"));
                cam.setStatus(rs.getString("Status"));
                list.add(cam);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public static void main(String[] args){
        CampaignDAO cam = new CampaignDAO();
        System.out.println(cam.getAllCampaign());
    }
}