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
        CampaignsDAO campaignDAO = new CampaignsDAO();
        Map<Integer, Campaigns> campaigns = campaignDAO.getAllCampaigns();
        request.setAttribute("campaigns", campaigns);
        request.getRequestDispatcher("CampaignList.jsp").forward(request, response);
    }
}
