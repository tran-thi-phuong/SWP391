/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO; // Importing PagesDAO to access page information
import dal.RegistrationsDAO; // Importing RegistrationsDAO to manage course registrations
import dal.RolePermissionDAO; // Importing RolePermissionDAO to handle user permissions
import model.Registrations; // Importing the Registrations model class
import jakarta.servlet.ServletException; // Importing ServletException for handling servlet errors
import jakarta.servlet.http.HttpServlet; // Importing HttpServlet for creating HTTP servlets
import jakarta.servlet.http.HttpServletRequest; // Importing HttpServletRequest to handle requests
import jakarta.servlet.http.HttpServletResponse; // Importing HttpServletResponse to handle responses
import jakarta.servlet.http.HttpSession; // Importing HttpSession to manage user sessions
import java.io.IOException; // Importing IOException for input/output errors
import java.util.List; // Importing List for using collections of Registrations objects
import model.Users; // Importing the Users model class

public class myCourse extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Uncomment to enable permission checking
        if (!hasPermission(request, response)) {
            return;
        }
        
        // Get the current session and the logged-in user
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        int userId = user.getUserID(); // Retrieve the user ID from the session

        // Retrieve search query and pagination parameters from the request
        String searchQuery = request.getParameter("searchQuery");
        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");

        // Maintain search query across requests
        if (searchQuery == null) {
            searchQuery = (String) session.getAttribute("searchQuery");
        }
        // Maintain page size across requests
        if (pageSizeStr == null) {
            pageSizeStr = (String) session.getAttribute("pageSizeStr");
        }

        // Store the current values in session for future requests
        session.setAttribute("searchQuery", searchQuery);
        session.setAttribute("pageSizeStr", pageSizeStr);

        int PAGE_SIZE = 5; // Default page size
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            try {
                PAGE_SIZE = Integer.parseInt(pageSizeStr); // Parse the page size if provided
            } catch (NumberFormatException e) {
                PAGE_SIZE = 5; // Reset to default if parsing fails
            }
        }

        // Determine the current page
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr); // Parse the current page number
            } catch (NumberFormatException e) {
                page = 1; // Reset to page 1 if parsing fails
            }
        }

        // Initialize RegistrationsDAO to access course data
        RegistrationsDAO course = new RegistrationsDAO();
        List<Registrations> courseList;
        int totalCourse;

        // Search for courses if a search query is provided
        if (searchQuery != null && !searchQuery.isEmpty()) {
            courseList = course.searchCourseByUserId(userId, searchQuery, page, PAGE_SIZE);
            totalCourse = course.getTotalSearchResultsCourseByUserId(userId, searchQuery); // Get total results based on search
        } else {
            // Retrieve courses for the user without search
            courseList = course.getCourseByUserId(userId, page, PAGE_SIZE);
            totalCourse = course.getTotalCourseByUserId(userId); // Get total courses for the user
        }

        // Calculate total pages for pagination
        int totalPages = (int) Math.ceil((double) totalCourse / PAGE_SIZE);
        
        // Set attributes for the JSP
        request.setAttribute("courseList", courseList); // List of courses
        request.setAttribute("searchQuery", searchQuery); // Current search query
        request.setAttribute("currentPage", page); // Current page number
        request.setAttribute("pageSize", PAGE_SIZE); // Current page size
        request.setAttribute("totalPages", totalPages); // Total number of pages

        // Forward the request to the myCourse.jsp page
        request.getRequestDispatcher("myCourse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Currently, no POST logic implemented; can delegate to doGet if needed
    }

    // Method to check user permissions for accessing the servlet
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        // Check if user is logged in; if not, redirect to login page
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false; // User is not logged in
        }

        // Retrieve user role and check permissions
        String userRole = currentUser.getRole();
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

        // If user is logged in but lacks permission, redirect to home page
        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage");
            return false; // User lacks permission
        } else if (pageID == null) {
            // If no page found in permission system, redirect to error page
            response.sendRedirect("error.jsp");
            return false; // No page found
        }

        return true; // User has access to this page
    }
}
