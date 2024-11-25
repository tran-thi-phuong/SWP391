/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.Lesson;
import dal.LessonDAO;
import dal.Lesson_UserDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Subject;
import model.Users;

public class LessonView extends HttpServlet {

    private LessonDAO lessonDAO = new LessonDAO(); // Initialize DAO
    private Lesson_UserDAO luserDAO = new Lesson_UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }

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

        Date deadline = null;
        String deadlineNotice = null;

        try {
            // Retrieve the deadline
            deadline = luserDAO.getDeadline(lessonId, userId);

            // Check and compare the deadline
            if (deadline != null) {
                Date currentDate = new Date(); // Get the current date

                if (deadline.after(currentDate)) {
                    deadlineNotice = "You still have time. The deadline is on " + deadline;
                } else if (deadline.equals(currentDate)) {
                    deadlineNotice = "The deadline is today: " + deadline;
                } else {
                    deadlineNotice = "The deadline was on " + deadline + ". It is overdue!";
                }
            } else {
                deadlineNotice = "No deadline found for this lesson.";
            }
        } catch (SQLException ex) {
            Logger.getLogger(LessonView.class.getName()).log(Level.SEVERE, null, ex);
            deadlineNotice = "An error occurred while retrieving the deadline.";
        }

        // Set attributes for the notice and deadline
        request.setAttribute("deadline", deadline);
        request.setAttribute("deadlineNotice", deadlineNotice);

        // Retrieve subjects
        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects;

        if (user == null) {
            subjects = subjectDAO.getSubjectDetailsBySubjectID(subjectId); // For guests
        } else {
            subjects = subjectDAO.getSubjectDetailsByUserIdAndSubjectID(userId, subjectId); // For logged-in users
        }

        request.setAttribute("subjects", subjects);

        // Forward to LessonView.jsp
        request.getRequestDispatcher("LessonView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }
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
