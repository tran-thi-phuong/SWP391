package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import model.Campaign; 
import dal.CampaignsDAO; 
import java.util.logging.Level;
import java.util.logging.Logger;

// Annotate the servlet to handle multipart form data
@MultipartConfig
public class AddCampaign extends HttpServlet {
    // Directory where uploaded images will be saved
    private static final String UPLOAD_DIR = "images";

    // Handle GET request to show the add campaign form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward the request to AddCampaign.jsp to display the form
        request.getRequestDispatcher("AddCampaign.jsp").forward(request, response);
    }

    // Handle POST request to process form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String campaignName = request.getParameter("campaignName");
        String description = request.getParameter("description");
        Date startDate = Date.valueOf(request.getParameter("startDate"));
        Date endDate = Date.valueOf(request.getParameter("endDate"));
        String status = request.getParameter("status");
        
        // Handle image upload
        String image = null;
        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // Create the directory if it does not exist
            }
            filePart.write(uploadPath + File.separator + fileName); // Save the file
            image = UPLOAD_DIR + File.separator + fileName; // Store relative path to save in DB
        }

        // Create a new campaign object and set its properties
        Campaign campaign = new Campaign();
        campaign.setCampaignName(campaignName);
        campaign.setDescription(description);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setImage(image);
        campaign.setStatus(status);

        // Save campaign to database using DAO
        CampaignsDAO campaignDAO = new CampaignsDAO();
        try {
            campaignDAO.addCampaign(campaign);
            // Store success message in session
            request.getSession().setAttribute("success", "Campaign added successfully!");
            // Redirect to campaign list page
            response.sendRedirect("campaignList");
        } catch (SQLException ex) {
            Logger.getLogger(AddCampaign.class.getName()).log(Level.SEVERE, null, ex);
            // Store error message in session
            request.getSession().setAttribute("error", "Failed to add campaign: " + ex.getMessage());
            // Redirect back to the add campaign page
            response.sendRedirect("AddCampaign.jsp");
        }
    }
}
