/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.RegistrationsDAO;
import dal.RolePermissionDAO;
import model.Registrations;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Users;

public class myRegistration extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        int userId = user.getUserID();

        String searchQuery = request.getParameter("searchQuery");
        String statusFilter = request.getParameter("status");
        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");

        if (searchQuery == null) {
            searchQuery = (String) session.getAttribute("searchQuery");
        }
        if (statusFilter == null) {
            statusFilter = (String) session.getAttribute("statusFilter");
        }
        if (pageSizeStr == null) {
            pageSizeStr = (String) session.getAttribute("pageSizeStr");
        }

        session.setAttribute("searchQuery", searchQuery);
        session.setAttribute("statusFilter", statusFilter);
        session.setAttribute("pageSizeStr", pageSizeStr);

        int PAGE_SIZE = 5;
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            try {
                PAGE_SIZE = Integer.parseInt(pageSizeStr);
            } catch (NumberFormatException e) {
                PAGE_SIZE = 5;
            }
        }

        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        RegistrationsDAO registrationsDAO = new RegistrationsDAO();
        List<Registrations> registrationList;
        int totalRegistrations;

        // Lấy danh sách đăng ký theo bộ lọc trạng thái, tìm kiếm hoặc phân trang
        if (statusFilter != null && !statusFilter.isEmpty() && !"All".equals(statusFilter)) {
            registrationList = registrationsDAO.getRegistrationsByUserIdAndStatus(userId, statusFilter, page,
                    PAGE_SIZE);
            totalRegistrations = registrationsDAO.getTotalRegistrationsByUserIdAndStatus(userId, statusFilter);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            registrationList = registrationsDAO.searchRegistrationsByUserId(userId, searchQuery, page, PAGE_SIZE);
            totalRegistrations = registrationsDAO.getTotalSearchResultsByUserId(userId, searchQuery);
        } else {
            registrationList = registrationsDAO.getRegistrationsByUserId(userId, page, PAGE_SIZE);
            totalRegistrations = registrationsDAO.getTotalRegistrationsByUserId(userId);
        }

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRegistrations / PAGE_SIZE);

        // Đặt các attribute để hiển thị trong JSP
        request.setAttribute("registrationList", registrationList);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("pageSizeStr", pageSizeStr);
        request.setAttribute("totalPages", totalPages);

        // Chuyển tiếp request tới trang JSP
        request.getRequestDispatcher("myRegistration.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        int PAGE_SIZE = 5;
        int page = 1;

        int userId = user.getUserID();

        if ("cancel".equals(action)) {
            String registrationIdStr = request.getParameter("registrationId");
            int registrationId = Integer.parseInt(registrationIdStr);
            RegistrationsDAO registrationsDAO = new RegistrationsDAO();
            int totalRegistrations;
            totalRegistrations = registrationsDAO.getTotalRegistrationsByUserId(userId);
            // Hủy đăng ký
            boolean isCancelled = registrationsDAO.updateStatusToCancelled(registrationId);

            // Nếu hủy thành công, cập nhật danh sách đăng ký mới
            if (isCancelled) {
                List<Registrations> registrationList = registrationsDAO.getRegistrationsByUserId(userId, page,
                        PAGE_SIZE);
                request.setAttribute("registrationList", registrationList);
                request.setAttribute("message", "Registration cancelled successfully.");
            } else {
                request.setAttribute("message", "Failed to cancel registration.");
            }
            int totalPages = (int) Math.ceil((double) totalRegistrations / PAGE_SIZE);
            request.setAttribute("totalPages", totalPages);
        }

        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("currentPage", page);

        request.getRequestDispatcher("myRegistration.jsp").forward(request, response);
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
           response.sendRedirect(request.getContextPath() + "/Homepage");

            return false;
        } else if (pageID == null) {
            // Nếu không tìm thấy trang trong hệ thống phân quyền, chuyển đến trang lỗi
            response.sendRedirect("error.jsp");
            return false;
        }

        return true; // Người dùng có quyền truy cập trang này
    }
}
