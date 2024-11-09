/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import dal.LessonDAO;
import dal.PackagePriceDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import model.PackagePrice;
import model.Subject;
import model.SubjectCategory;
import model.SubjectTopic;
import model.Users;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns = {"/SubjectDetailDimension"})
public class SubjectDimension extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//           if (!hasPermission(request, response)) return;
        String subjectId = request.getParameter("id");
        request.getSession().setAttribute("subjectID", subjectId);
        LessonDAO lDAO = new LessonDAO();
        List<SubjectTopic> l = lDAO.getAllLessonTopicBySubjectId(Integer.parseInt(subjectId));
        request.setAttribute("dimensions", l);

        // Chuyển tiếp đến JSP
        request.getRequestDispatcher("/SubjectDetailDimension.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ request
        String action = request.getParameter("action");
        String subjectId = request.getParameter("subjectId");

        try {
            LessonDAO lessonDAO = new LessonDAO();

            if ("add".equals(action)) {
                addTopic(request, lessonDAO, subjectId);
            } else if ("update".equals(action)) {
                updateTopic(request, lessonDAO, subjectId);
            } else if ("delete".equals(action)) {
                deleteTopic(request, lessonDAO, subjectId); // Thêm subjectId vào delete
            }
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
                // Nếu có lỗi, chuyển tiếp đến trang JSP để hiển thị thông báo
                request.getRequestDispatcher("/SubjectDetailDimension.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            request.getRequestDispatcher("/SubjectDetailDimension.jsp").forward(request, response);
            return; // Thêm return để tránh gọi response.sendRedirect
        }

        response.sendRedirect("SubjectDetailDimension.jsp");
    }

    private void addTopic(HttpServletRequest request, LessonDAO lessonDAO, String subjectId) {
        int subjectID = Integer.parseInt(subjectId);
        String topicName = request.getParameter("dimensionName");
        int order = Integer.parseInt(request.getParameter("order"));

        if (lessonDAO.isOrderUnique(subjectID, order)) {
            SubjectTopic topic = new SubjectTopic();
            topic.setSubjectID(subjectID);
            topic.setTopicName(topicName);
            topic.setOrder(order);

            lessonDAO.addLessonTopic(topic);
        } else {
            request.setAttribute("errorMessage", "The order must be unique for each subject.");

        }
    }

    private void updateTopic(HttpServletRequest request, LessonDAO lessonDAO, String subjectId) {
        // Retrieve information from the request
        int topicID = Integer.parseInt(request.getParameter("id"));
        int subjectID = Integer.parseInt(subjectId);
        String newTopicName = request.getParameter("dimensionName");
        int newOrder = Integer.parseInt(request.getParameter("order"));

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

    private void deleteTopic(HttpServletRequest request, LessonDAO lessonDAO, String subjectId) {
        int topicId = Integer.parseInt(request.getParameter("id"));
        lessonDAO.deleteLessonTopic(topicId, Integer.parseInt(subjectId)); // Gọi delete với cả topicId và subjectId
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
