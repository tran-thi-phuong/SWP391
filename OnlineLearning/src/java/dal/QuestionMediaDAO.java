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
public class QuestionMediaDAO extends DBContext{
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

}
