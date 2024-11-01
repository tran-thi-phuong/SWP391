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

    private LessonDAO lessonDAO = new LessonDAO(); // Initialize your DAO

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         if (!hasPermission(request, response)) return;
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        String lessonIdParam = request.getParameter("lessonId");
        String subjectIdd = request.getParameter("subjectId");
        
        int userId = (user != null) ? user.getUserID() : -1;
        int lessonId = (lessonIdParam != null && !lessonIdParam.isEmpty()) ? Integer.parseInt(lessonIdParam) : -1;
        int subjectId = (subjectIdd != null && !subjectIdd.isEmpty()) ? Integer.parseInt(subjectIdd) : -1; 
        // Retrieve lesson details
        Lesson lesson = lessonDAO.getLessonByLessonIDAndUserID(lessonId, userId);
        request.setAttribute("lesson", lesson);
        request.setAttribute("subjectId", subjectId);
        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects;

        // Nếu người dùng chưa đăng nhập, lấy danh sách môn học chỉ dựa trên subjectId
        if (user == null) {
            subjects = subjectDAO.getSubjectDetailsBySubjectID(subjectId);
        } else {
            // Nếu người dùng đã đăng nhập, lấy danh sách môn học dựa trên userId và subjectId
            subjects = subjectDAO.getSubjectDetailsByUserIdAndSubjectID(userId, subjectId);
        }

        request.setAttribute("subjects", subjects);

        // Forward to JSP
        request.getRequestDispatcher("LessonView.jsp").forward(request, response);
    }

    @Override
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
