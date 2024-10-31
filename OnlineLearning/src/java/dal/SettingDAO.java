package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.QuestionSetting;
import model.RegistrationSetting;
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
            String query = "INSERT INTO System_Setting (UserID, NumberOfItems, QuizID, Title, Subject, Description, QuizType, Duration, PassCondition, Level, Quantity) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
        String sql = "UPDATE System_Setting SET NumberOfItems = ?, QuizID = ?, Title = ?, Subject = ?, "
                + "Description = ?, QuizType = ?, Duration = ?, PassCondition = ?, Level = ?, Quantity = ? "
                + "WHERE UserID = ?";

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

    public boolean saveRegistrationSettings(RegistrationSetting setting) {
        // First, check if settings already exist for the user
        if (getRegistrationSettingsByUserID(setting.getUserID()) != null) {
            // Update existing settings
            return updateRegistrationSetting(setting);
        } else {
            // Insert new settings
            String query = "INSERT INTO System_Setting (UserID, NumberOfItems, RegistrationID, CustomerEmail"
                    + "Subject, campaign, PackageID, Total_Cost, Registration_Time, Valid_From,"
                    + " Valid_To, Status, Staff, Note) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, setting.getUserID());
                preparedStatement.setInt(2, setting.getNumberOfItems());
                preparedStatement.setBoolean(3, setting.isRegistrationId());
                preparedStatement.setBoolean(4, setting.isEmail());
                preparedStatement.setBoolean(5, setting.isSubject());
                preparedStatement.setBoolean(5, setting.isCampaign());
                preparedStatement.setBoolean(6, setting.isPackageId());
                preparedStatement.setBoolean(7, setting.isTotalCost());
                preparedStatement.setBoolean(8, setting.isRegistrationTime());
                preparedStatement.setBoolean(9, setting.isValidFrom());
                preparedStatement.setBoolean(10, setting.isValidTo());
                preparedStatement.setBoolean(11, setting.isStatus());
                preparedStatement.setBoolean(12, setting.isStaff());
                preparedStatement.setBoolean(13, setting.isNote());

                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Method to retrieve user settings by user ID
    public RegistrationSetting getRegistrationSettingsByUserID(int userID) {
        String query = "SELECT * FROM System_Setting WHERE UserID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                RegistrationSetting setting = new RegistrationSetting();
                setting.setUserID(resultSet.getInt("UserID"));
                setting.setNumberOfItems(resultSet.getInt("NumberOfItems"));
                setting.setRegistrationId(resultSet.getBoolean("RegistrationID"));
                setting.setEmail(resultSet.getBoolean("CustomerEmail"));
                setting.setSubject(resultSet.getBoolean("Subject"));
                setting.setCampaign(resultSet.getBoolean("campaign"));
                setting.setPackageId(resultSet.getBoolean("PackageID"));
                setting.setTotalCost(resultSet.getBoolean("Total_Cost"));
                setting.setRegistrationTime(resultSet.getBoolean("Registration_Time"));
                setting.setValidFrom(resultSet.getBoolean("Valid_From"));
                setting.setValidTo(resultSet.getBoolean("Valid_To"));
                setting.setStatus(resultSet.getBoolean("Status"));
                setting.setStaff(resultSet.getBoolean("Staff"));
                setting.setNote(resultSet.getBoolean("Note"));
                return setting;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null; // Return null if no settings are found
    }

    public boolean addRegistrationSetting(int userID) {
        String query = "INSERT INTO System_Setting (UserID, NumberOfItems, RegistrationID, CustomerEmail,"
                + " Subject,campaign, PackageID, Total_Cost, Registration_Time, Valid_From, Valid_To,"
                + " Status, Staff, Note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, 20); // numberOfItems is always 10
            preparedStatement.setBoolean(3, true); // RegistrationID
            preparedStatement.setBoolean(4, true); // CustomerEmail
            preparedStatement.setBoolean(5, true); // subject
            preparedStatement.setBoolean(6, true); //campaign
            preparedStatement.setBoolean(7, true); // PackageID
            preparedStatement.setBoolean(8, true); // Total_Cost
            preparedStatement.setBoolean(9, true); // Registration_Time
            preparedStatement.setBoolean(10, true); // Valid_From
            preparedStatement.setBoolean(11, true); // Valid_To
            preparedStatement.setBoolean(12, true); // Status
            preparedStatement.setBoolean(13, true); // Staff
            preparedStatement.setBoolean(14, true); // Note

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRegistrationSetting(RegistrationSetting setting) {
        String sql = "UPDATE System_Setting SET NumberOfItems = ?, RegistrationID = ?, CustomerEmail = ?, Subject = ?, "
                + "campaign = ?, PackageID = ?, Total_Cost = ?, Registration_Time = ?, Valid_From = ?, Valid_To = ?, Status = ?,"
                + " Staff = ?, Note = ?"
                + " WHERE UserID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, setting.getNumberOfItems());
            stmt.setBoolean(2, setting.isRegistrationId());
            stmt.setBoolean(3, setting.isEmail());
            stmt.setBoolean(4, setting.isSubject());
            stmt.setBoolean(5, setting.isCampaign());
            stmt.setBoolean(6, setting.isPackageId());
            stmt.setBoolean(7, setting.isTotalCost());
            stmt.setBoolean(8, setting.isRegistrationTime());
            stmt.setBoolean(9, setting.isValidFrom());
            stmt.setBoolean(10, setting.isValidTo());
            stmt.setBoolean(11, setting.isStatus());
            stmt.setBoolean(12, setting.isStaff());
            stmt.setBoolean(13, setting.isNote());
            stmt.setInt(14, setting.getUserID());

            return stmt.executeUpdate() > 0; // Returns true if rows were affected
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle exception appropriately
        }
        return false;
    }

    public QuestionSetting getQuestionSettingsByUserID(int userID) {
        QuestionSetting setting = null;
        String sql = "SELECT * FROM System_Setting WHERE UserID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    setting = new QuestionSetting();
                    setting.setUserID(rs.getInt("UserID"));
                    setting.setNumberOfItems(rs.getInt("NumberOfItems"));
                    setting.setShowContent(rs.getBoolean("ShowContent"));
                    setting.setShowLevel(rs.getBoolean("ShowLevel"));
                    setting.setShowStatus(rs.getBoolean("ShowStatus"));
                    // Add more settings as needed
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return setting;
    }

    public boolean updateQuestionSetting(QuestionSetting setting) {
        String sql = "UPDATE System_Setting SET NumberOfItems = ?, ShowContent = ?, ShowLevel = ?, ShowStatus = ? WHERE UserID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, setting.getNumberOfItems());
            stmt.setBoolean(2, setting.isShowContent());
            stmt.setBoolean(3, setting.isShowLevel());
            stmt.setBoolean(4, setting.isShowStatus());
            stmt.setInt(5, setting.getUserID());

            return stmt.executeUpdate() > 0; // Returns true if the update was successful
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Return false if there was an error
    }

    public static void main(String[] args) {
        SettingDAO sDAO = new SettingDAO();
        sDAO.addRegistrationSetting(2);
    }
}
