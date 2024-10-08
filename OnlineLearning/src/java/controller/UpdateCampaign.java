package controller;

import dal.CampaignsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Campaigns;

public class UpdateCampaign extends HttpServlet {

    private CampaignsDAO campaignDAO;

    @Override
    public void init() throws ServletException {
        campaignDAO = new CampaignsDAO(); // Initialize your DAO here
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve campaign ID from request
        String campaignIdStr = request.getParameter("id"); // Ensure this matches the link in your JSP

        // Validate campaign ID
        if (campaignIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing campaign ID");
            return;
        }

        int campaignId;
        try {
            campaignId = Integer.parseInt(campaignIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid campaign ID");
            return;
        }

        Campaigns campaign = campaignDAO.getCampaignByID(campaignId);

        if (campaign != null) {
            request.setAttribute("campaign", campaign);
            request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Campaign not found");
        }
    }

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Retrieve form data from the request
    String campaignIdStr = request.getParameter("campaignId");
    String campaignName = request.getParameter("campaignName");
    String description = request.getParameter("description");
    String startDateStr = request.getParameter("startDate");
    String endDateStr = request.getParameter("endDate");
    String status = request.getParameter("status");

    // Validate inputs
    if (campaignIdStr == null || campaignName == null || description == null || startDateStr == null || endDateStr == null || status == null) {
        request.setAttribute("error", "All fields are required.");
        request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
        return;
    }

    // Convert campaignId to integer
    int campaignId;
    try {
        campaignId = Integer.parseInt(campaignIdStr);
    } catch (NumberFormatException e) {
        request.setAttribute("error", "Invalid campaign ID.");
        request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
        return;
    }

    // Convert date strings to Date objects
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate;
    Date endDate;

    try {
        startDate = dateFormat.parse(startDateStr);
        endDate = dateFormat.parse(endDateStr);
    } catch (ParseException e) {
        request.setAttribute("error", "Invalid date format. Please use yyyy-MM-dd.");
        request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
        return;
    }

    // Create a Campaign object and set its attributes
    Campaigns campaign = new Campaigns();
    campaign.setCampaignId(campaignId);
    campaign.setCampaignName(campaignName);
    campaign.setDescription(description);
    campaign.setStartDate(startDate);
    campaign.setEndDate(endDate);
    campaign.setStatus(status);

    // Update campaign in the database
    boolean isUpdated = campaignDAO.updateCampaign(campaign);

    // Check if the update was successful
    if (isUpdated) {
        // Store success message in session
        request.getSession().setAttribute("success", "Campaign updated successfully!");
        // Redirect to campaign list page
        response.sendRedirect(request.getContextPath() + "/campaignList");
    } else {
        request.setAttribute("error", "Failed to update campaign.");
        request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
    }
}

    }
