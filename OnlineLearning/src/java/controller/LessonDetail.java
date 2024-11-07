/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.LessonDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Lesson;
import model.SubjectTopic;
import model.Users;

/**
 *
 * @author tuant
 */
public class LessonDetail extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
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
            out.println("<title>Servlet LessonDetail</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LessonDetail at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//         if (!hasPermission(request, response)) return;
        LessonDAO l = new LessonDAO();
        SubjectDAO s = new SubjectDAO();
        String action = request.getParameter("action");
        if (action.equals("update")) {
            String lessonID = request.getParameter("lessonID");
            Lesson lesson = l.getLessonByLessonID(Integer.parseInt(lessonID));
            request.setAttribute("lesson", lesson);
        }
        String courseID = request.getParameter("courseID");
        List<SubjectTopic> lessonTopic = l.getAllLessonTopicBySubjectId(Integer.parseInt(courseID));
        String courseName = s.getSubjectNameById(Integer.parseInt(courseID));
        request.setAttribute("lessonTopic", lessonTopic);
        request.setAttribute("action", action);
        request.setAttribute("courseID", courseID);
        request.setAttribute("courseName", courseName);
        request.getRequestDispatcher("lessonDetail.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        LessonDAO l = new LessonDAO();
        SubjectDAO s = new SubjectDAO();
        String action = request.getParameter("action");
        String description = request.getParameter("description");
        String topic = request.getParameter("topic-select");
        String lessonName = request.getParameter("lesson-name");
        String status = request.getParameter("status-select");
        String order = request.getParameter("order");
        String content = request.getParameter("content");
        String courseID = request.getParameter("courseID");
        
        if (action.equals("update")) {
            String lessonID = request.getParameter("lessonID");
            Lesson lesson = l.getLessonByLessonID(Integer.parseInt(lessonID));
            if (l.validLessonOrder(Integer.parseInt(courseID), Integer.parseInt(topic), Integer.parseInt(order)) || Integer.parseInt(order) == lesson.getOrder()) {
                l.updateLesson(Integer.parseInt(lessonID), lessonName, Integer.parseInt(topic), content, Integer.parseInt(order), description, status);
                session.setAttribute("success", true);
                response.sendRedirect("lessonDetail?lessonID=" + lessonID + "&courseID=" + courseID + "&action=" + action);
            } else {
                session.setAttribute("error", true);
                response.sendRedirect("lessonDetail?lessonID=" + lessonID + "&courseID=" + courseID + "&action=" + action);
            }

        } else if (action.equals("add")) {
            if (l.validLessonOrder(Integer.parseInt(courseID), Integer.parseInt(topic), Integer.parseInt(order))) {
                l.addLesson(Integer.parseInt(courseID), lessonName, Integer.parseInt(topic), content, Integer.parseInt(order), description, status);
                session.setAttribute("success", true);
                response.sendRedirect("lessonDetail?courseID=" + courseID + "&action=" + action);
            } else {
                session.setAttribute("error", true);
                response.sendRedirect("lessonDetail?courseID=" + courseID + "&action=" + action);
            }
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
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
