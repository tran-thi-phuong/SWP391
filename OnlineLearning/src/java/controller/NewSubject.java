/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import dal.UserDAO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.sql.SQLException;
import java.util.List;
import model.SubjectCategory;
import model.Users;

/**
 *
 * @author Admin
 */
@MultipartConfig
public class NewSubject extends HttpServlet {

    private CategoryDAO categoryDAO; // DAO for managing categories
    private UserDAO UserDAO; // DAO for managing users
    private static final String UPLOAD_DIR = "images"; // Directory for storing uploaded images

    // Initializes the DAOs when the servlet is created
    @Override
    public void init() throws ServletException {
        super.init();
        categoryDAO = new CategoryDAO(); // Initialize the category DAO
        UserDAO = new UserDAO(); // Initialize the user DAO
    }

    /**
     * Handles GET requests to display the form for creating a new subject.
     * Loads all available categories for the dropdown menu.
     *
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @throws ServletException If a servlet exception occurs
     * @throws IOException If an IO exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the user has permission to access this page
        if (!hasPermission(request, response)) {
            return;
        }
        try {
            // Get all categories and instructors from the database
            List<SubjectCategory> categories = categoryDAO.getAllCategories();
            List<Users> instructor = UserDAO.getInstructors(); // Get list of instructors
            request.setAttribute("Instructor", instructor); // Set instructors in the request attribute
            request.setAttribute("categories", categories); // Set categories in the request attribute
            request.getRequestDispatcher("newSubject.jsp").forward(request, response); // Forward to the JSP page
        } catch (ServletException | IOException ex) {
            throw new ServletException(ex); // Handle exception
        }
    }

    /**
     * Handles POST requests to process the form submission for creating a new
     * subject.
     *
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @throws ServletException If a servlet exception occurs
     * @throws IOException If an IO exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form parameters for creating a new subject
        String courseName = request.getParameter("courseName");
        String category = request.getParameter("category");
        String status = request.getParameter("status");
        String description = request.getParameter("description");
        String instructor = request.getParameter("instructor");

        // Process the uploaded file (thumbnail image)
        Part filePart = request.getPart("thumbnail"); // Get the uploaded file (thumbnail)
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Extract file name
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR; // Get upload directory path

        // Create the uploads directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the uploaded file to the specified directory
        File file = new File(uploadDir, fileName);
        filePart.write(file.getAbsolutePath()); // Write the file to disk
        String filePath = UPLOAD_DIR + "/" + fileName; // Set the file path for database storage

        // After saving the file, save the subject data to the database
        SubjectDAO subjectDAO = new SubjectDAO();
        int newSubjectId = subjectDAO.addSubject(courseName, category, status, description, filePath, instructor); // Add subject to DB

        if (newSubjectId > 0) {
            // If the subject is successfully added, redirect to the subject details page
            response.sendRedirect("SubjectDetailOverview?id=" + newSubjectId);
        } else {
            // If there is an error, set an error message and forward back to the subject creation page
            request.setAttribute("errorMessage", "Failed to add the course.");
            request.getRequestDispatcher("newSubject").forward(request, response);
        }
    }

    /**
     * Checks if the user has permission to access the page.
     *
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @return true if the user has permission, false otherwise
     * @throws IOException If an I/O error occurs
     */
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user"); // Get current user from session

        // If the user is not logged in, redirect to login page
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        // Get the user's role and check if they have permission to access this page
        String userRole = currentUser.getRole();
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString()); // Get page ID

        // If the user does not have permission, redirect to homepage
        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage");
            return false;
        } else if (pageID == null) {
            // If no page ID is found, redirect to error page
            response.sendRedirect("error.jsp");
            return false;
        }

        return true; // User has permission to access the page
    }
}
