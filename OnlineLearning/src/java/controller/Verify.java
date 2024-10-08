/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.UserDAO;
import dal.VerificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
import model.Verification;

/**
 *
 * @author tuant
 */
public class Verify extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
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
        String userId = request.getParameter("userId");
        String code = request.getParameter("code");
        String email = request.getParameter("email");
        VerificationDAO vDao = new VerificationDAO();
        UserDAO uDAO = new UserDAO();
        Verification veri = vDao.checkCode(Integer.parseInt(userId), code);
        if(veri == null){
            request.setAttribute("error", true);
            request.setAttribute("userId", userId);
            request.setAttribute("email", email);
            request.getRequestDispatcher("verify.jsp").forward(request, response);
        }else{
            vDao.activeUser(Integer.parseInt(userId));
            vDao.deleteCode(Integer.parseInt(userId));
            Users user = uDAO.getUserByInfo("Email", email);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("Homepage.jsp");
        }
        
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
