package controller;

import dal.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import model.Pages;
import model.Users;

/**
 * Servlet to handle role-based permissions for various pages in the application.
 * It allows for the addition of new pages and updating permissions for existing roles.
 * 
 * Author: Admin
 */
@WebServlet(name = "RolePermission", urlPatterns = {"/RolePermission"})
public class RolePermission extends HttpServlet {

    private RolePermissionDAO rolePermissionDAO; // DAO for handling role permissions
    private PagesDAO pagesDAO; // DAO for handling page data

    @Override
    public void init() throws ServletException {
        // Initialize the DAOs for use in this servlet
        rolePermissionDAO = new RolePermissionDAO();
        pagesDAO = new PagesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user has permission to access this page
        if (!hasPermission(request, response)) {
            return; // If no permission, exit the method
        }
        
        // Retrieve all pages from the database
        List<Pages> allPages = pagesDAO.getAllPages();
        request.setAttribute("allPages", allPages); // Set the pages list as a request attribute
        request.setAttribute("rolePermissionDAO", rolePermissionDAO); // Pass the rolePermissionDAO to the JSP

        // Check for any messages to display
        String message = (String) request.getAttribute("message");
        if (message != null) {
            request.setAttribute("message", message);
        }

        // Forward the request to the rolePermission.jsp page
        request.getRequestDispatcher("rolePermission.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the action parameter from the request
        String action = request.getParameter("action");

        // Handle adding a new page
        if ("add".equals(action)) {
            handleAddPage(request, response);
        } 
        // Handle updating multiple permissions
        else if ("updateMultiple".equals(action)) {
            handleUpdateMultiplePermissions(request, response);
        }
    }

    /**
     * Handles the addition of a new page.
     */
    private void handleAddPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("page");
        String pageURL = request.getParameter("pageURL");
        String status = request.getParameter("status");

        // Attempt to add the new page
        boolean isAdded = pagesDAO.addPage(page, pageURL, status);
        if (isAdded) {
            request.setAttribute("message", "Page added successfully!");
        } else {
            request.setAttribute("message", "Error: Unable to add page.");
        }

        // Redirect back to the doGet method to display the updated list
        doGet(request, response);
    }

    /**
     * Handles updating multiple role permissions based on submitted data.
     */
    private void handleUpdateMultiplePermissions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Delete all existing role permissions before updating
        rolePermissionDAO.deleteAllRolePermissions();
        Map<String, String[]> roleParams = request.getParameterMap(); // Get all submitted parameters

        boolean updateSuccess = true; // Flag to track success of updates
        // Iterate through submitted parameters to update permissions
        for (String param : roleParams.keySet()) {
            if (param.startsWith("roles[")) { // Check if parameter is for role permissions
                int pageId = Integer.parseInt(param.substring(6, param.length() - 1)); // Extract page ID
                String[] roles = roleParams.get(param); // Get associated roles

                // Add each role permission for the specified page
                for (String role : roles) {
                    boolean added = rolePermissionDAO.addRolePermission(role, pageId);
                    if (!added) {
                        updateSuccess = false; // If any permission fails to add, mark as failure
                    }
                }
            }
        }

        // Set success or error message based on update result
        if (updateSuccess) {
            request.setAttribute("message", "Permissions updated successfully!");
        } else {
            request.setAttribute("message", "Error: Some permissions were not updated.");
        }
        doGet(request, response); // Redirect back to the list view
    }

    /**
     * Checks if the user has permission to access the current page.
     */
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        // Redirect to login if user is not logged in
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        // Check user role and page ID for access validation
        String userRole = currentUser.getRole();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

        // Redirect to homepage if user does not have permission
        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect("/Homepage");
            return false;
        } else if (pageID == null) {
            // Redirect to error page if page ID is not found
            response.sendRedirect("error.jsp");
            return false;
        }

        return true; // User has access to the page
    }
}
