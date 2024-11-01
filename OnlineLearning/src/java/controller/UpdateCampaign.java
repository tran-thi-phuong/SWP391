package controller;

import dal.CampaignMediaDAO;
import dal.CampaignsDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
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
import java.util.List;
import java.util.stream.Collectors;
import model.CampaignMedia;
import model.Campaigns;
import model.Users;
@MultipartConfig
public class UpdateCampaign extends HttpServlet {

    private CampaignsDAO campaignDAO;
    private CampaignMediaDAO campaignMediaDAO;

    @Override
    public void init() throws ServletException {
        campaignDAO = new CampaignsDAO();
        campaignMediaDAO = new CampaignMediaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }

        String campaignIdStr = request.getParameter("id");
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
        List<CampaignMedia> mediaList = campaignMediaDAO.getCampaignMediaByCampaignID(campaignId);

        if (campaign != null) {
            request.setAttribute("campaign", campaign);
            request.setAttribute("cm", mediaList);
            request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Campaign not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String campaignIdStr = request.getParameter("campaignId");
        String campaignName = request.getParameter("campaignName");
        String description = request.getParameter("description");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        if (campaignIdStr == null || campaignName == null || description == null || startDateStr == null || endDateStr == null) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
            return;
        }

        int campaignId;
        try {
            campaignId = Integer.parseInt(campaignIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid campaign ID.");
            request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
            return;
        }

        // Parsing and validating dates
        java.sql.Date startDate;
        java.sql.Date endDate;
        try {
            startDate = java.sql.Date.valueOf(startDateStr);
            endDate = java.sql.Date.valueOf(endDateStr);
            if (startDate.after(endDate)) {
                request.setAttribute("error", "Start date cannot be after end date.");
                request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
                return;
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format: " + e.getMessage());
            request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
            return;
        }

        // Determine campaign status
        String status = request.getParameter("status");

        Campaigns campaign = new Campaigns(campaignId, campaignName, description, startDate, endDate, status);
        boolean isUpdated = campaignDAO.updateCampaign(campaign);

        // Handling media updates
        List<Part> mediaFiles = request.getParts().stream()
                .filter(part -> "mediaFiles".equals(part.getName()) && part.getSize() > 0)
                .collect(Collectors.toList());
        String[] mediaDescriptions = request.getParameterValues("mediaDescriptions");

        campaignMediaDAO.removeCampaignMedia(campaignId);

        // Saving new or updated media files
        for (int i = 0; i < mediaFiles.size(); i++) {
            Part mediaFile = mediaFiles.get(i);
            String mediaDescription = i < mediaDescriptions.length ? mediaDescriptions[i] : "";
            String uploadPath = saveMediaFile(mediaFile, "campaignMedia/");

            if (uploadPath != null) {
                CampaignMedia newMedia = new CampaignMedia();
                newMedia.setCampaignID(campaignId);
                newMedia.setMediaLink(uploadPath);
                newMedia.setDescription(mediaDescription);
                campaignMediaDAO.addCampaignMedia(newMedia);
            }
        }

        if (isUpdated) {
            request.getSession().setAttribute("success", "Campaign updated successfully!");
            response.sendRedirect(request.getContextPath() + "/campaignList");
        } else {
            request.setAttribute("error", "Failed to update campaign.");
            request.getRequestDispatcher("UpdateCampaign.jsp").forward(request, response);
        }
    }

    private String saveMediaFile(Part filePart, String uploadDir) throws IOException {
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String relativeUploadDir = "campaignMedia/";
            String uploadPath = getServletContext().getRealPath("/") + File.separator + relativeUploadDir;
            File uploadDirectory = new File(uploadPath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }
            File file = new File(uploadDirectory, fileName);
            filePart.write(file.getAbsolutePath());
            return relativeUploadDir + fileName;
        }
        return null;
    }
    

    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        String userRole = currentUser.getRole();
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect("/Homepage");
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true;
    }
}
