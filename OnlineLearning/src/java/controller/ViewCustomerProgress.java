/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.CustomerCourse;
import dal.Customer_SubjectDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import jakarta.servlet.http.HttpSession;
import model.Users;

/**
 *
 * @author Admin
 */
@WebServlet(name="ViewCustomerProgress", urlPatterns={"/ViewCustomerProgress"})
public class ViewCustomerProgress extends HttpServlet {
   
   @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
     if (!hasPermission(request, response)) {
            return;
        }
    Customer_SubjectDAO csubjectDAO = new Customer_SubjectDAO();
     SubjectDAO subjectDAO = new SubjectDAO();
    try {
        // Get SubjectID from request parameter
        String subjectIdParam = request.getParameter("subjectID");
        if (subjectIdParam == null || subjectIdParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subject ID is required.");
            return;
        }

        int subjectID = Integer.parseInt(subjectIdParam);

        // Fetch customer progress for the subject
        List<CustomerCourse> customerProgressList = csubjectDAO.getCustomerProgressBySubjectID(subjectID);
        int customer = csubjectDAO.countUsersByCourseID(subjectID);
        String subjectTitle = subjectDAO.getSubjectTitleByID(subjectID);

        // Set attributes for forwarding
        request.setAttribute("customerProgressList", customerProgressList);
        request.setAttribute("subjectID", subjectID);
        request.setAttribute("subjectTitle", subjectTitle);
         request.setAttribute("customer", customer);

        // Forward to ViewCustomerProgress.jsp
        request.getRequestDispatcher("ViewCustomerProgress.jsp").forward(request, response);
    } catch (Exception ex) {
        throw new ServletException(ex);
    }
}
   private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());
        String userRole = currentUser.getRole();

        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage");
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true;
    }
}
