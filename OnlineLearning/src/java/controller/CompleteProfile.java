package controller;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
@WebServlet(name = "CompleteProfile", urlPatterns = {"/CompleteProfile"})
public class CompleteProfile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the token from the request parameter
        String token = request.getParameter("token");

        // Validate the token
        if (token == null || token.isEmpty()) {
            // If token is invalid or expired, forward to error page with message
            request.setAttribute("message", "Invalid or expired reset link.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            return;
        }

        // Store the token in the session
        HttpSession session = request.getSession();
        session.setAttribute("resetToken", token);
                

        // Forward the request to CompleteProfile.jsp
        request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the session
        HttpSession session = request.getSession();
        // Retrieve the token from the session
        String token = (String) session.getAttribute("resetToken");

        // Gather parameters from the request
        String fullname = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String re_password = request.getParameter("repassword");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");

        // Validate required fields
        if (token == null || token.isEmpty() || password == null || password.isEmpty() || re_password == null || re_password.isEmpty()) {
            // If any required fields are missing, forward to CompleteProfile.jsp with error message
            request.setAttribute("message", "Invalid request.");
            request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
            return; // Early return on error
        }

        // Keep the token in the session for the next round
        session.setAttribute("resetToken", token);

        // Validate passwords
        if (!password.equals(re_password)) {
            // If passwords do not match, forward to CompleteProfile.jsp with error message
            request.setAttribute("passNotMatch", true);
            request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
            return; // Early return on error
        }

        // Initialize UserDAO to interact with the database
        UserDAO uDAO = new UserDAO();
        // Retrieve user by token
        Users user = uDAO.getUserByToken(token);
        // Check if the username is unique
        Users usernameCheck = uDAO.getUserByInfo("Username", username);
        // Check if the phone number is unique
        Users phoneCheck = uDAO.getUserByInfo("Phone", phone);

        // Validate username uniqueness
        if (usernameCheck != null) {
            // If username is not unique, forward to CompleteProfile.jsp with error message
            request.setAttribute("invalidUsername", true);
            request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
            return; // Early return on error
        }
        // Validate phone number uniqueness
        if (phoneCheck != null && phone != null) {
            // If phone number is not unique, forward to CompleteProfile.jsp with error message
            request.setAttribute("invalidPhone", true);
            request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
            return; // Early return on error
        }

        // Update user profile if user exists
        if (user != null) {
            // Perform profile update
            boolean isUpdated = uDAO.completeProfile(fullname, username, password, phone, address, gender, token);
            if (isUpdated) {
                // Clear reset token if update is successful
                uDAO.clearResetToken(token);
                // Remove the reset token from the session
                session.removeAttribute("resetToken");
                // Set success message
                request.setAttribute("message", "Your profile has been updated successfully. Please login to start a new experience.");
            } else {
                // Set failure message if update fails
                request.setAttribute("message", "Failed to update your profile.");
            }
        } else {
            // Set error message if user is not found or token is invalid
            request.setAttribute("message", "User not found or invalid token.");
        }

        // Forward the request to CompleteProfile.jsp
        request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
    }
}
