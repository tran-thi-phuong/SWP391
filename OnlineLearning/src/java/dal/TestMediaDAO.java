/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

// for SQL
import java.sql.*;

// database access
import java.util.ArrayList;
import java.util.List;

// model
import model.TestMedia;

public class TestMediaDAO extends DBContext {

    // search media by test id
    public List<TestMedia> getMediaByTestId(int testId) {
        List<TestMedia> mediaList = new ArrayList<>();
        String sql = "SELECT MediaID, TestID, Media_Link, Description FROM TestMedia WHERE TestID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, testId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TestMedia media = new TestMedia();
                media.setMediaId(rs.getInt("MediaID"));
                media.setTestId(rs.getInt("TestID"));
                media.setMediaLink(rs.getString("Media_Link"));
                media.setDescription(rs.getString("Description"));
                mediaList.add(media);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return mediaList;
    }

    // search media by its id
    public TestMedia getMediaById(int mediaId) {
        TestMedia media = null;
        String sql = "SELECT MediaID, TestID, Media_Link, Description FROM TestMedia WHERE MediaID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mediaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                media = new TestMedia();
                media.setMediaId(rs.getInt("MediaID"));
                media.setTestId(rs.getInt("TestID"));
                media.setMediaLink(rs.getString("Media_Link"));
                media.setDescription(rs.getString("Description"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return media;
    }

    // save media
    public void saveMedia(TestMedia media) {
        String sql = "INSERT INTO TestMedia (TestID, Media_Link, Description) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, media.getTestId()); // Make sure to set TestID appropriately
            stmt.setString(2, media.getMediaLink());
            stmt.setString(3, media.getDescription());
            stmt.executeUpdate();
            System.out.println("Media saved successfully: " + media.getMediaLink());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // delete media by media ID
    public void deleteMedia(int mediaId) {
        String sql = "DELETE FROM TestMedia WHERE MediaID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mediaId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Media with ID " + mediaId + " deleted successfully.");
            } else {
                System.out.println("No media found with ID " + mediaId + ".");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

