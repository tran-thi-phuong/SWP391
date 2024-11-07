package controller;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/DeleteUser")
public class DeleteUser extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get userId from the request
        String userId = request.getParameter("userId");

        // Validate if userId is provided
        if (userId != null && !userId.isEmpty()) {
            try {
                // Call DAO to delete the user
                boolean isDeleted = userDAO.deleteUser(userId);

                // Set appropriate message based on result
                HttpSession session = request.getSession();
                if (isDeleted) {
                    session.setAttribute("successMessage", "User deleted successfully.");
                } else {
                    session.setAttribute("errorMessage", "This user cannot be deleted. They have interacted with the system.");
                }
            } catch (Exception e) {
                // Log the exception
                e.printStackTrace();

                // Set an error message in the session
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", "An error occurred while deleting the user. Please try again.");
            }
        } else {
            // If userId is not provided, set error message
            request.setAttribute("errorMessage", "User ID is required.");
        }

        // Forward the request to UserDetail.jsp
        request.getRequestDispatcher("UserDetail.jsp").forward(request, response);
    }
}
