/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.RegistrationsDAO;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import model.SubjectCategoryCount;
/**
 *
 * @author tuant
 */
public class RegistrationStat extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<h1>Servlet RegistrationStat at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        LocalDate endDateLocal = LocalDate.now();
//        LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
//        Date endDate = Date.valueOf(endDateLocal);
//        Date startDate = Date.valueOf(startDateLocal);
//        RegistrationsDAO r = new RegistrationsDAO();
//        int successRegistration = r.getTotalRegistrationByStatus("Active");
//        int submittedRegistration = r.getTotalRegistrationByStatus("Processing");
//        int cancelledRegistration = r.getTotalRegistrationByStatus("Inactive");
//        int newRegistration = r.getNewRegistrationByTime(startDate, endDate);
//        int totalRegistration = r.getTotalRegistrations();
//        List<SubjectCategoryCount> registrationAllocation = r.getRegistrationAllocation();
//        List<SubjectCategoryCount> bestSeller = r.getBestSeller(5);
//        request.setAttribute("successRegistration", successRegistration);
//        request.setAttribute("submittedRegistration", submittedRegistration);
//        request.setAttribute("cancelledRegistration", cancelledRegistration);
//        request.setAttribute("bestSeller", bestSeller);
//        request.setAttribute("registrationAllocation", registrationAllocation);
//        request.setAttribute("newRegistration", newRegistration);
//        request.setAttribute("totalRegistration", totalRegistration);
//        request.setAttribute("action", "registrationStat");
//        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
