/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.UserDAO;
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
import model.Users;

/**
 *
 * @author tuant
 */
public class CustomerStat extends HttpServlet {

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
            out.println("<title>Servlet CustomerStat</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerStat at " + request.getContextPath() + "</h1>");
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
            return; // Exit if the user lacks permission
        }
        
        UserDAO u = new UserDAO();
        LocalDate endDateLocal = LocalDate.now();
        LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
        java.sql.Date endDate = java.sql.Date.valueOf(endDateLocal);
        java.sql.Date startDate = java.sql.Date.valueOf(startDateLocal);
        int newCustomer = u.getNewCustomer(startDate, endDate);
        int totalCustomer = u.getTotalCustomer();
        int newlyBoughtCustomer = u.getNewlyBoughtCustomer(startDate, endDate);
        int totalBoughtCustomer = u.getTotalBoughtCustomer();
        request.setAttribute("totalBoughtCustomer", totalBoughtCustomer);
        request.setAttribute("newlyBoughtCustomer", newlyBoughtCustomer);
        request.setAttribute("newCustomer", newCustomer);
        request.setAttribute("totalCustomer", totalCustomer);
        request.setAttribute("action", "customerStat");
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
        UserDAO u = new UserDAO();
        int newCustomer = 0;
        int totalCustomer = u.getTotalCustomer();
        int newlyBoughtCustomer = 0;
        int totalBoughtCustomer = u.getTotalBoughtCustomer();
        if (select.equals("7days")) {
            LocalDate endDateLocal = LocalDate.now();
            LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
            Date endDate = Date.valueOf(endDateLocal);
            Date startDate = Date.valueOf(startDateLocal);
            newlyBoughtCustomer = u.getNewlyBoughtCustomer(startDate, endDate);
            newCustomer = u.getNewCustomer(startDate, endDate);
        } else if (select.equals("30days")) {
            LocalDate endDateLocal = LocalDate.now();
            LocalDate startDateLocal = endDateLocal.minus(30, ChronoUnit.DAYS);
            Date endDate = Date.valueOf(endDateLocal);
            Date startDate = Date.valueOf(startDateLocal);
            newlyBoughtCustomer = u.getNewlyBoughtCustomer(startDate, endDate);
            newCustomer = u.getNewCustomer(startDate, endDate);

        } else if (select.equals("custom")) {
            if (timeRange == null || timeRange.trim().isEmpty()) {
                request.setAttribute("error", "Please select a valid date range.");
                request.setAttribute("totalBoughtCustomer", totalBoughtCustomer);
                request.setAttribute("newlyBoughtCustomer", newlyBoughtCustomer);
                request.setAttribute("newCustomer", newCustomer);
                request.setAttribute("totalCustomer", totalCustomer);
                request.setAttribute("action", "customerStat");
                request.setAttribute("select", select);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
            String[] timeRangeSplit = timeRange.split(" to ");
            if (timeRangeSplit.length != 2){
                request.setAttribute("error", "Please select a valid date range.");
                request.setAttribute("totalBoughtCustomer", totalBoughtCustomer);
                request.setAttribute("newlyBoughtCustomer", newlyBoughtCustomer);
                request.setAttribute("newCustomer", newCustomer);
                request.setAttribute("totalCustomer", totalCustomer);
                request.setAttribute("action", "customerStat");
                request.setAttribute("select", select);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
            String startDateStr = timeRangeSplit[0];
            String endDateStr = timeRangeSplit[1];

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = new Date(dateFormat.parse(startDateStr).getTime());
                Date endDate = new Date(dateFormat.parse(endDateStr).getTime());
                newlyBoughtCustomer = u.getNewlyBoughtCustomer(startDate, endDate);
                newCustomer = u.getNewCustomer(startDate, endDate);

            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
        request.setAttribute("totalBoughtCustomer", totalBoughtCustomer);
        request.setAttribute("newlyBoughtCustomer", newlyBoughtCustomer);
        request.setAttribute("newCustomer", newCustomer);
        request.setAttribute("totalCustomer", totalCustomer);
        request.setAttribute("action", "customerStat");
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
