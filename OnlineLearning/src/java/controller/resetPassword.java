package controller;

import model.Users;
import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class resetPassword extends HttpServlet {

    // Handles GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the token parameter from the request
        String token = request.getParameter("token");

        // Check if the token is null or empty
        if (token == null || token.isEmpty()) {
            // Set an error message attribute and forward to errorPage.jsp
            request.setAttribute("message", "Invalid or expired reset link.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            return;
        }

        // Store the token in the session
        HttpSession session = request.getSession();
        session.setAttribute("resetToken", token);

        // Forward to resetPassword.jsp
        request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
    }

    // Handles POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the session and the token from the session
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("resetToken");
        // Retrieve the new password and confirmation password from the request
        String newPassword = request.getParameter("newPassword") != null ? request.getParameter("newPassword").trim() : "";
        String confirmPassword = request.getParameter("confirmPassword")!= null ? request.getParameter("confirmPassword").trim() : "";

        // Check if any of the parameters are null or empty
        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            // Set an error message attribute and forward to resetPassword.jsp
            request.setAttribute("message", "Invalid request.");
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        // Instantiate the UserDAO to interact with the database
        UserDAO userDAO = new UserDAO();
        // Retrieve the user by the token
        Users user = userDAO.getUserByToken(token);

        // Check if the user exists
        if (user != null) {
            // Check if the new passwords match
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("message", "The new passwords do not match.");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                return;
            }
            // Check if the new password is the same as the old password
            if (newPassword.equals(user.getPassword())) {
                request.setAttribute("message", "The new passwords is same with old password.");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                return;
            }
            // Validate the new password
            if (!isValidPassword(newPassword)) {
                request.setAttribute("message", "The new password must be 8-16 characters long, start with an uppercase letter, and contain at least one special character.");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                return;
            }
            // Update the user's password and clear the reset token
            userDAO.updatePassword(user.getToken(), newPassword);
            userDAO.clearResetToken(token);
            // Set a success message and remove the reset token from the session
            request.setAttribute("message", "Your password has been reset successfully.");
            session.removeAttribute("resetToken");
        } else {
            // Set an error message if the user is not found or the token is invalid
            request.setAttribute("message", "User not found or invalid token.");
        }

        // Forward to resetPassword.jsp
        request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
    }

    // Helper method to validate the new password
    private boolean isValidPassword(String password) {
        // Check the length of the password
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }
        // Check if the first character is an uppercase letter
        if (!Character.isUpperCase(password.charAt(0))) {
            return false;
        }
        // Check if the password contains at least one special character
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return false;
        }
        return true;
    }
}
