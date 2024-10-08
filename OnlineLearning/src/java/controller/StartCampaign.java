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

/**
 *
 * @author Admin
 */
public class StartCampaign extends HttpServlet {
   
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    // Lấy id của campaign cần xử lý từ request parameter
    int campaignId = Integer.parseInt(request.getParameter("campaignId"));

    // Khởi tạo đối tượng CampaignsDAO
    CampaignsDAO cDAO = new CampaignsDAO();

    // Cập nhật trạng thái cho campaign
    boolean updateSuccess = cDAO.startCampaign(campaignId);

    if (updateSuccess) {
        // Nếu cập nhật thành công, cập nhật lại danh sách campaigns
        Map<Integer, Campaigns> campaigns = cDAO.getAllCampaigns();
        request.setAttribute("campaigns", campaigns);
        request.setAttribute("success", "Campaign stopped successfully!"); // Success message
    } else {
        // Nếu cập nhật không thành công, xử lý thông báo lỗi
        request.setAttribute("error", "Failed to stop campaign."); // Error message
    }

    // Chuyển hướng về trang danh sách campaigns
    request.getRequestDispatcher("CampaignList.jsp").forward(request, response);
}

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
