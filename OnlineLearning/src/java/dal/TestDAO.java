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
import model.Test;

public class TestDAO extends DBContext {

    public void addTest(Test test) {
        String sql = "INSERT INTO Tests (SubjectID, Title, Description, Type, Duration, Pass_Condition, Level, Quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, test.getSubjectID());
            stmt.setString(2, test.getTitle());
            stmt.setString(3, test.getDescription());
            stmt.setString(4, test.getType());
            stmt.setInt(5, test.getDuration());
            stmt.setDouble(6, test.getPassCondition());
            stmt.setString(7, test.getLevel());
            stmt.setInt(8, test.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Test> getAllTests() {
        List<Test> tests = new ArrayList<>();
        String sql = "SELECT * FROM Tests";
        try (
                Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Test test = new Test();
                test.setTestID(rs.getInt("TestID"));
                test.setSubjectID(rs.getInt("SubjectID"));
                test.setTitle(rs.getString("Title"));
                test.setDescription(rs.getString("Description"));
                test.setType(rs.getString("Type"));
                test.setDuration(rs.getInt("Duration"));
                test.setPassCondition(rs.getDouble("Pass_Condition"));
                test.setLevel(rs.getString("Level"));
                test.setQuantity(rs.getInt("Quantity"));
                tests.add(test);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tests;
    }

    public List<Test> searchTests(String query, int offset, int limit) {
        List<Test> tests = new ArrayList<>();
        String sql = "SELECT * FROM Tests WHERE Title LIKE ? OR Description LIKE ? ORDER BY TestID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            stmt.setInt(3, offset);
            stmt.setInt(4, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Test test = new Test();
                    test.setTestID(rs.getInt("TestID"));
                    test.setSubjectID(rs.getInt("SubjectID"));
                    test.setTitle(rs.getString("Title"));
                    test.setDescription(rs.getString("Description"));
                    test.setType(rs.getString("Type"));
                    test.setDuration(rs.getInt("Duration"));
                    test.setPassCondition(rs.getDouble("Pass_Condition"));
                    test.setLevel(rs.getString("Level"));
                    test.setQuantity(rs.getInt("Quantity"));
                    tests.add(test);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tests;
    }

    public int countTests(String query) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Tests WHERE Title LIKE ? OR Description LIKE ?";
        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }

    // Additional methods for update and delete can be added similarly
}
