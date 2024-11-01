package controller;

import model.Users;
import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class findAccount extends HttpServlet {

    // Handles POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the email parameter from the request and trim whitespace
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";

        // Check if the email input is empty
        if (email.isEmpty()) {
            // Set an error message for empty input
            request.setAttribute("errorMessage", "Email field cannot be empty.");
            // Forward the request to findAccount.jsp
            request.getRequestDispatcher("/findAccount.jsp").forward(request, response);
            return;
        }

        // Instantiate the UserDAO to interact with the database
        UserDAO userDAO = new UserDAO();
        // Retrieve the user by the provided email
        Users user = userDAO.getUserByEmail(email);

        // Check if the user exists
        if (user != null) {
            // Store the email in the session
            HttpSession session = request.getSession();
            session.setAttribute("resetEmail", email);

            // Set attributes to display user details on the recoverAccount.jsp page
            request.setAttribute("email", user.getEmail());
            request.setAttribute("username", user.getUsername());
            // Check if the user has an avatar, otherwise set a default avatar image
            request.setAttribute("avatarUrl", (user.getAvatar() != null && !user.getAvatar().isEmpty())
                ? user.getAvatar()
                : "images/default-avatar.jpg");

            // Forward the request to recoverAccount.jsp
            request.getRequestDispatcher("/recoverAccount.jsp").forward(request, response);
        } else {
            // Set an error message if no account is found with the provided email
            request.setAttribute("errorMessage", "No account found with this email. Please try again with another email.");
            // Forward the request to findAccount.jsp
            request.getRequestDispatcher("/findAccount.jsp").forward(request, response);
        }
    }
}
