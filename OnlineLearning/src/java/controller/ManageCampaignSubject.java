/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.Campaign_SubjectDAO;
import dal.CampaignsDAO;
import dal.PackagePriceDAO;
import dal.SubjectDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import model.Campaigns;
import model.Subject;

/**
 *
 * @author Admin
 */
/**
 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
 * methods.
 *
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
@WebServlet(name = "ManageCampaignSubject", urlPatterns = {"/ManageCampaignSubject"})
public class ManageCampaignSubject extends HttpServlet {

    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final CampaignsDAO campaignsDAO = new CampaignsDAO();
    private final Campaign_SubjectDAO csDao = new Campaign_SubjectDAO();
    private final PackagePriceDAO pDao = new PackagePriceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch the active subjects and ongoing campaigns
        List<Subject> activeSubjects = subjectDAO.getActiveSubjects();
        List<Campaigns> ongoingCampaigns = campaignsDAO.getOngoingCampaigns();
        // Fetch the current discounts for each campaign-subject pair
        // This can be implemented with a method in your Campaign_SubjectDAO class
        Map<String, Double> currentDiscounts = csDao.getCurrentDiscounts();

        // Set the data as request attributes
        request.setAttribute("activeSubjects", activeSubjects);
        request.setAttribute("ongoingCampaigns", ongoingCampaigns);
        request.setAttribute("currentDiscounts", currentDiscounts);

        // Forward to CampaignSubject.jsp
        request.getRequestDispatcher("CampaignSubject.jsp").forward(request, response);
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

    // Iterate over all the request parameters that are named 'subjectSelection_{campaignId}_{subjectId}'
    for (String param : request.getParameterMap().keySet()) {
        if (param.startsWith("subjectSelection_")) {
            // Extract campaignId and subjectId from the parameter name
            String[] parts = param.split("_");
            String campaignId = parts[1];  // campaignId
            String subjectId = parts[2];   // subjectId

            // Get the discount value for the current subject and campaign
            String discountStr = request.getParameter("discount_" + subjectId + "_" + campaignId);

            try {
                // Validate the discount value
                int discount = Integer.parseInt(discountStr);
                if (discount < 0 || discount > 100) {
                    // If discount is invalid, return error message and redirect back to the JSP
                    request.setAttribute("errorMessage", "Discount must be between 0 and 100.");
                    request.getRequestDispatcher("CampaignSubject.jsp").forward(request, response);
                    return;
                }
                boolean delete = csDao.deleteCampaignSubject();

                // Call the DAO method to add the campaign-subject discount
                boolean success = csDao.addCampaignSubject(campaignId, subjectId, discount);

                if (!success) {
                    // If insertion failed, return error message
                    request.setAttribute("errorMessage", "Failed to save campaign discount for course " + subjectId);
                    request.getRequestDispatcher("CampaignSubject.jsp").forward(request, response);
                    return;
                }

                // Get the list of prices for the given subjectId
                List<Double> prices = pDao.getPricesBySubjectID(Integer.parseInt(subjectId));

                // Check if the list is not empty and process the first price
                if (prices != null && !prices.isEmpty()) {
                    // Calculate the sale price based on the discount using the first price
                    double originalPrice = prices.get(0);
                    double salePrice = originalPrice - (originalPrice * discount / 100.0);

                    // Update the sale price in the database using the PackagePriceDAO
                    boolean priceUpdateSuccess = pDao.updateSalePriceBySubjectID(Integer.parseInt(subjectId), salePrice);
                    if (!priceUpdateSuccess) {
                        request.setAttribute("errorMessage", "Failed to update sale price for subject " + subjectId);
                        request.getRequestDispatcher("CampaignSubject.jsp").forward(request, response);
                        return;
                    }
                } else {
                    // If no prices are found for the given subjectId
                    request.setAttribute("errorMessage", "No prices found for the given subjectId.");
                    request.getRequestDispatcher("CampaignSubject.jsp").forward(request, response);
                    return;
                }

            } catch (NumberFormatException e) {
                // Handle invalid discount format
                request.setAttribute("errorMessage", "Invalid discount value. Please enter a valid number between 0 and 100.");
                request.getRequestDispatcher("CampaignSubject.jsp").forward(request, response);
                return;
            }
        }
    }

    // After successfully processing, redirect to a success page or back to the main form
    request.setAttribute("successMessage", "Discounts updated successfully.");
    response.sendRedirect("ManageCampaignSubject");  // Redirecting to the same page or a success page
}

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
