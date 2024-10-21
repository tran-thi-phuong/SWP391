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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("resetToken");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("message", "Invalid request.");
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        Users user = userDAO.getUserByToken(token);

        if (user != null) {
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("message", "The new passwords do not match.");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                return;
            }
            if (newPassword.equals(user.getPassword())) {
                request.setAttribute("message", "The new passwords is same with old password.");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                return;
            }
            if (!isValidPassword(newPassword)) {
                request.setAttribute("message", "The new password must be 8-16 characters long, start with an uppercase letter, and contain at least one special character.");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                return;
            }
            userDAO.updatePassword(user.getToken(), newPassword);
            userDAO.clearResetToken(token);
            request.setAttribute("message", "Your password has been reset successfully.");
            session.removeAttribute("resetToken");
        } else {
            request.setAttribute("message", "User not found or invalid token.");
        }

        request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }
        if (!Character.isUpperCase(password.charAt(0))) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return false;
        }
        return true;
    }
}
