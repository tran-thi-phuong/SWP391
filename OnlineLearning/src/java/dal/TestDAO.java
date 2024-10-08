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

    public List<Test> getAllTests(int pageNumber, int pageSize) {
    List<Test> tests = new ArrayList<>();
    // Calculate the offset for pagination
    int offset = (pageNumber - 1) * pageSize;
    String sql = "SELECT * FROM Tests LIMIT ? OFFSET ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, pageSize); // Number of items per page
        stmt.setInt(2, offset);    // Calculate the offset

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
public List<Test> getAllTests(int pageNumber, int pageSize, String search, String subjectId, String type) {
    List<Test> tests = new ArrayList<>();
    int offset = (pageNumber - 1) * pageSize;

    // Base SQL query
    StringBuilder sql = new StringBuilder("SELECT * FROM Tests WHERE 1=1"); // Start with a basic query

    // Build query conditions based on provided parameters
    if (search != null && !search.isEmpty()) {
        sql.append(" AND Title LIKE ?");
    }
    if (subjectId != null && !subjectId.isEmpty()) {
        sql.append(" AND SubjectID = ?");
    }
    if (type != null && !type.isEmpty()) {
        sql.append(" AND Type = ?");
    }

    // Add pagination using OFFSET and FETCH NEXT
    sql.append(" ORDER BY TestID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

    try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
        int paramIndex = 1;

        // Set parameters for the query
        if (search != null && !search.isEmpty()) {
            stmt.setString(paramIndex++, "%" + search + "%"); // Use LIKE for searching
        }
        if (subjectId != null && !subjectId.isEmpty()) {
            stmt.setInt(paramIndex++, Integer.parseInt(subjectId)); // Set subject ID
        }
        if (type != null && !type.isEmpty()) {
            stmt.setString(paramIndex++, type); // Set type
        }

        // Set pagination parameters
        stmt.setInt(paramIndex++, offset); // Offset for pagination
        stmt.setInt(paramIndex++, pageSize); // Number of items per page

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
        ex.printStackTrace(); // Handle exception appropriately
    }
    return tests; // Return the list of tests
}

public int getTotalTestCount(String search, String subjectId, String type) {
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Tests WHERE 1=1");

    // Build query conditions based on provided parameters
    if (search != null && !search.isEmpty()) {
        sql.append(" AND Title LIKE ?");
    }
    if (subjectId != null && !subjectId.isEmpty()) {
        sql.append(" AND SubjectID = ?");
    }
    if (type != null && !type.isEmpty()) {
        sql.append(" AND Type = ?");
    }

    try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
        int paramIndex = 1;

        // Set parameters for the query
        if (search != null && !search.isEmpty()) {
            stmt.setString(paramIndex++, "%" + search + "%"); // Use LIKE for searching
        }
        if (subjectId != null && !subjectId.isEmpty()) {
            stmt.setInt(paramIndex++, Integer.parseInt(subjectId)); // Set subject ID
        }
        if (type != null && !type.isEmpty()) {
            stmt.setString(paramIndex++, type); // Set type
        }

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1); // Return the count
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Handle exception appropriately
    }
    return 0; // Return 0 if an error occurs
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
