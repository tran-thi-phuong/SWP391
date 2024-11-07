/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//data access
import dal.PackagePriceDAO;
import dal.RegistrationsDAO;
import dal.UserDAO;

//default sevlet
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//randomize
import java.security.SecureRandom;
//debug
import java.sql.SQLException;

//model
import model.PackagePrice;
import model.Users;

/**
 *
 * @author 84336
 */
@WebServlet(name = "payment", urlPatterns = {"/payment"})
public class payment extends HttpServlet {

    // Setup for random string generation (no changes here)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    // Generate random string for user (no changes here)
    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }
        return result.toString();
    }

    // Setup DAO (no changes here)
    RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    UserDAO userDAO = new UserDAO();
    PackagePriceDAO packagePriceDAO = new PackagePriceDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get package ID and other details from request
        int packageId = Integer.parseInt(request.getParameter("package"));
        String fullname = request.getParameter("full-name");
        String email = request.getParameter("email");
        String phone = request.getParameter("mobile");
        String gender = request.getParameter("gender");
        int subjectID = Integer.parseInt(request.getParameter("subjectID"));
        
        // Get the package price from DAO
        PackagePrice currentPac = packagePriceDAO.searchByPackagePriceId(packageId);
        double price = Math.min(currentPac.getSalePrice(), currentPac.getPrice());


        // Set up the session
        HttpSession session = request.getSession();
        session.setAttribute("price", price);  // Store the price for use in JSP
        try {
            // Check if the user exists
            Users user = (Users) session.getAttribute("user");
            int userID;
            if (user == null) {
                // Register a new user if none exists in session
                userID = userDAO.addUser(email, phone, gender, phone, generateRandomString(10));
                session.setAttribute("notification", "Registration successful! You have been registered for the course. We will send you an email for verification soon:3");
            } else {
                userID = user.getUserID();
                session.setAttribute("notification", "Registration successful! You have been registered for the course.");
            }
            
            // Add registration to the database
            registrationsDAO.addRegistration(userID, subjectID, packageId, price);
           
            // Redirect to the Payment page
            response.sendRedirect("Payment.jsp");
        } catch (SQLException ex) {
            session.setAttribute("notification", "Registration failed. Please try again.");
            response.sendRedirect("registerCourse?id=" + subjectID);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}


