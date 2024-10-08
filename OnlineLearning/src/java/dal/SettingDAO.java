package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.SystemSetting;

public class SettingDAO extends DBContext {

    // Method to save user settings
    public boolean saveSettings(SystemSetting setting) {
        // First, check if settings already exist for the user
        if (getSettingsByUserID(setting.getUserID()) != null) {
            // Update existing settings
            return updateSetting(setting);
        } else {
            // Insert new settings
            String query = "INSERT INTO System_Setting (UserID, NumberOfItems, QuizID, Title, Subject, Description, QuizType, Duration, PassCondition, Level, Quantity) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, setting.getUserID());
                preparedStatement.setInt(2, setting.getNumberOfItems());
                preparedStatement.setBoolean(3, setting.isQuizID());
                preparedStatement.setBoolean(4, setting.isTitle());
                preparedStatement.setBoolean(5, setting.isSubject());
                preparedStatement.setBoolean(6, setting.isDescription()); // Assuming you added a description column
                preparedStatement.setBoolean(7, setting.isQuizType());
                preparedStatement.setBoolean(8, setting.isDuration());
                preparedStatement.setBoolean(9, setting.isPassCondition()); // Assuming you added a pass condition column
                preparedStatement.setBoolean(10, setting.isLevel());
                preparedStatement.setBoolean(11, setting.isQuantity());

                return preparedStatement.executeUpdate() > 0; // Returns true if rows were affected
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
        return false;
    }

    // Method to retrieve user settings by user ID
    public SystemSetting getSettingsByUserID(int userID) {
        String query = "SELECT * FROM System_Setting WHERE UserID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                SystemSetting setting = new SystemSetting();
                setting.setUserID(resultSet.getInt("UserID"));
                setting.setNumberOfItems(resultSet.getInt("NumberOfItems"));
                setting.setQuizID(resultSet.getBoolean("QuizID"));
                setting.setTitle(resultSet.getBoolean("Title"));
                setting.setSubject(resultSet.getBoolean("Subject"));
                setting.setDescription(resultSet.getBoolean("Description")); // Assuming you added a description column
                setting.setQuizType(resultSet.getBoolean("QuizType"));
                setting.setDuration(resultSet.getBoolean("Duration"));
                setting.setPassCondition(resultSet.getBoolean("PassCondition")); // Assuming you added a pass condition column
                setting.setLevel(resultSet.getBoolean("Level"));
                setting.setQuantity(resultSet.getBoolean("Quantity"));
                return setting;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null; // Return null if no settings are found
    }

    public boolean addSetting(int userID) {
        String query = "INSERT INTO System_Setting (UserID, NumberOfItems, QuizID, Title, Subject, Description, QuizType, Duration, PassCondition, Level, Quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, 10); // numberOfItems is always 10
            preparedStatement.setBoolean(3, true); // quizID
            preparedStatement.setBoolean(4, true); // title
            preparedStatement.setBoolean(5, true); // subject
            preparedStatement.setBoolean(6, true); // description
            preparedStatement.setBoolean(7, true); // quizType
            preparedStatement.setBoolean(8, true); // duration
            preparedStatement.setBoolean(9, true); // passCondition
            preparedStatement.setBoolean(10, true); // level
            preparedStatement.setBoolean(11, true); // quantity

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0; // Return true if the setting was added successfully
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return false; // Return false if an error occurred
        }
    }

    public boolean updateSetting(SystemSetting setting) {
        String sql = "UPDATE System_Setting SET NumberOfItems = ?, QuizID = ?, Title = ?, Subject = ?, " +
                     "Description = ?, QuizType = ?, Duration = ?, PassCondition = ?, Level = ?, Quantity = ? " +
                     "WHERE UserID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, setting.getNumberOfItems());
            stmt.setBoolean(2, setting.isQuizID());
            stmt.setBoolean(3, setting.isTitle());
            stmt.setBoolean(4, setting.isSubject());
            stmt.setBoolean(5, setting.isDescription()); // Assuming you added a description column
            stmt.setBoolean(6, setting.isQuizType());
            stmt.setBoolean(7, setting.isDuration());
            stmt.setBoolean(8, setting.isPassCondition()); // Assuming you added a pass condition column
            stmt.setBoolean(9, setting.isLevel());
            stmt.setBoolean(10, setting.isQuantity());
            stmt.setInt(11, setting.getUserID());

            return stmt.executeUpdate() > 0; // Returns true if rows were affected
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle exception appropriately
        }
        return false;
    }
}
