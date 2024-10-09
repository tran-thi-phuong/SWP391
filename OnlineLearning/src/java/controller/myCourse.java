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
        String searchQuery = request.getParameter("searchQuery");

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int userId = user.getUserID();

        RegistrationsDAO course = new RegistrationsDAO();
        List<Registrations> courseList;

        if (searchQuery != null) {
            courseList = course.searchCourseByUserId(userId, searchQuery);
        } else {
            courseList = course.getCourseByUserId(userId);
        }

        request.setAttribute("courseList", courseList);
        request.setAttribute("searchQuery", searchQuery);

        // Chuyển tiếp request tới trang JSP
        request.getRequestDispatcher("myCourse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
