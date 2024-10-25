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

    public int addTest(Test test) {
        String sql = "INSERT INTO Tests (SubjectID, Title, Description, MediaType, MediaURL, MediaDescription, Type, Duration, Pass_Condition, Level, Quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1; 

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, test.getSubjectID());
            stmt.setString(2, test.getTitle());
            stmt.setString(3, test.getDescription());
            stmt.setString(4, test.getMediaType()); 
            stmt.setString(5, test.getMediaURL()); 
            stmt.setString(6, test.getMediaDescription());
            stmt.setString(7, test.getType());
            stmt.setInt(8, test.getDuration());
            stmt.setDouble(9, test.getPassCondition());
            stmt.setString(10, test.getLevel());
            stmt.setInt(11, test.getQuantity());

 
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1); // Get the first column of the generated keys
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return generatedId; // Return the generated ID
    }

    public void updateTest(Test test) {
        String sql = "UPDATE Tests SET SubjectID = ?, Title = ?, Description = ?, MediaType = ?, MediaURL = ?, MediaDescription = ?, Type = ?, Duration = ?, Pass_Condition = ?, Level = ?, Quantity = ? WHERE TestID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, test.getSubjectID());
            stmt.setString(2, test.getTitle());
            stmt.setString(3, test.getDescription());
            stmt.setString(4, test.getMediaType()); // New field
            stmt.setString(5, test.getMediaURL()); // New field
            stmt.setString(6, test.getMediaDescription()); // New field
            stmt.setString(7, test.getType());
            stmt.setInt(8, test.getDuration());
            stmt.setDouble(9, test.getPassCondition());
            stmt.setString(10, test.getLevel());
            stmt.setInt(11, test.getQuantity());
            stmt.setInt(12, test.getTestID()); // Ensure you set the TestID at the end

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Test updated successfully.");
            } else {
                System.out.println("No test found with the provided TestID.");
            }
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
            sql.append(" AND Type Like ?");
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
            sql.append(" AND Type Like ?");
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

    public List<String> getAllQuizTypes() {
        List<String> quizTypes = new ArrayList<>();
        String sql = "SELECT DISTINCT Type FROM Tests"; // Assuming 'Type' is the column name for quiz types

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                quizTypes.add(rs.getString("Type"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return quizTypes;
    }

    public int countAttemptsByTestID(int testID) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM User_Attempt WHERE TestID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, testID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return count;
    }

    public double calculatePassRate(int testID) {
        double passRate = 0.0;
        String passConditionSql = "SELECT PassCondition FROM Tests WHERE TestID = ?";
        String totalAttemptsSql = "SELECT COUNT(*) FROM User_Attempt WHERE TestID = ?";
        String passedAttemptsSql = "SELECT COUNT(*) FROM User_Attempt WHERE TestID = ? AND Mark > ?";

        try {
            // Get PassCondition
            double passCondition = 0.0;
            try (PreparedStatement pstmt = connection.prepareStatement(passConditionSql)) {
                pstmt.setInt(1, testID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    passCondition = rs.getDouble("PassCondition");
                }
            }

            // Count total attempts
            int totalAttempts = 0;
            try (PreparedStatement pstmt = connection.prepareStatement(totalAttemptsSql)) {
                pstmt.setInt(1, testID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    totalAttempts = rs.getInt(1);
                }
            }

            // Count passed attempts
            int passedAttempts = 0;
            try (PreparedStatement pstmt = connection.prepareStatement(passedAttemptsSql)) {
                pstmt.setInt(1, testID);
                pstmt.setDouble(2, passCondition);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    passedAttempts = rs.getInt(1);
                }
            }

            // Calculate pass rate
            if (totalAttempts > 0) {
                passRate = (double) passedAttempts * 100.0 / totalAttempts;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return passRate;
    }

    public Test getTestById(int testID) {
        String sql = "SELECT * FROM Tests WHERE TestID = ?";
        Test test = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, testID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                test = new Test(); // Create a new Test object
                test.setTestID(rs.getInt("TestID"));
                test.setSubjectID(rs.getInt("SubjectID"));
                test.setTitle(rs.getString("Title"));
                test.setDescription(rs.getString("Description"));
                test.setMediaType(rs.getString("MediaType"));
                test.setMediaURL(rs.getString("MediaURL"));
                test.setMediaDescription(rs.getString("MediaDescription"));
                test.setType(rs.getString("Type"));
                test.setDuration(rs.getInt("Duration"));
                test.setPassCondition(rs.getDouble("Pass_Condition"));
                test.setLevel(rs.getString("Level"));
                test.setQuantity(rs.getInt("Quantity"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return test; // Return the Test object or null if not found
    }

    public void deleteTest(int testID) {
        String sql = "DELETE FROM Tests WHERE TestID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, testID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Test deleted successfully.");
            } else {
                System.out.println("No test found with the provided TestID.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        TestDAO d = new TestDAO();
        model.Test testToUpdate = d.getTestById(153);
        System.out.println(testToUpdate);
    }
}
