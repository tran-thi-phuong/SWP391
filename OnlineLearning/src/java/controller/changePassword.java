package controller;

import dal.UserDAO;
import model.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class changePassword extends HttpServlet {
    
    private static final int MAX_ATTEMPTS = 5; // Maximum allowed attempts to enter the correct password

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Retrieve the current session
    HttpSession session = request.getSession();
    // Get the user object from the session
    Users user = (Users) session.getAttribute("user");

    // If user is not logged in, redirect to the login page
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Retrieve and trim parameters from the request
    String username = user.getUsername();
    String oldPassword = request.getParameter("oldPassword") != null ? request.getParameter("oldPassword").trim() : "";
    String newPassword = request.getParameter("newPassword") != null ? request.getParameter("newPassword").trim() : "";
    String confirmNewPassword = request.getParameter("confirmNewPassword") != null ? request.getParameter("confirmNewPassword").trim() : "";

    // Check for empty values
    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
        session.setAttribute("errorMessage", "All fields are required.");
        response.sendRedirect("profile.jsp");
        return;
    }

    // Get the number of attempts from the session, defaulting to 0 if not present
    Integer attempts = (Integer) session.getAttribute("attempts");
    if (attempts == null) {
        attempts = 0;
    }

    // Create an instance of UserDAO to interact with the database
    UserDAO dao = new UserDAO();
    // Retrieve the user from the database by username
    Users dbUser = dao.getUserByUsername(username);

    // Check if the provided old password matches the password in the database
    if (dbUser == null || !dbUser.getPassword().equals(oldPassword)) {
        // Increment the attempts count
        attempts++;
        session.setAttribute("attempts", attempts);

        // Lock the account if maximum attempts are exceeded
        if (attempts >= MAX_ATTEMPTS) {
            user.setStatus("deactive");
            dao.updateUserStatus(user);
            session.invalidate(); // Invalidate the session
            response.sendRedirect("login.jsp");
            return;
        }

        // Set error message and redirect to profile page if password is incorrect
        session.setAttribute("errorMessage", "Your old password is incorrect. If you enter the wrong password more than 5 times, your account will be locked. Attempt " + attempts + " of " + MAX_ATTEMPTS);
        response.sendRedirect("profile.jsp");
        return;
    }

    // Check if the new password is the same as the old password
    if (oldPassword.equals(newPassword)) {
        session.setAttribute("errorMessage", "The new password cannot be the same as the old password");
        response.sendRedirect("profile.jsp");
        return;
    }

    // Check if the new password matches the confirmation password
    if (!newPassword.equals(confirmNewPassword)) {
        session.setAttribute("errorMessage", "The new passwords do not match");
        response.sendRedirect("profile.jsp");
        return;
    }

    // Validate the new password
    if (!isValidPassword(newPassword)) {
        session.setAttribute("errorMessage", "The new password must be 8-16 characters long, start with an uppercase letter, and contain at least one special character");
        response.sendRedirect("profile.jsp");
        return;
    }

    // Update the user's password in the database
    dbUser.setPassword(newPassword);
    boolean isUpdated = dao.updateUserPassword(dbUser);
    if (isUpdated) {
        // Set success message and clear attempts count on successful password update
        session.setAttribute("successMessage", "Password changed successfully!");
        session.removeAttribute("attempts");
        response.sendRedirect("profile.jsp");
    } else {
        // Set error message if password update fails
        session.setAttribute("errorMessage", "Failed to update password");
        response.sendRedirect("profile.jsp");
    }
}


    // Helper method to validate the password
    private boolean isValidPassword(String password) {
        // Password must be between 8 and 16 characters long
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }
        // Password must start with an uppercase letter
        if (!Character.isUpperCase(password.charAt(0))) {
            return false;
        }
        // Password must contain at least one special character
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return false;
        }
        return true;
    }
}
