/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Account;

/**
 *
 * @author 84336
 */
public class AccountDAO extends DBContext{
    //login method
    public Account login(String user, String pass) {
        String query = "select * from Account\n"
                + "where username = ?\n"
                + "and password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return new Account(rs.getString(1),
                        rs.getString(2));
                        
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    //test function
    public static void main(String[] args) {
        AccountDAO AccountDAO = new AccountDAO();
        Account a = AccountDAO.login("a@gmail.com","123");
        System.out.println(a);
    }
}
