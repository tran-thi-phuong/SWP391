/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

/**
 *
 * @author sonna
 */
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Subject;
import model.Users;

public class SubjectView extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        // Initialize userId
        int userId = (user != null) ? user.getUserID() : -1;
        String subjectIdParam = request.getParameter("subjectId");

        // Parse subjectId from request parameter
        int subjectId = (subjectIdParam != null && !subjectIdParam.isEmpty()) ? Integer.parseInt(subjectIdParam) : -1;

        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects;

        // If the user is not logged in, retrieve the subject list based on subjectId only
        if (user == null) {
            subjects = subjectDAO.getSubjectDetailsBySubjectID(subjectId);
        } else {
            // If the user is logged in, retrieve the subject list based on userId and subjectId
            subjects = subjectDAO.getSubjectDetailsByUserIdAndSubjectID(userId, subjectId);
        }

        // Set the subjects attribute for use in the JSP
        request.setAttribute("subjects", subjects);

        // Forward the request to SubjectView.jsp
        request.getRequestDispatcher("SubjectView.jsp").forward(request, response);
    }

    
}
