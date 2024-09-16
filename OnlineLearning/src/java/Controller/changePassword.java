package Controller;

import dal.UserDAO;
import Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class changePassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");

        UserDAO dao = new UserDAO();
        User user = dao.getUserByUsername(username);

        if (user == null || !user.getPassword().equals(oldPassword)) {
            request.setAttribute("errorMessage", "Your old password is incorrect");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        if (oldPassword.equals(newPassword)) {
            request.setAttribute("errorMessage", "The new password cannot be the same as the old password");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            request.setAttribute("errorMessage", "The new passwords do not match");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        // Validate new password
        if (!isValidPassword(newPassword)) {
            request.setAttribute("errorMessage", "The new password must be 8-16 characters long, start with an uppercase letter, and contain at least one special character");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        user.setPassword(newPassword);
        boolean isUpdated = dao.updateUserPassword(user);

        if (isUpdated) {
            request.setAttribute("successMessage", "Password changed successfully!");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Failed to update password");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        }
    }

    // Method to validate password
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
