/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.Lesson;
import dal.LessonDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Users;

public class LessonView extends HttpServlet {

    private LessonDAO lessonDAO = new LessonDAO(); // Initialize your DAO

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        String lessonIdParam = request.getParameter("lessonId");
        String subjectId = request.getParameter("subjectId");
        
        int userId = (user != null) ? user.getUserID() : -1;
        int lessonId = (lessonIdParam != null && !lessonIdParam.isEmpty()) ? Integer.parseInt(lessonIdParam) : -1;

        // Retrieve lesson details
        Lesson lesson = lessonDAO.getLessonByLessonIDAndUserID(lessonId, userId);
        request.setAttribute("lesson", lesson);
        request.setAttribute("subjectId", subjectId);

        // Forward to JSP
        request.getRequestDispatcher("LessonView.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String subjectId = request.getParameter("subjectId");
        // Lấy lessonID từ request
        String lessonIDParam = request.getParameter("lessonID");
        if (lessonIDParam == null || lessonIDParam.isEmpty()) {
            response.sendRedirect("error.jsp"); // Hoặc trang thông báo lỗi khác nếu lessonID không hợp lệ
            return;
        }

        int lessonID = Integer.parseInt(lessonIDParam); // Chuyển đổi lessonID thành số nguyên

        // Lấy userID từ session
        Users user = (Users) session.getAttribute("user");
        int userID = (user != null) ? user.getUserID() : -1; // Kiểm tra xem user có tồn tại không

        // Kiểm tra nếu userID không hợp lệ
        if (userID == -1) {
            response.sendRedirect("error.jsp"); // Hoặc trang thông báo lỗi khác nếu không có user
            return;
        }

        boolean isUpdated = lessonDAO.updateLessonStatusToCompleted(lessonID, userID);

        // Kiểm tra xem việc cập nhật có thành công không
        if (isUpdated) {
            // Nếu cập nhật thành công, chuyển hướng đến LessonView với lessonID và subjectId
            response.sendRedirect("LessonView?lessonId=" + lessonID + "&subjectId=" + subjectId);
        } else {
            // Nếu cập nhật không thành công, chuyển hướng đến trang lỗi hoặc thông báo
            response.sendRedirect("error.jsp"); // Hoặc trang thông báo lỗi khác
        }
    }

}
