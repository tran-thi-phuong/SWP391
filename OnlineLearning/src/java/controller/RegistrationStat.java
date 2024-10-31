/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.RegistrationsDAO;
import dal.RolePermissionDAO;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
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
public class RegistrationStat extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegistrationStat</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegistrationStat at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }
        LocalDate endDateLocal = LocalDate.now();
        LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
        Date endDate = Date.valueOf(endDateLocal);
        Date startDate = Date.valueOf(startDateLocal);
        RegistrationsDAO r = new RegistrationsDAO();
        int successRegistration = r.getTotalRegistrationByStatus("Active", startDate, endDate);
        int submittedRegistration = r.getTotalRegistrationByStatus("Processing", startDate, endDate);
        int cancelledRegistration = r.getTotalRegistrationByStatus("Inactive", startDate, endDate);
        int newRegistration = r.getNewRegistrationByTime(startDate, endDate);
        int totalRegistration = r.getTotalRegistrations();
        List<SubjectCategoryCount> registrationAllocation = r.getRegistrationAllocation();
        List<SubjectCategoryCount> bestSeller = r.getBestSeller(5);
        request.setAttribute("successRegistration", successRegistration);
        request.setAttribute("submittedRegistration", submittedRegistration);
        request.setAttribute("cancelledRegistration", cancelledRegistration);
        request.setAttribute("bestSeller", bestSeller);
        request.setAttribute("registrationAllocation", registrationAllocation);
        request.setAttribute("newRegistration", newRegistration);
        request.setAttribute("totalRegistration", totalRegistration);
        request.setAttribute("action", "registrationStat");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String select = request.getParameter("select-action");
        String timeRange = request.getParameter("timeRange");
        RegistrationsDAO r = new RegistrationsDAO();
        int successRegistration = 0;
        int submittedRegistration = 0;
        int cancelledRegistration = 0;
        int totalRegistration = r.getTotalRegistrations();
        int newRegistration = 0;
        List<SubjectCategoryCount> registrationAllocation = r.getRegistrationAllocation();
        List<SubjectCategoryCount> bestSeller = r.getBestSeller(5);
        if (select.equals("7days")) {
            LocalDate endDateLocal = LocalDate.now();
            LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
            Date endDate = Date.valueOf(endDateLocal);
            Date startDate = Date.valueOf(startDateLocal);
            successRegistration = r.getTotalRegistrationByStatus("Active", startDate, endDate);
            submittedRegistration = r.getTotalRegistrationByStatus("Processing", startDate, endDate);
            cancelledRegistration = r.getTotalRegistrationByStatus("Inactive", startDate, endDate);
            newRegistration = r.getNewRegistrationByTime(startDate, endDate);
        } else if (select.equals("30days")) {
            LocalDate endDateLocal = LocalDate.now();
            LocalDate startDateLocal = endDateLocal.minus(30, ChronoUnit.DAYS);
            Date endDate = Date.valueOf(endDateLocal);
            Date startDate = Date.valueOf(startDateLocal);
            successRegistration = r.getTotalRegistrationByStatus("Active", startDate, endDate);
            submittedRegistration = r.getTotalRegistrationByStatus("Processing", startDate, endDate);
            cancelledRegistration = r.getTotalRegistrationByStatus("Inactive", startDate, endDate);
            newRegistration = r.getNewRegistrationByTime(startDate, endDate);
        } else if (select.equals("custom")) {
            if (timeRange == null || timeRange.trim().isEmpty()) {
                request.setAttribute("error", "Please select a valid date range.");
                request.setAttribute("successRegistration", successRegistration);
                request.setAttribute("submittedRegistration", submittedRegistration);
                request.setAttribute("cancelledRegistration", cancelledRegistration);
                request.setAttribute("bestSeller", bestSeller);
                request.setAttribute("registrationAllocation", registrationAllocation);
                request.setAttribute("newRegistration", newRegistration);
                request.setAttribute("totalRegistration", totalRegistration);
                request.setAttribute("action", "registrationStat");
                request.setAttribute("select", select);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
            String[] timeRangeSplit = timeRange.split(" to ");
            if (timeRangeSplit.length != 2){
                request.setAttribute("error", "Please select a valid date range.");
                request.setAttribute("successRegistration", successRegistration);
                request.setAttribute("submittedRegistration", submittedRegistration);
                request.setAttribute("cancelledRegistration", cancelledRegistration);
                request.setAttribute("bestSeller", bestSeller);
                request.setAttribute("registrationAllocation", registrationAllocation);
                request.setAttribute("newRegistration", newRegistration);
                request.setAttribute("totalRegistration", totalRegistration);
                request.setAttribute("action", "registrationStat");
                request.setAttribute("select", select);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
            String startDateStr = timeRangeSplit[0];
            String endDateStr = timeRangeSplit[1];

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = new Date(dateFormat.parse(startDateStr).getTime());
                Date endDate = new Date(dateFormat.parse(endDateStr).getTime());
                newRegistration = r.getNewRegistrationByTime(startDate, endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
        request.setAttribute("successRegistration", successRegistration);
        request.setAttribute("submittedRegistration", submittedRegistration);
        request.setAttribute("cancelledRegistration", cancelledRegistration);
        request.setAttribute("bestSeller", bestSeller);
        request.setAttribute("registrationAllocation", registrationAllocation);
        request.setAttribute("newRegistration", newRegistration);
        request.setAttribute("totalRegistration", totalRegistration);
        request.setAttribute("action", "registrationStat");
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

}
