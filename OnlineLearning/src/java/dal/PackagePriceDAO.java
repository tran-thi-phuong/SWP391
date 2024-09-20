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

    public List<PackagePrice> searchBySubjectId(int subjectId) {
        List<PackagePrice> packages = new ArrayList<>();
        String query = "SELECT * FROM Package_Price WHERE subjectId = ?"; 
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
    public PackagePrice searchByPackagePriceId(int packagePriceId) {
    PackagePrice packagePrice = null; // Initialize to null
    String query = "SELECT * FROM Package_Price WHERE PackageId = ?";
    
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
}
