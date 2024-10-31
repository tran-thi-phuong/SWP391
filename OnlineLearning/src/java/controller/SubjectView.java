/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

/**
 *
 * @author sonna
 */
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

        // Khởi tạo userId
        int userId = (user != null) ? user.getUserID() : -1;
        String subjectIdParam = request.getParameter("subjectId");

        // Phân tích subjectId từ tham số yêu cầu
        int subjectId = (subjectIdParam != null && !subjectIdParam.isEmpty()) ? Integer.parseInt(subjectIdParam) : -1;

        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects;

        // Nếu người dùng chưa đăng nhập, lấy danh sách môn học chỉ dựa trên subjectId
        if (user == null) {
            subjects = subjectDAO.getSubjectDetailsBySubjectID(subjectId);
        } else {
            // Nếu người dùng đã đăng nhập, lấy danh sách môn học dựa trên userId và subjectId
            subjects = subjectDAO.getSubjectDetailsByUserIdAndSubjectID(userId, subjectId);
        }

        // Thiết lập thuộc tính subjects để sử dụng trong JSP
        request.setAttribute("subjects", subjects);

        // Chuyển tiếp yêu cầu đến trang SubjectView.jsp
        request.getRequestDispatcher("SubjectView.jsp").forward(request, response);
    }
}
