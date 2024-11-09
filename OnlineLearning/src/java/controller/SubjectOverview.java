/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import dal.PackagePriceDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import model.PackagePrice;
import model.Subject;
import model.SubjectCategory;
import model.Users;

/**
 *
 * @author Admin
 */
@MultipartConfig
@WebServlet(urlPatterns = {"/SubjectDetailOverview"})
public class SubjectOverview extends HttpServlet {

    private static final String UPLOAD_DIR = "images"; // Directory to store uploaded images

    // Handles GET requests to display the details of a subject
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the user has permission to access this page
        if (!hasPermission(request, response)) {
            return;
        }

        String subjectId = request.getParameter("id"); // Get the subject ID from the request
        request.getSession().setAttribute("subjectID", subjectId); // Store the subject ID in the session

        // Retrieve the subject details from the database
        SubjectDAO sDAO = new SubjectDAO();
        Subject subject = sDAO.getSubjectById(Integer.parseInt(subjectId));
        request.setAttribute("subject", subject); // Set the subject in the request attribute

        // Retrieve all categories for the subject
        CategoryDAO cDAO = new CategoryDAO();
        List<SubjectCategory> categories = cDAO.getAllCategories();
        request.setAttribute("categories", categories); // Set the categories in the request attribute

        // Forward the request to the JSP page to display the subject details
        request.getRequestDispatcher("/SubjectDetailOverview.jsp").forward(request, response);
    }

    // Handles POST requests to update the subject details
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get data from the form submission
        String subjectId = request.getParameter("subjectId");
        String subjectName = request.getParameter("subjectName");
        String category = request.getParameter("category");
        String status = request.getParameter("status");
        String description = request.getParameter("description");
        Part filePart = request.getPart("thumbnail"); // Get the uploaded thumbnail image
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR; // Get the upload directory path
        String filePath = null;

        // If a new file is uploaded, save it to the server
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Create the upload directory if it doesn't exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the uploaded file
            File file = new File(uploadDir, fileName);
            filePart.write(file.getAbsolutePath());
            filePath = UPLOAD_DIR + "/" + fileName; // Set the file path for storage
        } else {
            // If no new file is uploaded, use the old file path
            filePath = request.getParameter("oldThumbnail");
        }

        try {
            // Update the subject details in the database
            SubjectDAO sDAO = new SubjectDAO();
            sDAO.updateSubject(subjectName, category, status, description, subjectId, filePath);
            response.sendRedirect("SubjectDetailOverview?id=" + subjectId); // Redirect to the updated subject page
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error saving subject: " + e.getMessage()); // Set error message if exception occurs
            request.getRequestDispatcher("/SubjectDetailOverview.jsp").forward(request, response); // Forward to the JSP page with the error message
        }
    }

    // Checks if the user has permission to access this page
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user"); // Get the current user from the session

        // If the user is not logged in, redirect to the login page
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString()); // Get the page ID from the URL
        String userRole = currentUser.getRole(); // Get the user's role

        // Check if the user has permission to access this page
        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage"); // Redirect to homepage if no permission
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp"); // Redirect to error page if no page ID found
            return false;
        }

        return true; // User has permission to access the page
    }
}
