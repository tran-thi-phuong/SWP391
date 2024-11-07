/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;

/**
 *
 * @author Admin
 */
public class Campaign_SubjectDAO extends DBContext {
    //Method to add campaign for subject
/**
 * 
 * @param campaignId
 * @param subjectId
 * @param discount
 * @return 
 */
  public boolean addCampaignSubject(String campaignId, String subjectId, int discount) {
        String sql = "INSERT INTO Campaign_Subject (CampaignID, SubjectID, Discount) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(campaignId)); // Campaign ID
            ps.setInt(2, Integer.parseInt(subjectId));  // Subject ID
            ps.setInt(3, discount); // Discount

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;  // Returns true if insertion is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Returns false if an error occurs
        }
    }
    public Map<String, Double> getCurrentDiscounts() {
    Map<String, Double> discounts = new HashMap<>();
    String sql = "SELECT CampaignID, SubjectID, Discount FROM Campaign_Subject";
    
    try (
         PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            String key = rs.getInt("CampaignID") + "_" + rs.getInt("SubjectID");
            double discount = rs.getDouble("Discount");
            discounts.put(key, discount);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return discounts;
}
    public static void main(String[] args) {
       Campaign_SubjectDAO d = new Campaign_SubjectDAO();
       Map<String, Double> c = d.getCurrentDiscounts();
        for (String key : c.keySet()) {
            System.out.println(key);
        }
       
    }
    }
