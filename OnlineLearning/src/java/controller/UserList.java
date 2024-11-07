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
import java.util.List;
import java.util.logging.Level;
import model.Users;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns = {"/UserList"})
public class UserList extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UserList.class.getName());

    private final UserDAO userDAO = new UserDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user has permission to access this page
        if (!hasPermission(request, response)) {
            return; // Exit if the user lacks permission
        }

        // Retrieve filter parameters
        String gender = request.getParameter("gender");
        String role = request.getParameter("role");
        String status = request.getParameter("status");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Handle pagination parameters
        String currentPageParam = request.getParameter("page");
        int pageSize = 30; // Default page size if not set
        int currentPage = 1; // Default to the first page

        // Parse the current page number
        if (currentPageParam != null) {
            try {
                currentPage = Integer.parseInt(currentPageParam);
            } catch (NumberFormatException e) {
                currentPage = 1; // Default back to first page on error
            }
        }
        String sortColumn = request.getParameter("sortColumn");
        String sortOrder = request.getParameter("sortOrder");

// Default to sorting by ID if no column is specified
        if (sortColumn == null || sortColumn.isEmpty()) {
            sortColumn = "UserID";
        }
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "ASC";
        }
        try {
            // Fetch the users for the current page with applied filters
            List<Users> users = userDAO.getUsersByPage(currentPage, pageSize, gender, role, status, name, email, phone, sortColumn, sortOrder);

            // Get the total number of users based on filters
            int totalUsers = userDAO.getTotalUser(gender, role, status, name, email, phone);

            // Calculate total pages
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            if (totalPages < 1) {
                totalPages = 1; // Ensure at least one page is available
            }

            // Set the retrieved users and pagination info as request attributes
            request.setAttribute("users", users);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("error", request.getParameter("error"));
            request.setAttribute("success", request.getParameter("success"));

            // Forward the request to UserList.jsp to display the users
            request.getRequestDispatcher("UserList.jsp").forward(request, response);

        } catch (Exception e) {
            // Log the exception and redirect to error page
            LOGGER.log(Level.SEVERE, "Error retrieving users", e);
            response.sendRedirect("error.jsp?message=Error retrieving users");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        // Check if the user is logged in
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        // Check permissions
        String userRole = currentUser.getRole();
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage");
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true; // User has permission to access the page
    }
}
