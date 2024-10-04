/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PackagePriceDAO;
import dal.RegistrationsDAO;
import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PackagePrice;

/**
 *
 * @author 84336
 */
@WebServlet(name = "payment", urlPatterns = {"/payment"})
public class payment extends HttpServlet {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }
        return result.toString();
    }
    RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    UserDAO userDAO = new UserDAO();
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
        int subjectID =  Integer.parseInt(request.getParameter("subjectID"));
        PackagePriceDAO PackagePriceDAO = new PackagePriceDAO();
        PackagePrice currentPac = PackagePriceDAO.searchByPackagePriceId(packageId);
        double price = Math.min(currentPac.getSalePrice(), currentPac.getPrice());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            int userID = userDAO.addUser(email, phone, gender, phone, generateRandomString(10));
            registrationsDAO.addRegistration(userID, subjectID, packageId, price);
            out.println("<html><head><title>Submission Details</title></head><body>");
        out.println("<h1>Form Submission Details</h1>");
        out.println("<p><strong>Package ID:</strong> " + currentPac.getDurationTime() + "days - " + currentPac.getSalePrice() + "$</p>");
        out.println("<p><strong>Full Name:</strong> " + fullname + "</p>");
        out.println("<p><strong>Email:</strong> " + email + "</p>");
        out.println("<p><strong>Phone:</strong> " + phone + "</p>");
        out.println("<p><strong>Gender:</strong> " + gender + "</p>");
        out.println("<p><strong>Status:Submission Successful </strong></p>");
        out.println("</body></html>");
        } catch (SQLException ex) {
            System.out.println(ex);
            out.println("<p><strong>Status:Submission Failed </strong></p>");
        }
        
        
        // Get the PrintWriter to write the response
        

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
