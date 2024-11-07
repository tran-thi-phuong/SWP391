/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.RegistrationsDAO;
import dal.RolePermissionDAO;
import model.Registrations;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Users;

public class myRegistration extends HttpServlet {

    // Handles GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //the permission check
         if (!hasPermission(request, response)) {
             return;
         }

        // Get the session and the logged-in user
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        int userId = user.getUserID(); // Retrieve user ID from the session

        // Retrieve search and filter parameters from request or session
        String searchQuery = request.getParameter("searchQuery");
        String statusFilter = request.getParameter("status");
        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");

        // If search query is not provided, check the session for previous value
        if (searchQuery == null) {
            searchQuery = (String) session.getAttribute("searchQuery");
        }
        // If status filter is not provided, check the session for previous value
        if (statusFilter == null) {
            statusFilter = (String) session.getAttribute("statusFilter");
        }
        // If page size is not provided, check the session for previous value
        if (pageSizeStr == null) {
            pageSizeStr = (String) session.getAttribute("pageSizeStr");
        }

        // Store current search and filter values in session
        session.setAttribute("searchQuery", searchQuery);
        session.setAttribute("statusFilter", statusFilter);
        session.setAttribute("pageSizeStr", pageSizeStr);

        // Set default page size
        int PAGE_SIZE = 5;
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            try {
                PAGE_SIZE = Integer.parseInt(pageSizeStr); // Parse page size
            } catch (NumberFormatException e) {
                PAGE_SIZE = 5; // Reset to default if parsing fails
            }
        }

        // Set default page number
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr); // Parse page number
            } catch (NumberFormatException e) {
                page = 1; // Reset to default if parsing fails
            }
        }

        // Create DAO instance for registrations
        RegistrationsDAO registrationsDAO = new RegistrationsDAO();
        List<Registrations> registrationList; // List to hold registration data
        int totalRegistrations; // Variable to hold total registrations

        // Retrieve registrations based on status filter, search query, or pagination
        if (statusFilter != null && !statusFilter.isEmpty() && !"All".equals(statusFilter)) {
            registrationList = registrationsDAO.getRegistrationsByUserIdAndStatus(userId, statusFilter, page,
                    PAGE_SIZE);
            totalRegistrations = registrationsDAO.getTotalRegistrationsByUserIdAndStatus(userId, statusFilter);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            registrationList = registrationsDAO.searchRegistrationsByUserId(userId, searchQuery, page, PAGE_SIZE);
            totalRegistrations = registrationsDAO.getTotalSearchResultsByUserId(userId, searchQuery);
        } else {
            registrationList = registrationsDAO.getRegistrationsByUserId(userId, page, PAGE_SIZE);
            totalRegistrations = registrationsDAO.getTotalRegistrationsByUserId(userId);
        }

        // Calculate total number of pages for pagination
        int totalPages = (int) Math.ceil((double) totalRegistrations / PAGE_SIZE);

        // Set attributes for JSP to display
        request.setAttribute("registrationList", registrationList);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("pageSizeStr", pageSizeStr);
        request.setAttribute("totalPages", totalPages);

        // Forward request to myRegistration.jsp for rendering
        request.getRequestDispatcher("myRegistration.jsp").forward(request, response);
    }

    // Handles POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action"); // Determine the action (e.g., cancel)
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        int PAGE_SIZE = 5; // Default page size
        int page = 1; // Default page number

        int userId = user.getUserID(); // Get user ID from session

        // If the action is to cancel a registration
        if ("cancel".equals(action)) {
            String registrationIdStr = request.getParameter("registrationId"); // Get registration ID from request
            int registrationId = Integer.parseInt(registrationIdStr); // Parse registration ID
            RegistrationsDAO registrationsDAO = new RegistrationsDAO(); // Create DAO instance
            int totalRegistrations;
            totalRegistrations = registrationsDAO.getTotalRegistrationsByUserId(userId); // Get total registrations for user
            
            // Cancel the registration
            boolean isCancelled = registrationsDAO.updateStatusToCancelled(registrationId);

            // If cancellation is successful, update the registration list
            if (isCancelled) {
                List<Registrations> registrationList = registrationsDAO.getRegistrationsByUserId(userId, page,
                        PAGE_SIZE);
                request.setAttribute("registrationList", registrationList);
                request.setAttribute("message", "Registration cancelled successfully."); // Success message
            } else {
                request.setAttribute("message", "Failed to cancel registration."); // Failure message
            }
            int totalPages = (int) Math.ceil((double) totalRegistrations / PAGE_SIZE); // Calculate total pages
            request.setAttribute("totalPages", totalPages);
        }

        // Set attributes for page size and current page
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("currentPage", page);

        // Forward request to myRegistration.jsp for rendering
        request.getRequestDispatcher("myRegistration.jsp").forward(request, response);
    }

    // Check if the user has permission to access the page
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
