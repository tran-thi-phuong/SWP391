package controller;

import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Users;

@WebServlet("/UpdateUser")
public class UpdateUser extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, response)) {
             return;
         }
        String userId = request.getParameter("userId");
        String role = request.getParameter("role");
        String status = request.getParameter("status");

        // Use UserDAO to update user details
        boolean isUpdated = userDAO.updateUser(userId, role, status);

        if (isUpdated) {
            request.setAttribute("successMessage", "Update information successful.");
            request.getRequestDispatcher("UserList").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Failed to update user details.");
            request.getRequestDispatcher("UserDetail.jsp").forward(request, response);
        }

    }
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user"); // Get current user from session

        // If the user is not logged in, redirect to the login page
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        // Get the user's role and check permissions for the current page
        String userRole = currentUser.getRole();
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

        // If the user is logged in but does not have permission, redirect to the homepage
        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage");
            return false;
        } else if (pageID == null) {
            // If the page ID is not found in the permission system, redirect to the error page
            response.sendRedirect("error.jsp");
            return false;
        }

        return true; // User has permission to access the page
    }
}
