/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author 84336
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Question;
import model.QuestionMedia;

public class QuestionMediaDAO extends DBContext {

    public List<QuestionMedia> getMediaByQuestionId(int questionId) {
        List<QuestionMedia> mediaList = new ArrayList<>();
        String sql = "SELECT MediaID, QuestionID, MediaLink, Description FROM QuestionMedia WHERE QuestionID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                QuestionMedia media = new QuestionMedia();
                media.setMediaID(rs.getInt("MediaID"));
                media.setQuestionID(rs.getInt("QuestionID"));
                media.setMediaLink(rs.getString("MediaLink"));
                media.setDescription(rs.getString("Description"));
                mediaList.add(media);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return mediaList;
    }

    public QuestionMedia getMediaById(int mediaId) {
        QuestionMedia media = null;
        String sql = "SELECT MediaID, QuestionID, MediaLink, Description FROM QuestionMedia WHERE MediaID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mediaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                media = new QuestionMedia();
                media.setMediaID(rs.getInt("MediaID"));
                media.setQuestionID(rs.getInt("QuestionID"));
                media.setMediaLink(rs.getString("MediaLink"));
                media.setDescription(rs.getString("Description"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return media;
    }

    public void saveMedia(QuestionMedia media) {
        String sql = "INSERT INTO QuestionMedia (QuestionID, MediaLink, Description) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, media.getQuestionID()); // Make sure to set QuestionID appropriately
            stmt.setString(2, media.getMediaLink());
            stmt.setString(3, media.getDescription());
            stmt.executeUpdate();
            System.out.println("Media saved successfully: " + media.getMediaLink());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteMedia(int QuestionID) {
        String sql = "DELETE FROM QuestionMedia WHERE QuestionID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, QuestionID);
            int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Media with ID " + QuestionID + " deleted successfully.");
        } else {
            System.out.println("No media found with ID " + QuestionID + ".");
        }
    }
         catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
