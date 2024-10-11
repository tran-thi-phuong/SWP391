package controller;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;

public class CompleteProfile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        // Validate the token
        if (token == null || token.isEmpty()) {
            request.setAttribute("message", "Invalid or expired reset link.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            return;
        }

        // Store the token in the session
        HttpSession session = request.getSession();
        session.setAttribute("resetToken", token);

        // Forward to CompleteProfile.jsp
        request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    String token = (String) session.getAttribute("resetToken");

    // Gather parameters
    String fullname = request.getParameter("name");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String re_password = request.getParameter("repassword");
    String phone = request.getParameter("phone");
    String address = request.getParameter("address");
    String gender = request.getParameter("gender");

    // Validate required fields
    if (token == null || token.isEmpty() || password == null || password.isEmpty() || re_password == null || re_password.isEmpty()) {
        request.setAttribute("message", "Invalid request.");
        request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
        return; // Early return on error
    }

    // Keep the token in the session for the next round
    session.setAttribute("resetToken", token);

    // Validate passwords
    if (!password.equals(re_password)) {
        request.setAttribute("passNotMatch", true);
        request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
        return; // Early return on error
    }

    // Check for unique username and phone
    UserDAO uDAO = new UserDAO();
    Users user = uDAO.getUserByToken(token);
    Users usernameCheck = uDAO.getUserByInfo("Username", username);
    Users phoneCheck = uDAO.getUserByInfo("Phone", phone);

    if (usernameCheck != null) {
        request.setAttribute("invalidUsername", true);
        request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
        return; // Early return on error
    }
    if (phoneCheck != null && phone != null) {
        request.setAttribute("invalidPhone", true);
        request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
        return; // Early return on error
    }

    // Update user profile
    if (user != null) {
        boolean isUpdated = uDAO.completeProfile(fullname, username, password, phone, address, gender, token);
        if (isUpdated) {
            uDAO.clearResetToken(token);
            session.removeAttribute("resetToken");
            request.setAttribute("message", "Your profile has been updated successfully. Please login to start a new experience.");
        } else {
            request.setAttribute("message", "Failed to update your profile.");
        }
    } else {
        request.setAttribute("message", "User not found or invalid token.");
    }

    request.getRequestDispatcher("/CompleteProfile.jsp").forward(request, response);
}

}
