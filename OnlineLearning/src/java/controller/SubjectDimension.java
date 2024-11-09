/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.LessonDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.SubjectTopic;
import model.Users;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns = {"/SubjectDetailDimension"})
public class SubjectDimension extends HttpServlet {

    // Handles GET request to display the subject details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the user has permission to access this page
        if (!hasPermission(request, response)) {
            return;
        }

        String subjectId = request.getParameter("id"); // Get subject ID from request parameters
        request.getSession().setAttribute("subjectID", subjectId); // Store subject ID in session

        // Get the list of lessons/topics related to the subject from the database
        LessonDAO lDAO = new LessonDAO();
        List<SubjectTopic> topics = lDAO.getAllLessonTopicBySubjectId(Integer.parseInt(subjectId));
        request.setAttribute("dimensions", topics); // Set the topics in request attribute

        // Forward the request to the JSP page for rendering
        request.getRequestDispatcher("/SubjectDetailDimension.jsp").forward(request, response);
    }

    // Handles POST request for actions like add, update, or delete topic
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the action parameter from the request (add, update, delete)
        String action = request.getParameter("action");
        String subjectId = request.getParameter("subjectId"); // Get subject ID from request parameters

        try {
            LessonDAO lessonDAO = new LessonDAO();

            // Perform actions based on the specified action parameter
            if ("add".equals(action)) {
                addTopic(request, lessonDAO, subjectId); // Add a new topic
            } else if ("update".equals(action)) {
                updateTopic(request, lessonDAO, subjectId); // Update an existing topic
            } else if ("delete".equals(action)) {
                deleteTopic(request, lessonDAO, subjectId); // Delete a topic
            }

            // Check if there are any error messages
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
                // If there is an error, forward the request to the JSP page to show the message
                request.getRequestDispatcher("/SubjectDetailDimension.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            // Forward the request to the JSP page to show the error message
            request.getRequestDispatcher("/SubjectDetailDimension.jsp").forward(request, response);
            return; // Return to prevent the redirect from being called
        }

        // Redirect to the SubjectDetailDimension.jsp page after successfully processing the request
        response.sendRedirect("SubjectDetailDimension.jsp");
    }

    // Adds a new topic for the subject
    private void addTopic(HttpServletRequest request, LessonDAO lessonDAO, String subjectId) {
        int subjectID = Integer.parseInt(subjectId); // Parse subject ID from request
        String topicName = request.getParameter("dimensionName"); // Get the topic name
        int order = Integer.parseInt(request.getParameter("order")); // Get the order for the topic

        // Check if the order is unique for the subject
        if (lessonDAO.isOrderUnique(subjectID, order)) {
            SubjectTopic topic = new SubjectTopic();
            topic.setSubjectID(subjectID);
            topic.setTopicName(topicName);
            topic.setOrder(order);

            // Add the new topic to the database
            lessonDAO.addLessonTopic(topic);
        } else {
            // Set an error message if the order is not unique
            request.setAttribute("errorMessage", "The order must be unique for each subject.");
        }
    }

    // Updates an existing topic for the subject
    private void updateTopic(HttpServletRequest request, LessonDAO lessonDAO, String subjectId) {
        // Retrieve information from the request
        int topicID = Integer.parseInt(request.getParameter("id")); // Get topic ID
        int subjectID = Integer.parseInt(subjectId); // Get subject ID
        String newTopicName = request.getParameter("dimensionName"); // Get the new topic name
        int newOrder = Integer.parseInt(request.getParameter("order")); // Get the new order

        // Fetch the old topic from the database for comparison
        SubjectTopic oldTopic = lessonDAO.getLessonTopicById(topicID);

        // Check if the order has changed
        if (oldTopic.getOrder() != newOrder) {
            // If the order has changed, verify if the new order is unique
            if (lessonDAO.isOrderUnique(subjectID, newOrder)) {
                // If the new order is unique, update both the topic name and the order
                oldTopic.setTopicName(newTopicName);
                oldTopic.setOrder(newOrder);
                lessonDAO.updateLessonTopic(oldTopic);
            } else {
                // Handle the case when the new order is not unique (e.g., show an error message)
                request.setAttribute("error", "The order already exists. Please choose a different order.");
            }
        } else {
            // If the order has not changed, only update the topic name
            oldTopic.setTopicName(newTopicName);
            lessonDAO.updateLessonTopic(oldTopic);
        }
    }

    // Deletes a topic from the subject
    private void deleteTopic(HttpServletRequest request, LessonDAO lessonDAO, String subjectId) {
        int topicId = Integer.parseInt(request.getParameter("id")); // Get topic ID from request
        lessonDAO.deleteLessonTopic(topicId, Integer.parseInt(subjectId)); // Delete the topic from the database
    }

    // Checks if the user has permission to access this page
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user"); // Get the current user from the session

        // If the user is not logged in, redirect to the login page
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString()); // Get the page ID from the URL
        String userRole = currentUser.getRole(); // Get the user's role

        // Check if the user has permission to access this page
        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage"); // Redirect to homepage if no permission
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp"); // Redirect to error page if no page ID
            return false;
        }

        return true; // User has permission to access the page
    }
}
