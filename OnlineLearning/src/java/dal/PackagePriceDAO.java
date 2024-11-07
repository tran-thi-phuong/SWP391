/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.List;
import model.PackagePrice;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author 84336
 */
public class PackagePriceDAO extends DBContext {
    //get by subject
    public List<PackagePrice> searchBySubjectId(int subjectId) {
        List<PackagePrice> packages = new ArrayList<>();
        String query = "SELECT * FROM Package_Price WHERE SubjectID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, subjectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PackagePrice packagePrice = new PackagePrice();
                packagePrice.setPackageId(resultSet.getInt("PackageId"));
                packagePrice.setSubjectId(resultSet.getInt("SubjectId"));
                packagePrice.setName(resultSet.getString("name"));
                packagePrice.setDurationTime(resultSet.getInt("duration_time"));
                packagePrice.setSalePrice(resultSet.getDouble("sale_price"));
                packagePrice.setPrice(resultSet.getDouble("price"));
                packages.add(packagePrice);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return packages;
    }
    //get by package id
    public PackagePrice searchByPackagePriceId(int packagePriceId) {
        PackagePrice packagePrice = null; // Initialize to null
        String query = "SELECT * FROM Package_Price WHERE PackageID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, packagePriceId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) { // Check if there's a result
                packagePrice = new PackagePrice();
                packagePrice.setPackageId(resultSet.getInt("PackageId"));
                packagePrice.setSubjectId(resultSet.getInt("SubjectId"));
                packagePrice.setName(resultSet.getString("name"));
                packagePrice.setDurationTime(resultSet.getInt("duration_time"));
                packagePrice.setSalePrice(resultSet.getDouble("sale_price"));
                packagePrice.setPrice(resultSet.getDouble("price"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return packagePrice; // Return the single PackagePrice object or null if not found
    }
    //find lowest option
    public double findLowestPrice(List<PackagePrice> packagePrices) {
        double lowestPrice = Double.MAX_VALUE;
        for (PackagePrice packagePrice : packagePrices) {
            double salePrice = packagePrice.getSalePrice();
            if (salePrice < lowestPrice) {
                lowestPrice = salePrice;
            }
            double originalPrice = packagePrice.getPrice();
            if (originalPrice < lowestPrice) {
                lowestPrice = originalPrice;
            }
        }
        return lowestPrice == Double.MAX_VALUE ? 0 : lowestPrice;
    }
    //get name of package
    public String getNameByPackageId(int packageId) {
        String name = "";

        try {
            String query = "SELECT Name FROM Package_Price WHERE PackageID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, packageId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        name = resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
    //get duration of package
    public int getDurationByPackageId(int packageId) {
        int durationTime = 0;

        String query = "SELECT Duration_Time FROM Package_Price WHERE PackageID = ?";
        try (
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, packageId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    durationTime = resultSet.getInt("Duration_Time");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return durationTime;
    }
    //get price 
    public PackagePrice getPriceByPackageId(int packageId) {
        String query = "SELECT Price, Sale_Price FROM Package_Price WHERE PackageID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, packageId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    PackagePrice packagePrice = new PackagePrice();
                    packagePrice.setPrice(rs.getDouble("Price"));      
                    packagePrice.setSalePrice(rs.getDouble("Sale_Price")); 
                    return packagePrice;  // Return the PackagePrice object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no result is found
    }
    public void addPackagePrice(PackagePrice packagePrice) {
        String query = "INSERT INTO Package_Price (SubjectID, Name, Duration_Time, Sale_Price, Price) VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, packagePrice.getSubjectId());
            preparedStatement.setString(2, packagePrice.getName());
            preparedStatement.setInt(3, packagePrice.getDurationTime());
            preparedStatement.setDouble(4, packagePrice.getSalePrice());
            preparedStatement.setDouble(5, packagePrice.getPrice());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePackagePrice(PackagePrice packagePrice) {
        String query = "UPDATE Package_Price SET SubjectID = ?, Name = ?, Duration_time = ?, Sale_price = ?, Price = ? WHERE PackageID = ?";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, packagePrice.getSubjectId());
            preparedStatement.setString(2, packagePrice.getName());
            preparedStatement.setInt(3, packagePrice.getDurationTime());
            preparedStatement.setDouble(4, packagePrice.getSalePrice());
            preparedStatement.setDouble(5, packagePrice.getPrice());
            preparedStatement.setInt(6, packagePrice.getPackageId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deletePackagePrice(int packagePriceId) {
        String query = "DELETE FROM Package_Price WHERE PackageID = ?";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, packagePriceId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to get Price by SubjectID
    public Double getPriceBySubjectID(int subjectId) {
    String sql = "SELECT Price FROM Package_Price WHERE SubjectID = ?";
    
    try (
         PreparedStatement pst = connection.prepareStatement(sql)) {
        
        pst.setInt(1, subjectId);
        
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getDouble("Price");  // Returns the price if found
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    // Return null if no price is found
    return null;
}
 public boolean updateSalePriceBySubjectID(int subjectID, double newSalePrice) {
        String sql = "UPDATE Package_Price SET Sale_Price = ? WHERE SubjectID = ?";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set the parameters
            stmt.setDouble(1, newSalePrice);
            stmt.setInt(2, subjectID);

            // Execute the update
            int rowsUpdated = stmt.executeUpdate();

            // Return true if at least one row was updated
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


