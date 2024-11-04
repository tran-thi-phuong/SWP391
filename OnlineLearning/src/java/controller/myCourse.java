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

public class myCourse extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        if (!hasPermission(request, response)) {
//            return;
//        }
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        int userId = user.getUserID();

        String searchQuery = request.getParameter("searchQuery");
        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");

        if (searchQuery == null) {
            searchQuery = (String) session.getAttribute("searchQuery");
        }
        if (pageSizeStr == null) {
            pageSizeStr = (String) session.getAttribute("pageSizeStr");
        }

        // Lưu các giá trị hiện tại vào session cho các request tiếp theo
        session.setAttribute("searchQuery", searchQuery);
        session.setAttribute("pageSizeStr", pageSizeStr);
        int PAGE_SIZE = 5;
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            try {
                PAGE_SIZE = Integer.parseInt(pageSizeStr);
            } catch (NumberFormatException e) {
                PAGE_SIZE = 5;
            }
        }

        // Xác định trang hiện tại
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        RegistrationsDAO course = new RegistrationsDAO();
        List<Registrations> courseList;
        int totalCourse;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            courseList = course.searchCourseByUserId(userId, searchQuery, page, PAGE_SIZE);
            totalCourse = course.getTotalSearchResultsCourseByUserId(userId, searchQuery);
        } else {
            courseList = course.getCourseByUserId(userId, page, PAGE_SIZE);
            totalCourse = course.getTotalCourseByUserId(userId);
        }
        int totalPages = (int) Math.ceil((double) totalCourse / PAGE_SIZE);
        request.setAttribute("courseList", courseList);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("totalPages", totalPages);

        // Chuyển tiếp request tới trang JSP
        request.getRequestDispatcher("myCourse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
