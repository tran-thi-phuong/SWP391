/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.AnswerDAO;
import dal.PagesDAO;
import dal.QuestionDAO;
import dal.QuestionMediaDAO;
import dal.RolePermissionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Answer;
import model.Question;
import model.QuestionMedia;
import model.Users;

/**
 *
 * @author 84336
 */
@WebServlet(name = "QuestionDetail", urlPatterns = {"/QuestionDetail"})
public class QuestionDetail extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private QuestionDAO questionDAO;
    private AnswerDAO answerDAO;
    private QuestionMediaDAO mediaDAO;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         if (!hasPermission(request, response)) {
            return;
        }
        String questionIdParam = request.getParameter("questionId");
        if (questionIdParam != null) {
            try {
                int questionId = Integer.parseInt(questionIdParam);

                // Retrieve the question using the DAO
                Question currentQuestion = questionDAO.getQuestionById(questionId);

                // Retrieve associated answers and media
                List<Answer> answers = answerDAO.getAnswersByQuestionId(questionId);
                List<QuestionMedia> media = mediaDAO.getMediaByQuestionId(questionId);

                // Set attributes for the JSP
                request.setAttribute("currentQuestion", currentQuestion);
                request.setAttribute("answers", answers);
                request.setAttribute("media", media);

                // Forward to the JSP page
                request.getRequestDispatcher("/QuestionDetail.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                // Handle the case where questionId is not a valid number
                response.sendRedirect("error.jsp?message=Invalid question ID");
            }
        } else {
            // Handle the case where questionId is missing
            response.sendRedirect("error.jsp?message=Question ID is required");
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
        processRequest(request, response);
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
        processRequest(request, response);
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
        response.sendRedirect("/Homepage");
        return false;
    } else if (pageID == null) {
        // Nếu không tìm thấy trang trong hệ thống phân quyền, chuyển đến trang lỗi
        response.sendRedirect("error.jsp");
        return false;
    }

    return true; // Người dùng có quyền truy cập trang này
}
}
