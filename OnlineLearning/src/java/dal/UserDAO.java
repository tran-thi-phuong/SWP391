/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Users;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class UserDAO extends DBContext {

    // Method to get user by username
    public Users getUserByUsername(String username) {
        Users user = null;
        try {
            String sql = "SELECT * FROM Users WHERE TRIM(Username) = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setEmail(rs.getString("Email"));
                user.setAddress(rs.getString("Address"));
                user.setAvatar(rs.getString("Avatar"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                user.setToken(rs.getString("Token"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Method to login
    public Users getUser(String username, String password) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE ( Username = ? OR  Email = ?) AND  Password = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, username);
            st.setString(3, password);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setName(rs.getString("Name"));
                    user.setGender(rs.getString("Gender"));
                    user.setPhone(rs.getString("Phone"));
                    user.setEmail(rs.getString("Email"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(rs.getString("Status"));
                    user.setToken(rs.getString("Token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return user;
    }

    public List<Users> getInstructors() {
        List<Users> instructors = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE Role = 'Instructor'";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Users user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setEmail(rs.getString("Email"));
                user.setAddress(rs.getString("Address"));
                user.setAvatar(rs.getString("Avatar"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                user.setToken(rs.getString("Token"));
                instructors.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return instructors;
    }

    // Method to update password
    public boolean updateUserPassword(Users user) {
        String sql = "UPDATE Users SET Password = ? WHERE Username = ?";
        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Update Members table
            try (PreparedStatement statementMembers = connection.prepareStatement(sql)) {
                statementMembers.setString(1, user.getPassword());
                statementMembers.setString(2, user.getUsername());
                statementMembers.executeUpdate();
            }
            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Reset auto-commit mode to its default state
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    // Method to update status of user

    public boolean updateUserStatus(Users user) {
        String sql = "UPDATE Users SET Status = ? WHERE Username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getStatus());
            ps.setString(2, user.getUsername());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
// Method to get user by email

    public Users getUserByEmail(String email) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE Email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Chỉ cần lấy một bản ghi
                    user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setName(rs.getString("Name"));
                    user.setGender(rs.getString("Gender"));
                    user.setPhone(rs.getString("Phone"));
                    user.setEmail(rs.getString("Email"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(rs.getString("Status"));
                    user.setToken(rs.getString("Token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    // get cutomer's email method

    public String getEmailByUserId(int userId) {
        String email = "";

        try {
            String query = "SELECT Email FROM Users WHERE UserID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        email = resultSet.getString("email");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }

//get staff username method
    public String getStaffUserNameByUserId(int userId) {
        String username = "";

        try {
            String query = "SELECT Username FROM Users WHERE UserID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        username = resultSet.getString("username");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }
    // Create Token for an account

    public boolean updateResetToken(String email, String token) {
        String sql = "UPDATE Users SET Token = ? WHERE Email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setString(2, email);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu cập nhật thành công (hơn 0 hàng bị ảnh hưởng)
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }

    //Method to get user by token
    public Users getUserByToken(String token) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE Token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setName(rs.getString("Name"));
                    user.setGender(rs.getString("Gender"));
                    user.setPhone(rs.getString("Phone"));
                    user.setEmail(rs.getString("Email"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(rs.getString("Status"));
                    user.setToken(rs.getString("Token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    // Method to register account for customer

    public int register(String fullname, String username, String email, String password, String phone, String address, String gender, String role, String status) {
        int userId = -1;

        String sql = "INSERT INTO Users (Name, Username, Email, Password, Phone, Address, Gender, Role, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            st.setString(1, fullname);
            st.setString(2, username);
            st.setString(3, email);
            st.setString(4, password);
            if (phone != null && !phone.isEmpty()) {
                st.setString(5, phone);
            } else {
                st.setNull(5, java.sql.Types.VARCHAR);
            }

            if (address != null && !address.isEmpty()) {
                st.setString(6, address);
            } else {
                st.setNull(6, java.sql.Types.VARCHAR);
            }

            if (gender != null && !gender.isEmpty()) {
                st.setString(7, gender);
            } else {
                st.setNull(7, java.sql.Types.VARCHAR);
            }

            st.setString(8, role);
            st.setString(9, status);

            int affectedRows = st.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } else {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userId;
    }

    // Method to get user information
    public Users getUserByInfo(String info, String content) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE " + info + " = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, content);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setName(rs.getString("Name"));
                    user.setGender(rs.getString("Gender"));
                    user.setPhone(rs.getString("Phone"));
                    user.setEmail(rs.getString("Email"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(rs.getString("Status"));
                    user.setToken(rs.getString("Token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Return the user object or null if not found
    }
// Method to update password by token

    public void updatePassword(String token, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE Token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword); // Nên băm mật khẩu trước khi lưu
            ps.setString(2, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// Method to clear token

    public void clearResetToken(String token) {
        String sql = "UPDATE Users SET Token = NULL WHERE Token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Method to update profile

    public void updateProfile(String name, String username, String phone, String address, String gender, int userID, String avatar) {
        try {
            String sql = "update Users set Name = ?, Username= ?, Phone = ?, Address = ?, Gender = ?, Avatar = ? "
                    + "where UserID = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, name);
            st.setString(2, username);
            if (phone != null && !phone.isEmpty()) {
                st.setString(3, phone);
            } else {
                st.setNull(3, java.sql.Types.VARCHAR);
            }
            if (address != null && !address.isEmpty()) {
                st.setString(4, address);
            } else {
                st.setNull(4, java.sql.Types.VARCHAR);
            }
            if (gender != null && !gender.isEmpty()) {
                st.setString(5, gender);
            } else {
                st.setNull(5, java.sql.Types.VARCHAR);
            }

            if (avatar != null && !avatar.isEmpty()) {
                st.setString(6, avatar);
            } else {
                st.setNull(6, java.sql.Types.VARCHAR);
            }
            st.setInt(7, userID);
            st.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Method complete profile
    public boolean completeProfile(String fullname, String username, String password, String phone, String address, String gender, String token) {
        String sql = "UPDATE Users SET Name = ?, Username = ?, Password = ?, Phone = ?, Address = ?, Gender = ?, Status = 'Active' WHERE Token = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters
            stmt.setString(1, fullname); // Set fullname
            stmt.setString(2, username); // Set username
            stmt.setString(3, password); // Set password (Consider hashing it before storing)
            stmt.setString(4, phone); // Set phone
            stmt.setString(5, address); // Set address
            stmt.setString(6, gender); // Set gender
            stmt.setString(7, token); // Set token as the 7th parameter

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if update failed
    }

// Method to get user by email
    public Integer getUserIdByEmail(String email) throws SQLException {
        String query = "SELECT UserID FROM Users WHERE Email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            }
        }
        return null; // Return null if email not found
    }
    // Method to add an rondom account by email

    public int addUser(String email, String password, String gender, String phone, String username) throws SQLException {
        String sql = "INSERT INTO Users (Email, Password, Gender, Phone, Status, Role, Username) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int generatedUserId = -1;

        try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, email);
            pstmt.setString(2, "12345678");
            pstmt.setString(3, gender);
            pstmt.setString(4, phone);
            pstmt.setString(5, "Inactive");
            pstmt.setString(6, "Customer");
            pstmt.setString(7, username);

            // Execute the insert operation
            pstmt.executeUpdate();

            // Retrieve the generated user ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedUserId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
            throw e; // Re-throw exception after logging
        }

        return generatedUserId;
    }

    public int getTotalCustomer() {
        String sql = "SELECT COUNT(*) FROM Users where Role = 'Customer' and Status = 'Active'";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getNewCustomer(Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Create_At BETWEEN ? AND ? and Status = 'Active' and Role = 'Customer'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getNewlyBoughtCustomer(Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM (SELECT UserID FROM Payment where PaymentDate between ? and ? GROUP BY UserID HAVING COUNT(UserID) = 1) AS NewlyBought";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public int getTotalBoughtCustomer() {
        String sql = "SELECT COUNT(DISTINCT UserID) FROM Payment;";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }
        return 0;
    }

    public Users getUserById(int userId) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new Users();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setEmail(rs.getString("Email"));
                user.setAvatar(rs.getString("Avatar"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    //Methousers to paging filter, sort user
    public List<Users> getUsersByPage(int currentPage, int pageSize, String gender, String role, String status, String name, String email, String phone, String sortColumn, String sortOrder) {
        List<Users> users = new ArrayList<>();
        int offset = (currentPage - 1) * pageSize;

        StringBuilder sql = new StringBuilder("SELECT * FROM Users WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Build the query with parameters
        if (role != null && !role.isEmpty()) {
            sql.append(" AND Role LIKE ?");
            params.add("%" + role + "%");
        }
        if (gender != null && !gender.isEmpty()) {
            sql.append(" AND Gender LIKE ?");
            params.add("%" + gender + "%");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND Name LIKE ?");
            params.add("%" + name + "%");
        }
        if (email != null && !email.isEmpty()) {
            sql.append(" AND Email LIKE ?");
            params.add("%" + email + "%");
        }
        if (phone != null && !phone.isEmpty()) {
            sql.append(" AND Phone LIKE ?");
            params.add("%" + phone + "%");
        }

        sql.append(" ORDER BY ").append(sortColumn).append(" ").append(sortOrder)
                .append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }
            stmt.setInt(paramIndex++, offset);
            stmt.setInt(paramIndex, pageSize);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setName(rs.getString("Name"));
                    user.setGender(rs.getString("Gender"));
                    user.setPhone(rs.getString("Phone"));
                    user.setEmail(rs.getString("Email"));
                    user.setAddress(rs.getString("Address"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setRole(rs.getString("Role"));
                    user.setStatus(rs.getString("Status"));
                    user.setToken(rs.getString("Token"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {

        }
        return users; // Return the list, even if it's empty
    }

    public int getTotalUser(String gender, String role, String status, String name, String email, String phone) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Users WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Build the query with parameters
        if (role != null && !role.isEmpty()) {
            sql.append(" AND Role LIKE ?");
            params.add("%" + role + "%");
        }
        if (gender != null && !gender.isEmpty()) { // Ensure this is consistent
            sql.append(" AND Gender LIKE ?");
            params.add("%" + gender + "%");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND Name LIKE ?");
            params.add("%" + name + "%");
        }
        if (email != null && !email.isEmpty()) {
            sql.append(" AND Email LIKE ?");
            params.add("%" + email + "%");
        }
        if (phone != null && !phone.isEmpty()) {
            sql.append(" AND Phone LIKE ?");
            params.add("%" + phone + "%");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {

        }
        return 0; // Return 0 if an error occurs
    }
// Method to update user role and status

    public boolean updateUser(String userId, String role, String status) {
        String sql = "UPDATE Users SET Role = ?, Status = ? WHERE UserID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set parameters for the update query
            ps.setString(1, role);
            ps.setString(2, status);
            ps.setString(3, userId);

            // Execute update and check if any row was updated
            int updatedRows = ps.executeUpdate();
            return updatedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addStaff(String email,String username, String role) {
        String sql = "INSERT INTO Users (Email, Role, Username, Password, Status) VALUES (?, ?,?,?, 'Inactive')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, role);
            ps.setString(3, username);
            ps.setString(4, "12345678");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isEmailExists(String email) {
    String query = "SELECT COUNT(*) FROM Users WHERE Email = ?";
    try (
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // If count > 0, email exists
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
 public boolean deleteUser(String userId) {
    String sql = "DELETE FROM Users WHERE UserID = ?"; // Adjust table name if necessary
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        // Set the userId parameter
        statement.setString(1, userId);

        // Execute the delete query
        int rowsAffected = statement.executeUpdate();

        // If one or more rows are affected, return true
        return rowsAffected > 0;
    } catch (SQLException e) {
        // Log the error with details
        System.err.println("Error deleting user with ID: " + userId);
        // Print stack trace for debugging
        return false;
    }
}

}
