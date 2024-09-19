/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PackagePriceDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.PackagePrice;

/**
 *
 * @author 84336
 */
@WebServlet(name = "payment", urlPatterns = {"/payment"})
public class payment extends HttpServlet {

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
        request.setAttribute("errorMessage", "You cannot access this page directly.");
        request.getRequestDispatcher("error.jsp").forward(request, response);
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
        processRequest(request, response);
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
        int packageId = Integer.parseInt(request.getParameter("package"));
        String fullname = request.getParameter("full-name");
        String email = request.getParameter("email");
        String phone = request.getParameter("mobile");
        String gender = request.getParameter("gender");
        response.setContentType("text/html");
        PackagePriceDAO PackagePriceDAO = new PackagePriceDAO();
        PackagePrice currentPac = PackagePriceDAO.searchByPackagePriceId(packageId);
        // Get the PrintWriter to write the response
        PrintWriter out = response.getWriter();

        // Write the HTML response
        out.println("<html><head><title>Submission Details</title></head><body>");
        out.println("<h1>Form Submission Details</h1>");
        out.println("<p><strong>Package ID:</strong> " + currentPac.getDurationTime() + "days - " + currentPac.getSalePrice() + "$</p>");
        out.println("<p><strong>Full Name:</strong> " + fullname + "</p>");
        out.println("<p><strong>Email:</strong> " + email + "</p>");
        out.println("<p><strong>Phone:</strong> " + phone + "</p>");
        out.println("<p><strong>Gender:</strong> " + gender + "</p>");
        out.println("</body></html>");

        // Close the PrintWriter
        out.close();

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
