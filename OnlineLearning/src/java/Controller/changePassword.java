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
    
    private static final int MAX_ATTEMPTS = 5;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        
        Integer attempts = (Integer) session.getAttribute("attempts");
        if (attempts == null) {
            attempts = 0;
        }

        UserDAO dao = new UserDAO();
        Users user = dao.getUserByUsername(username);

        if (user == null || !user.getPassword().equals(oldPassword)) {
            attempts++;
            session.setAttribute("attempts", attempts);

            if (attempts >= MAX_ATTEMPTS) {
                user.setStatus("deactive");
                dao.updateUserStatus(user);
                session.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }

            session.setAttribute("errorMessage", "Your old password is incorrect. If you enter the wrong password more than 5 times, your account will be locked"
                    + ". Attempt " + attempts + " of " + MAX_ATTEMPTS);
            response.sendRedirect("profile.jsp");
            return;
        }

        if (oldPassword.equals(newPassword)) {
            session.setAttribute("errorMessage", "The new password cannot be the same as the old password");
            response.sendRedirect("profile.jsp");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            session.setAttribute("errorMessage", "The new passwords do not match");
            response.sendRedirect("profile.jsp");
            return;
        }

        if (!isValidPassword(newPassword)) {
            session.setAttribute("errorMessage", "The new password must be 8-16 characters long, start with an uppercase letter, and contain at least one special character");
            response.sendRedirect("profile.jsp");
            return;
        }

        user.setPassword(newPassword);
        boolean isUpdated = dao.updateUserPassword(user);
        if (isUpdated) {
            session.setAttribute("successMessage", "Password changed successfully!");
            session.removeAttribute("attempts");
            response.sendRedirect("customer.jsp");
        } else {
            session.setAttribute("errorMessage", "Failed to update password");
            response.sendRedirect("customer.jsp");
        }
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
