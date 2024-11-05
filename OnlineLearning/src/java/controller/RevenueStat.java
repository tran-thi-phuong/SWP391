/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.PaymentDAO;
import dal.RolePermissionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import model.Revenue;
import model.Users;

/**
 *
 * @author tuant
 */
public class RevenueStat extends HttpServlet {

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
            out.println("<title>Servlet RevenueStat</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RevenueStat at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        // set time range 30 days for the first time go to registration statistic
        LocalDate endDateLocal = LocalDate.now();
        LocalDate startDateLocal = endDateLocal.minus(30, ChronoUnit.DAYS);
        Date endDate = Date.valueOf(endDateLocal);
        Date startDate = Date.valueOf(startDateLocal);
        PaymentDAO p = new PaymentDAO();
        double totalRevenue = p.getTotalRevenue();
        double lastMonthRevenue = p.getRevenueByTime(startDate, endDate);
        List<Revenue> revenueAllocation = p.getRevenueAllocation();
        request.setAttribute("lastMonthRevenue", lastMonthRevenue);
        request.setAttribute("revenueAllocation", revenueAllocation);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("action", "revenueStat");
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
        PaymentDAO p = new PaymentDAO();
        double totalRevenue = p.getTotalRevenue();
        double lastMonthRevenue = 0;
        List<Revenue> revenueAllocation = p.getRevenueAllocation();
        if (select.equals("7days")) {
            LocalDate endDateLocal = LocalDate.now();
            LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
            Date endDate = Date.valueOf(endDateLocal);
            Date startDate = Date.valueOf(startDateLocal);
            lastMonthRevenue = p.getRevenueByTime(startDate, endDate);
        } else if (select.equals("30days")) {
            LocalDate endDateLocal = LocalDate.now();
            LocalDate startDateLocal = endDateLocal.minus(30, ChronoUnit.DAYS);
            Date endDate = Date.valueOf(endDateLocal);
            Date startDate = Date.valueOf(startDateLocal);
            lastMonthRevenue = p.getRevenueByTime(startDate, endDate);
        } else if (select.equals("custom")) {
            if (timeRange == null || timeRange.trim().isEmpty()) {
                request.setAttribute("error", "Please select a valid date range.");
                request.setAttribute("lastMonthRevenue", lastMonthRevenue);
                request.setAttribute("revenueAllocation", revenueAllocation);
                request.setAttribute("totalRevenue", totalRevenue);
                request.setAttribute("action", "revenueStat");
                request.setAttribute("select", select);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
            String[] timeRangeSplit = timeRange.split(" to ");
            if (timeRangeSplit.length != 2) {
                request.setAttribute("error", "Please select a valid date range.");
                request.setAttribute("lastMonthRevenue", lastMonthRevenue);
                request.setAttribute("revenueAllocation", revenueAllocation);
                request.setAttribute("totalRevenue", totalRevenue);
                request.setAttribute("action", "revenueStat");
                request.setAttribute("select", select);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
            String startDateStr = timeRangeSplit[0];
            String endDateStr = timeRangeSplit[1];

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = new Date(dateFormat.parse(startDateStr).getTime());
                Date endDate = new Date(dateFormat.parse(endDateStr).getTime());
                lastMonthRevenue = p.getRevenueByTime(startDate, endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
        request.setAttribute("lastMonthRevenue", lastMonthRevenue);
        request.setAttribute("revenueAllocation", revenueAllocation);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("action", "revenueStat");
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
