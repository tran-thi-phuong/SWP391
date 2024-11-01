/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//database access
import dal.AnswerDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;

//default servlet
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//data structure
import java.util.ArrayList;
import java.util.List;

//for model
import model.Answer;
import model.Users;

/**
 *
 * @author 84336
 */
@WebServlet(name = "EditAnswer", urlPatterns = {"/EditAnswer"})
public class EditAnswer extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    AnswerDAO answerDAO = new AnswerDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EditAnswer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditAnswer at " + request.getContextPath() + "</h1>");
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
         if (!hasPermission(request, response)) return;
        //get item from the form submit
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        String[] answerContents = request.getParameterValues("answers.content");
        String[] answerExplanations = request.getParameterValues("answers.explanation");
        String[] correctAnswers = request.getParameterValues("answers.correct");
        List<Answer> answerList = new ArrayList<>();
        Answer a;
        //add answer to answer list
        for (int i = 0; i < answerContents.length; i++) {
            String content = answerContents[i];
            String explanation = answerExplanations[i];
            String correct = correctAnswers[i];
            //validate
            if (content != null && explanation != null && correct != null && !content.trim().isEmpty() && !explanation.trim().isEmpty() && !correct.isEmpty()){
                a = new Answer(questionId, content, explanation, "true".equals(correct));
                answerList.add(a);
            }
        }
        //delete old answer
        answerDAO.clearAnswersByQuestionId(questionId);
        
        //add new answer
        for (Answer answer : answerList) {
            answerDAO.addAnswer(answer);
        }
        response.sendRedirect("QuestionDetail?id=" + questionId);
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
            response.sendRedirect("/Homepage");
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true;
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

}
