/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import model.Revenue;

/**
 *
 * @author tuant
 */
public class PaymentDAO extends DBContext {

    public double getTotalRevenue() {
        String sql = "SELECT SUM(Amount) AS TotalRevenue FROM Payment;";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public List<Revenue> getRevenueAllocation() {
        List<Revenue> list = new ArrayList<>();
        try {
            String sql = """
            SELECT sc.Title AS Title, SUM(p.Amount) AS Revenue
            FROM Payment p
            JOIN Subjects s ON p.SubjectID = s.SubjectID
            JOIN Subject_Category sc ON s.Subject_CategoryID = sc.Subject_CategoryID
            GROUP BY sc.Title
            
        """;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Revenue revenue = new Revenue();
                revenue.setCategory(rs.getString("Title"));
                revenue.setRevenue(rs.getDouble("Revenue"));
                list.add(revenue);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Revenue> getRevenueAllocationByTime(Date startDate, Date endDate) {
        List<Revenue> list = new ArrayList<>();
        String sql = """
        SELECT sc.Title AS Title, SUM(p.Amount) AS Revenue
        FROM Payment p
        JOIN Subjects s ON p.SubjectID = s.SubjectID
        JOIN Subject_Category sc ON s.Subject_CategoryID = sc.Subject_CategoryID
        WHERE p.PaymentDate BETWEEN ? AND ?
        GROUP BY sc.Title
    """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Revenue revenue = new Revenue();
                    revenue.setCategory(rs.getString("Title"));
                    revenue.setRevenue(rs.getDouble("Revenue"));
                    list.add(revenue);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public double getRevenueByTime(java.util.Date startDate, java.util.Date endDate) {
        String sql = "  select sum(Amount) from Payment where PaymentDate between ? and ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public boolean addPayment(int userId, int subjectId, double amount) {
        String sql = "INSERT INTO Payment (UserID, SubjectID, PaymentDate, Amount, PaymentMethod) VALUES (?, ?,GETDATE(),?, 'QR Pay')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, subjectId);
            stmt.setDouble(3, amount);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {

                return true;
            } else {

                return false;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
}
