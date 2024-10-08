package controller;

import dal.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddRegistrationBySale extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddRegistrationBySale.class.getName());
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    private final UserDAO userDAO = new UserDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final PackagePriceDAO packageDAO = new PackagePriceDAO();

    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return result.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectIdStr = request.getParameter("subjectID");
        String packageIdStr = request.getParameter("packageID"); // Lấy packageID
        int subjectID = 0;
        int packageId = 0;

        // Parse subjectID
        if (subjectIdStr != null) {
            try {
                subjectID = Integer.parseInt(subjectIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (packageIdStr != null) {
            try {
                packageId = Integer.parseInt(packageIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Map<Integer, Subject> subjects = subjectDAO.getAllSubject();
        List<PackagePrice> prices = packageDAO.searchBySubjectId(subjectID);
        PackagePrice packagePrice = packageDAO.getPriceByPackageId(packageId);

        // Calculate total cost
        double totalCost = 0.0;

        if (packagePrice != null) {
            Double price = packagePrice.getPrice();
            Double salePrice = packagePrice.getSalePrice();

            // Check if price is not null
            if (price != null) {
                if (salePrice != null) {
                    totalCost = Math.min(price, salePrice); // Use sale price if available
                } else {
                    totalCost = price; // Use price if sale price is null
                }
            }
        }

        // Set attributes to request
        request.setAttribute("subject", subjects);
        request.setAttribute("price", prices);
        request.setAttribute("selectedSubjectID", subjectID);
        request.setAttribute("totalCost", totalCost); // Add total cost to the request

        // Forward to JSP
        request.getRequestDispatcher("AddRegistrationBySale.jsp").forward(request, response);
    }

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        String email = request.getParameter("email");
        int subjectId = Integer.parseInt(request.getParameter("subjectID"));
        int packageId = Integer.parseInt(request.getParameter("packageID")); 
        double totalCost = Double.parseDouble(request.getParameter("totalCost"));
        String fullname = request.getParameter("full-name");
        String phone = request.getParameter("mobile");
        String gender = request.getParameter("gender");
        String note = request.getParameter("note");

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user"); 
        
        // Check if the user object is null
        if (user == null) {
            request.setAttribute("errorMessage", "User not found in session.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Retrieve the StaffID from the user object
        Integer staffId = user.getUserID();
        if (staffId == null) {
            request.setAttribute("errorMessage", "Staff ID is null.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Check if the user exists
        Users existingUser = userDAO.getUserByEmail(email);
        int userId;

        if (existingUser != null) {
            // User exists, get the user ID
            userId = existingUser.getUserID();
        } else {
            
            userId = userDAO.addUser(email, phone, gender, phone, generateRandomString(0));
        }

        // Add the registration
        addRegistration(userId, subjectId, packageId, totalCost, staffId, note);

        // Redirect to the registration list page after successful processing
        response.sendRedirect(request.getContextPath() + "/listRegistration");
    } catch (NumberFormatException e) {
        // Handle invalid number format
        request.setAttribute("errorMessage", "Invalid number format in form data.");
        request.getRequestDispatcher("error.jsp").forward(request, response);
    } catch (SQLException e) {
        // Handle database errors
        request.setAttribute("errorMessage", "Database error occurred: " + e.getMessage());
        request.getRequestDispatcher("error.jsp").forward(request, response);
    } catch (Exception e) {
        // Handle general errors
        request.setAttribute("errorMessage", "Error processing registration: " + e.getMessage());
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}

private void addRegistration(int userId, int subjectId, int packageId, double totalCost, Integer staffId, String note) throws SQLException {
    Date registrationTime = new Date();
    Date validFrom = registrationTime;
    int durationDays = packageDAO.getDurationByPackageId(packageId);
    Date validTo = new Date(validFrom.getTime() + durationDays * 24 * 60 * 60 * 1000L);

    registrationsDAO.addNewRegistration(userId, subjectId, packageId, totalCost,
            registrationTime, validFrom, validTo, staffId, note);
}

   
}
