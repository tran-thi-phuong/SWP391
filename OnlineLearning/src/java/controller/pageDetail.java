package controller;

import dal.PagesDAO; 
import dal.RolePermissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Pages; 
import model.Users;

import java.io.IOException;

/**
 * Servlet to handle the display and update of page details in the application.
 * It allows users to view specific page information and update it as necessary.
 */
@WebServlet("/pageDetail")
public class pageDetail extends HttpServlet {
    private PagesDAO pagesDAO; // DAO for handling page data

    @Override
    public void init() throws ServletException {
        // Initialize the PagesDAO for use in this servlet
        pagesDAO = new PagesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user has permission to access this page
        if (!hasPermission(request, response)) {
            return; // Exit the method if no permission
        }

        // Retrieve the pageId parameter from the request
        String pageIdParam = request.getParameter("pageId");
        
        if (pageIdParam != null) {
            try {
                // Parse the page ID from the parameter
                int pageId = Integer.parseInt(pageIdParam);
                
                // Fetch page details using the DAO
                Pages pageDetail = pagesDAO.getPageByID(pageId);
                
                // Check if page detail is found
                if (pageDetail != null) {
                    // Set page detail as a request attribute to be used in the JSP
                    request.setAttribute("pageDetail", pageDetail);
                    // Forward to the JSP that displays the page detail
                    request.getRequestDispatcher("PageDetail.jsp").forward(request, response);
                } else {
                    // If page not found, set error message and forward to error page
                    request.setAttribute("message", "Page not found.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                // Handle invalid page ID format
                request.setAttribute("message", "Invalid page ID.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            // Handle missing page ID parameter
            request.setAttribute("message", "Page ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the pageId parameter from the request for updating
        String pageIdParam = request.getParameter("pageId");
        
        if (pageIdParam != null) {
            try {
                // Parse the page ID from the parameter
                int pageId = Integer.parseInt(pageIdParam);
                String pageName = request.getParameter("pageName"); // Get new page name
                String pageUrl = request.getParameter("pageUrl");   // Get new page URL
                String status = request.getParameter("status");      // Get new page status
                
                // Update the page details using the DAO
                boolean isUpdated = pagesDAO.updatePage(pageId, pageName, pageUrl, status);
                
                // Set appropriate message based on update result
                if (isUpdated) {
                    request.setAttribute("message", "Page updated successfully!");
                } else {
                    request.setAttribute("message", "Failed to update page.");
                }
                
                // Fetch the updated page details to display on the JSP
                Pages pageDetail = pagesDAO.getPageByID(pageId);
                request.setAttribute("pageDetail", pageDetail);
                // Forward to the JSP that displays the updated page detail
                request.getRequestDispatcher("PageDetail.jsp").forward(request, response);
                
            } catch (NumberFormatException e) {
                // Handle invalid page ID format
                request.setAttribute("message", "Invalid page ID.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            // Handle missing page ID parameter for updating
            request.setAttribute("message", "Page ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
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
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

        // Redirect to homepage if user does not have permission
        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage");

            return false;
        } else if (pageID == null) {
            // Redirect to error page if page ID is not found
            response.sendRedirect("error.jsp");
            return false;
        }

        return true; // User has access to the page
    }
}
