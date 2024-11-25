/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.LessonDAO;
import dal.SubjectDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Lesson;
import model.Subject;

/**
 *
 * @author Admin
 */
@WebServlet(name="TryCourse", urlPatterns={"/TryCourse"})
public class TryCourse extends HttpServlet {
   
    private static final long serialVersionUID = 1L;

    // Example DAOs, you should replace these with your actual DAO classes
    private SubjectDAO subjectDAO = new SubjectDAO();
    private LessonDAO lessonDAO = new LessonDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the subjectId and lessonId from the request parameters
        String subjectIdStr = request.getParameter("subjectId");
       

        // Validate the input (ensure both subjectId and lessonId are provided)
        if (subjectIdStr != null ) {
            try {
                // Parse the subjectId to an integer
                int subjectId = Integer.parseInt(subjectIdStr);

                // Retrieve the subject using the subjectId
                Subject subject = subjectDAO.getSubjectById(subjectId);
                
                // Check if the subject exists
                if (subject != null) {
                    // Retrieve the first lesson from the lessonDAO based on the subjectId
                    Lesson firstLesson = lessonDAO.getFirstLessonBySubjectId(subjectId);

                    // Check if the first lesson exists
                    if (firstLesson != null) {
                        // Set the subject and first lesson as request attributes
                        request.setAttribute("subject", subject);
                        request.setAttribute("lesson", firstLesson);

                        // Forward to the LessonView page to display the lesson
                        request.getRequestDispatcher("/TryCourse.jsp").forward(request, response);
                    } else {
                        // Lesson not found, redirect or display an error
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Lesson not found.");
                    }
                } else {
                    // Subject not found, redirect or display an error
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Subject not found.");
                }
            } catch (NumberFormatException e) {
                // Handle invalid subjectId format
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subjectId format.");
            } catch (Exception e) {
                // Handle any other errors during retrieval
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving lesson.");
            }
        } else {
            // If the required parameters are missing
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing subjectId or lessonId.");
        }
    }
}
