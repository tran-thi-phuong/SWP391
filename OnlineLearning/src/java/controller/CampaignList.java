package controller;

import dal.CampaignsDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Campaigns;
import model.Users;

public class CampaignList extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CampaignList.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user has permission to access this page
        if (!hasPermission(request, response)) {
            return; // Exit if the user lacks permission
        }

        try {
            // Retrieve all campaigns as a map where the key is the campaign ID and the value is the Campaigns object
            CampaignsDAO campaignDAO = new CampaignsDAO();
            Map<Integer, Campaigns> campaigns = campaignDAO.getAllCampaigns();

            // Set the retrieved campaigns map as a request attribute to be accessed in the JSP
            request.setAttribute("campaigns", campaigns);

            // Forward the request to CampaignList.jsp to display the campaigns
            request.getRequestDispatcher("CampaignList.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving campaigns", e);
            response.sendRedirect("error.jsp?message=Error retrieving campaigns");
        }
    }

      private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession();
    Users currentUser = (Users) session.getAttribute("user");

    // Kiểm tra nếu người dùng chưa đăng nhập thì chuyển hướng đến trang đăng nhập
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return false;
    }

    // Lấy quyền của người dùng và kiểm tra quyền truy cập với trang hiện tại
    String userRole = currentUser.getRole();
    RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
    Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

    // Nếu người dùng đã đăng nhập nhưng không có quyền, chuyển hướng về /homePage
    if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
        response.sendRedirect("/Homepage");
        return false;
    } else if (pageID == null) {
        // Nếu không tìm thấy trang trong hệ thống phân quyền, chuyển đến trang lỗi
        response.sendRedirect("error.jsp");
        return false;
    }

    return true; // Người dùng có quyền truy cập trang này
}
}
