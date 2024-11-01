package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import dal.CampaignsDAO;
import dal.CampaignMediaDAO;
import dal.RolePermissionDAO;
import dal.PagesDAO;
import model.Campaigns;
import model.CampaignMedia;
import model.Users;

@MultipartConfig
public class AddCampaign extends HttpServlet {
    
    private final CampaignMediaDAO campaignMediaDAO = new CampaignMediaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!hasPermission(request, response)) return;
        request.getRequestDispatcher("AddCampaign.jsp").forward(request, response);
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (!hasPermission(request, response)) return;

    // Retrieve form parameters
    String campaignName = request.getParameter("campaignName");
    String description = request.getParameter("description");
    Date startDate = Date.valueOf(request.getParameter("startDate"));
    Date endDate = Date.valueOf(request.getParameter("endDate"));

    // Determine the status based on the start date relative to the current date
    Date currentDate = new Date(System.currentTimeMillis());
    String status = (startDate.after(currentDate)) ? "Not Started" : "Active";

    // Create a new Campaign object with the updated status
    Campaigns campaign = new Campaigns();
    campaign.setCampaignName(campaignName);
    campaign.setDescription(description);
    campaign.setStartDate(startDate);
    campaign.setEndDate(endDate);
    campaign.setStatus(status);

    CampaignsDAO campaignDAO = new CampaignsDAO();
    try {
        // Add the campaign and retrieve the new campaign ID
        int campaignID = campaignDAO.addCampaign(campaign);

        // Process media files and descriptions
        List<Part> mediaFiles = request.getParts().stream()
                .filter(part -> part.getName().equals("mediaFiles") && part.getSize() > 0)
                .collect(Collectors.toList());
        List<String> mediaDescriptions = List.of(request.getParameterValues("mediaDescriptions"));

        // Save each media file and link to the campaign
        for (int i = 0; i < mediaFiles.size(); i++) {
            String mediaDescription = (i < mediaDescriptions.size()) ? mediaDescriptions.get(i) : "";
            String uploadDir = "campaignMedia/";
            String mediaLink = saveMediaFile(mediaFiles.get(i), uploadDir);

            if (mediaLink != null) {
                CampaignMedia media = new CampaignMedia();
                media.setCampaignID(campaignID);
                media.setMediaLink(mediaLink);
                media.setDescription(mediaDescription);
                campaignMediaDAO.addCampaignMedia(media);
            }
        }

        request.getSession().setAttribute("success", "Campaign added successfully!");
        response.sendRedirect("campaignList");

    } catch (SQLException ex) {
        Logger.getLogger(AddCampaign.class.getName()).log(Level.SEVERE, null, ex);
        request.getSession().setAttribute("error", "Failed to add campaign: " + ex.getMessage());
        response.sendRedirect("AddCampaign.jsp");
    }
}


private String saveMediaFile(Part filePart, String uploadDir) throws IOException {
    if (filePart != null && filePart.getSize() > 0) {
        // Get the file name from the uploaded part
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        // Define the upload directory relative to the web application context
        String relativeUploadDir = "campaignMedia/";
        // Use a consistent, non-build directory
        String uploadPath = getServletContext().getRealPath("/") + File.separator + relativeUploadDir;
        // Create the directory if it does not exist
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs(); // Create any necessary parent directories
        }
        // Create a new file object for the uploaded file
        File file = new File(uploadDirectory, fileName);
        // Write the file to the upload path
        filePart.write(file.getAbsolutePath());
        // Return the relative path for accessing the file
        return relativeUploadDir + fileName;
    }
    return null; // Return null if no file was uploaded
}



    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());
        String userRole = currentUser.getRole();

        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
           response.sendRedirect(request.getContextPath() + "/Homepage");

            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true;
    }
}
