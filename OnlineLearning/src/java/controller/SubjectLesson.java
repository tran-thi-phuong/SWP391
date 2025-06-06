/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.LessonDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Lesson;
import model.Users;
import model.SubjectTopic;

/**
 *
 * @author tuant
 */
public class SubjectLesson extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SubjectLesson</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SubjectLesson at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//         if (!hasPermission(request, response)) {
//            return;
//        }
     
        String courseId = request.getParameter("courseId");
        String courseName = request.getParameter("courseName");
        LessonDAO l = new LessonDAO();
        List<SubjectTopic> lessonType = l.getAllLessonTopicBySubjectId(Integer.parseInt(courseId));
        List<Lesson> lessonList = l.getAllLessonBySubjectId(Integer.parseInt(courseId));
        request.setAttribute("lessonType", lessonType);
        request.setAttribute("lessonList", lessonList);
        request.setAttribute("courseName", courseName);
        request.setAttribute("courseId", courseId);
        request.setAttribute("selectStatus", "all");
        request.setAttribute("selectTopic", "all");
        request.getRequestDispatcher("subjectLesson.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String courseId = request.getParameter("courseId");
        String courseName = request.getParameter("courseName");
        String searchValue = request.getParameter("search-value");
        String status = request.getParameter("select-status");
        String topic = request.getParameter("select-topic");
        LessonDAO l = new LessonDAO();
        // get all topic
        List<SubjectTopic> lessonType = l.getAllLessonTopicBySubjectId(Integer.parseInt(courseId));
        List<Lesson> lessonList;
        if(searchValue != null && !searchValue.trim().isEmpty()){
            request.setAttribute("searchValue", searchValue);
            // get all lesson of subject
           lessonList = l.searchLesson(Integer.parseInt(courseId), searchValue);
        }else{
            // get all lesson of subject
           lessonList = l.getAllLessonBySubjectId(Integer.parseInt(courseId)); 
        }
        request.setAttribute("lessonType", lessonType);
        request.setAttribute("lessonList", lessonList);
        request.setAttribute("courseName", courseName);
        request.setAttribute("courseId", courseId);
        request.setAttribute("selectStatus", status);
        request.setAttribute("selectTopic", topic);
        request.getRequestDispatcher("subjectLesson.jsp").forward(request, response); // forward request to subject lesson page
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession();
    Users currentUser = (Users) session.getAttribute("user");

    // Kiểm tra nếu người dùng chưa đăng nhập thì chuyển hướng đến trang đăng nhập
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return false;
    }

    // Lấy quyền của người dùng và kiểm tra quyền truy cập với trang hiện tại
    String userRole = currentUser.getRole();
    RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
    Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

    // Nếu người dùng đã đăng nhập nhưng không có quyền, chuyển hướng về /homePage
    if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
        response.sendRedirect(request.getContextPath() + "/Homepage");

        return false;
    } else if (pageID == null) {
        // Nếu không tìm thấy trang trong hệ thống phân quyền, chuyển đến trang lỗi
        response.sendRedirect("error.jsp");
        return false;
    }

    return true; // Người dùng có quyền truy cập trang này
}
}
