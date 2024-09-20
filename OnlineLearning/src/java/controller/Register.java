/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.sql.SQLException;
import dal.UserDAO;
import dal.VerificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
import util.SendEmail;
import util.GenerateCode;

/**
 *
 * @author DELL
 */
@WebServlet(name = "Register", urlPatterns = {"/register"})
public class Register extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            Users user = (Users) session.getAttribute("user");
            String role = user.getRole().toLowerCase();

            switch (role) {
                case "admin":
                    response.sendRedirect("dashboard.jsp");
                    break;
                case "customer":
                    response.sendRedirect("Homepage.jsp");
                    break;
                default:
                    response.sendRedirect("Homepage.jsp");
                    break;
            }
        } else {
            request.getRequestDispatcher("Homepage.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullname = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String re_password = request.getParameter("repassword");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        if (phone.isEmpty()) {
            phone = null;
        }
        if (gender.equals("none")) {
            gender = null;
        }
        if (address.isEmpty()) {
            address = null;
        }
        UserDAO uDAO = new UserDAO();

        try {
            Users emailCheck = uDAO.getUserByInfo("Email", email);
            Users usernameCheck = uDAO.getUserByInfo("Username", username);
            Users phoneCheck = uDAO.getUserByInfo("Phone", phone);

            if (!password.equals(re_password)) {
                request.setAttribute("passNotMatch", true);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else if (emailCheck != null) {
                request.setAttribute("invalidEmail", true);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else if (usernameCheck != null) {
                request.setAttribute("invalidUsername", true);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else if (phoneCheck != null && phone != null) {
                request.setAttribute("invalidPhone", true);
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                VerificationDAO veri = new VerificationDAO();
                int userId = uDAO.register(fullname, username, email, password, phone, address, gender, "Customer", "Inactive");
                String code = GenerateCode.generateRandomCode();
                SendEmail.sendCode(email, code);
                veri.generateCode(userId, code);
                request.setAttribute("email", email);
                request.setAttribute("userId", userId);
                request.getRequestDispatcher("verify.jsp").forward(request, response);
            
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }
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
