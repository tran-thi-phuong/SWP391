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

public class myCourse extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
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
}
