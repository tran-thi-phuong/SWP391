package controller;

import dal.CampaignsDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Campaigns;
import model.Users;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CampaignList extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CampaignList.class.getName());
    private final CampaignsDAO campaignDAO = new CampaignsDAO();
    private static final String DATE_FORMAT = "yyyy-MM-dd"; // Adjusting to match input type="date"

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user has permission to access this page
        if (!hasPermission(request, response)) {
            return; // Exit if the user lacks permission
        }

        String campaignName = request.getParameter("campaignName");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String status = request.getParameter("status");

        String currentPageParam = request.getParameter("page");
        int pageSize = 10; // Default page size if not set

        // Parse the current page number
        int currentPage = 1; // Default to the first page
        if (currentPageParam != null) {
            try {
                currentPage = Integer.parseInt(currentPageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        try {
            List<Campaigns> campaigns = campaignDAO.getCampaignsesByPage(currentPage, pageSize, campaignName,
                    parseDate(startDate), parseDate(endDate), status);
            int totalCampaign = campaignDAO.getTotalCampaign(campaignName, parseDate(startDate),
                    parseDate(endDate), status);
            int totalPages = (int) Math.ceil((double) totalCampaign / pageSize);
            if (totalPages < 1) {
                totalPages = 1; // At least one page must be available
            }

            // Set the retrieved campaigns map as a request attribute to be accessed in the JSP
            request.setAttribute("campaigns", campaigns);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("error", request.getParameter("error"));
            request.setAttribute("success", request.getParameter("success"));

            // Forward the request to CampaignList.jsp to display the campaigns
            request.getRequestDispatcher("CampaignList.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving campaigns", e);
            response.sendRedirect("error.jsp?message=Error retrieving campaigns");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response); // Handle GET requests
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response); // Handle POST requests
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

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null; // Return null for empty or null date strings
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setLenient(false); // Strict parsing to catch format errors
            return dateFormat.parse(dateStr); // Parse and return the date
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Date parsing failed for input: {0}", dateStr); // Better logging
            return null; // Return null on parse error
        }
    }
}
