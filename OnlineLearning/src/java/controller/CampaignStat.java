/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.CampaignDAO;
import dal.PaymentDAO;
import dal.RegistrationsDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import model.Campaign;
import model.Revenue;

/**
 *
 * @author tuant
 */
public class CampaignStat extends HttpServlet {
   
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
            out.println("<title>Servlet CampaignStat</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CampaignStat at " + request.getContextPath () + "</h1>");
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
        String campaignID = request.getParameter("campaignID");
        CampaignDAO cDAO = new CampaignDAO();
        PaymentDAO p = new PaymentDAO();
        UserDAO u = new UserDAO();
        Campaign cam = cDAO.getCampaignByID(Integer.parseInt(campaignID));
        RegistrationsDAO r = new RegistrationsDAO();
        Date currentDate = new Date();
        
        if(cam.getStatus().equals("Incoming")){
            request.setAttribute("status", "incoming");
        }else{
            int registration, newCustomer, newlyBought;
            double revenue;
            List<Revenue> revenueAllocation;
            
            if(cam.getEndDate() != null && cam.getEndDate().before(currentDate)){
                revenueAllocation = p.getRevenueAllocationByTime(cam.getStartDate(), currentDate);
                newlyBought = u.getNewlyBoughtCustomer(cam.getStartDate(), currentDate);
                newCustomer = u.getNewCustomer(cam.getStartDate(), currentDate);
                registration = r.getNewRegistrationByTime(cam.getStartDate(), currentDate);
                revenue = p.getRevenueByTime(cam.getStartDate(), currentDate);
            }else{
                revenueAllocation = p.getRevenueAllocationByTime(cam.getStartDate(), cam.getEndDate());
                newlyBought = u.getNewlyBoughtCustomer(cam.getStartDate(), cam.getEndDate());
                newCustomer = u.getNewCustomer(cam.getStartDate(), cam.getEndDate());
                registration = r.getNewRegistrationByTime(cam.getStartDate(), cam.getEndDate());
                revenue = p.getRevenueByTime(cam.getStartDate(), cam.getEndDate());
            }
            request.setAttribute("revenueAllocation", revenueAllocation);
            request.setAttribute("newlyBought", newlyBought);
            request.setAttribute("newCustomer", newCustomer);
            request.setAttribute("registration", registration);
            request.setAttribute("revenue", revenue);
        }
        
        request.setAttribute("campaign", cam);
        request.setAttribute("action", "campaignStat");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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
