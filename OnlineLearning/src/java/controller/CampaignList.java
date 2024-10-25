/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.CampaignsDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import model.Campaigns;

public class CampaignList extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Create an instance of CampaignsDAO to interact with the database
        CampaignsDAO campaignDAO = new CampaignsDAO();
        
        // Retrieve all campaigns as a map where the key is the campaign ID and the value is the Campaigns object
        Map<Integer, Campaigns> campaigns = campaignDAO.getAllCampaigns();
        
        // Set the retrieved campaigns map as a request attribute to be accessed in the JSP
        request.setAttribute("campaigns", campaigns);
        
        // Forward the request to CampaignList.jsp to display the campaigns
        request.getRequestDispatcher("CampaignList.jsp").forward(request, response);
    }
}
