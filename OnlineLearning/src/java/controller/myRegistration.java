/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.RegistrationsDAO;
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
        String searchQuery = request.getParameter("searchQuery");
        String statusFilter = request.getParameter("status");
        String pageParam = request.getParameter("page");
        int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        int pageSize = 6;

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int userId = user.getUserID();

        RegistrationsDAO registrationsDAO = new RegistrationsDAO();
        List<Registrations> registrationList;

        if (statusFilter != null) {
            // Lấy danh sách đăng ký dựa trên trạng thái
            registrationList = registrationsDAO.getRegistrationsByUserIdAndStatus(userId, statusFilter);
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
            // Tìm kiếm đăng ký dựa trên từ khóa
            registrationList = registrationsDAO.searchRegistrationsByUserId(userId, searchQuery);
        } else {
            // Nếu không có từ khóa, lấy tất cả các đăng ký
            registrationList = registrationsDAO.getRegistrationsByUserId(userId);
        }

        // Đặt danh sách đăng ký vào request attribute để hiển thị trong JSP
        request.setAttribute("registrationList", registrationList);
        request.setAttribute("searchQuery", searchQuery);

        // Chuyển tiếp request tới trang JSP
        request.getRequestDispatcher("myRegistration.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        int userId = user.getUserID(); 

        if ("cancel".equals(action)) {
            String registrationIdStr = request.getParameter("registrationId");
            System.out.println("Registration ID: " + registrationIdStr); // Ghi ra giá trị của registrationId
            int registrationId = Integer.parseInt(registrationIdStr);
            RegistrationsDAO registrationsDAO = new RegistrationsDAO();
            List<Registrations> registrationList;

            // Hủy đăng ký
            boolean isCancelled = registrationsDAO.updateStatusToCancelled(registrationId);

            // Nếu hủy thành công, có thể lấy danh sách đăng ký mới
            if (isCancelled) {
                registrationList = registrationsDAO.getRegistrationsByUserId(userId);
                request.setAttribute("registrationList", registrationList);
                request.setAttribute("message", "Registration cancelled successfully.");
            } else {
                request.setAttribute("message", "Failed to cancel registration.");
            }
        }

        request.getRequestDispatcher("myRegistration.jsp").forward(request, response);
    }
}
