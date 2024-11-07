/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.Lesson;
import dal.LessonDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Subject;
import model.Users;

public class LessonView extends HttpServlet {

    private LessonDAO lessonDAO = new LessonDAO(); // Initialize DAO

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//         if (!hasPermission(request, response)) return;
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        String lessonIdParam = request.getParameter("lessonId");
        String subjectIdParam = request.getParameter("subjectId");
        
        int userId = (user != null) ? user.getUserID() : -1;
        int lessonId = (lessonIdParam != null && !lessonIdParam.isEmpty()) ? Integer.parseInt(lessonIdParam) : -1;
        int subjectId = (subjectIdParam != null && !subjectIdParam.isEmpty()) ? Integer.parseInt(subjectIdParam) : -1;

        // Retrieve lesson details
        Lesson lesson = lessonDAO.getLessonByLessonIDAndUserID(lessonId, userId);
        request.setAttribute("lesson", lesson);
        request.setAttribute("subjectId", subjectId);

        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects;

        // If the user is not logged in, retrieve subjects based only on subjectId
        if (user == null) {
            subjects = subjectDAO.getSubjectDetailsBySubjectID(subjectId);
        } else {
            // If the user is logged in, retrieve subjects based on userId and subjectId
            subjects = subjectDAO.getSubjectDetailsByUserIdAndSubjectID(userId, subjectId);
        }

        request.setAttribute("subjects", subjects);

        // Forward to LessonView.jsp
        request.getRequestDispatcher("LessonView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String subjectId = request.getParameter("subjectId");

        // Retrieve lessonID from the request
        String lessonIDParam = request.getParameter("lessonID");
        if (lessonIDParam == null || lessonIDParam.isEmpty()) {
            response.sendRedirect("error.jsp"); // Redirect to error page if lessonID is invalid
            return;
        }

        int lessonID = Integer.parseInt(lessonIDParam); // Convert lessonID to an integer

        // Retrieve userID from the session
        Users user = (Users) session.getAttribute("user");
        int userID = (user != null) ? user.getUserID() : -1; // Check if user exists

        // Check if userID is invalid
        if (userID == -1) {
            response.sendRedirect("error.jsp"); // Redirect to error page if user is not logged in
            return;
        }

        boolean isUpdated = lessonDAO.updateLessonStatusToCompleted(lessonID, userID);

        // Check if the update was successful
        if (isUpdated) {
            // If update was successful, redirect to LessonView with lessonID and subjectId
            response.sendRedirect("LessonView?lessonId=" + lessonID + "&subjectId=" + subjectId);
        } else {
            // If update failed, redirect to error page or display an error message
            response.sendRedirect("error.jsp"); // Redirect to error page
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
