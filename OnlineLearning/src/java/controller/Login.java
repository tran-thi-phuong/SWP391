/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Users;

/**
 *
 * @author tuant
 */
public class Login extends HttpServlet {

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
        UserDAO uDAO = new UserDAO();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Users user = uDAO.getUser(username, password);
        if (user == null) {
            Boolean error = true;
            request.setAttribute("error", error);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {

            if (user.getStatus().toLowerCase().equals("active")) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                if (user.getRole().toLowerCase().equals("customer")) {
                    response.sendRedirect("Homepage.jsp");
                } else {
                    response.sendRedirect("dashboard.jsp");
                }
            } else {
                Users u1 = uDAO.getUserByInfo("Email", username);
                Users u2 = uDAO.getUserByInfo("Username", username);
                if(u1 != null){
                    request.setAttribute("userId", u1.getUserID());
                    request.setAttribute("email", u1.getEmail());
                }else{
                    request.setAttribute("userId", u2.getUserID());
                    request.setAttribute("email", u2.getEmail());
                }
                request.getRequestDispatcher("verify.jsp").forward(request, response);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}