/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.RolePermissionDAO;
import java.sql.Date;
import dal.SubjectDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.List;
import model.SubjectCategoryCount;
import model.Users;

/**
 *
 * @author tuant
 */
public class CourseStat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         if (!hasPermission(request, response)) {
            return;
        }
         // set time range 7 days for the first time go to registration statistic
        LocalDate endDateLocal = LocalDate.now();
        LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
        Date endDate = Date.valueOf(endDateLocal);
        Date startDate = Date.valueOf(startDateLocal);
        SubjectDAO sDAO = new SubjectDAO();
        int totalCourse = sDAO.getTotalSubjects();
        int totalActiveCourse = sDAO.getTotalActiveSubjects();
        int newCourse = sDAO.getNewSubjectByTime(startDate, endDate);
        List<SubjectCategoryCount> subjectAllocation = sDAO.getSubjectAllocation();
        request.setAttribute("inactiveCourse", totalCourse - totalActiveCourse);
        request.setAttribute("newCourse", newCourse);
        request.setAttribute("totalCourse", totalCourse);
        request.setAttribute("subjectAllocation", subjectAllocation);
        request.setAttribute("action", "courseStat");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response); //forward request to dashboard.jsp
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // method for searching 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String select = request.getParameter("select-action");
        String timeRange = request.getParameter("timeRange");
        SubjectDAO sDAO = new SubjectDAO();
        int totalCourse = sDAO.getTotalSubjects();
        int totalActiveCourse = sDAO.getTotalActiveSubjects();
        List<SubjectCategoryCount> subjectAllocation = sDAO.getSubjectAllocation();
        int newCourse = 0;
        if (select.equals("7days")) {
            LocalDate endDateLocal = LocalDate.now();
            LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
            Date endDate = Date.valueOf(endDateLocal);
            Date startDate = Date.valueOf(startDateLocal);
            newCourse = sDAO.getNewSubjectByTime(startDate, endDate);
        } else if (select.equals("30days")) {
            LocalDate endDateLocal = LocalDate.now();
            LocalDate startDateLocal = endDateLocal.minus(30, ChronoUnit.DAYS);
            Date endDate = Date.valueOf(endDateLocal);
            Date startDate = Date.valueOf(startDateLocal);
            newCourse = sDAO.getNewSubjectByTime(startDate, endDate);
        } else if (select.equals("custom")) {
            if (timeRange == null || timeRange.trim().isEmpty()) {
                request.setAttribute("error", "Please select a valid date range.");
                request.setAttribute("inactiveCourse", totalCourse - totalActiveCourse);
                request.setAttribute("newCourse", newCourse);
                request.setAttribute("totalCourse", totalCourse);
                request.setAttribute("subjectAllocation", subjectAllocation);
                request.setAttribute("action", "courseStat");
                request.setAttribute("select", select);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                return;
            }
            String[] timeRangeSplit = timeRange.split(" to ");
            if (timeRangeSplit.length != 2) {
                request.setAttribute("error", "Please select a valid date range.");
                request.setAttribute("inactiveCourse", totalCourse - totalActiveCourse);
                request.setAttribute("newCourse", newCourse);
                request.setAttribute("totalCourse", totalCourse);
                request.setAttribute("subjectAllocation", subjectAllocation);
                request.setAttribute("action", "courseStat");
                request.setAttribute("select", select);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                return;
            }
            String startDateStr = timeRangeSplit[0];
            String endDateStr = timeRangeSplit[1];

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = new Date(dateFormat.parse(startDateStr).getTime());
                Date endDate = new Date(dateFormat.parse(endDateStr).getTime());
                newCourse = sDAO.getNewSubjectByTime(startDate, endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }

        request.setAttribute("inactiveCourse", totalCourse - totalActiveCourse);
        request.setAttribute("newCourse", newCourse);
        request.setAttribute("totalCourse", totalCourse);
        request.setAttribute("subjectAllocation", subjectAllocation);
        request.setAttribute("action", "courseStat");
        request.setAttribute("select", select);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);

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
