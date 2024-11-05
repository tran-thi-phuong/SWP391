/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//for Database connect
import dal.LessonDAO;
import dal.PagesDAO;
import dal.QuestionDAO;
import dal.RolePermissionDAO;
import dal.SettingDAO;

//default servlet
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

//for model
import model.Lesson;
import model.Question;
import model.QuestionSetting;
import model.Users;

/**
 *
 * @author 84336
 */
@WebServlet(name = "QuestionList", urlPatterns = {"/QuestionList"})
public class QuestionList extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //initialize DAO
    SettingDAO settingDAO = new SettingDAO();
    QuestionDAO questionDAO = new QuestionDAO();
    LessonDAO lessonDAO = new LessonDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
           if (!hasPermission(request, response)) return;
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        QuestionSetting setting = (QuestionSetting) session.getAttribute("QuestionSetting");
        if (setting == null) {
            // Create a new setting if none exists
            setting = settingDAO.getQuestionSettingsByUserID(user.getUserID());
            if (setting != null) {
                session.setAttribute("QuestionSetting", setting);
            }
        }
        //get data from query
        String search = request.getParameter("search");
        String lessonID = request.getParameter("lesson");
        String status = request.getParameter("status");
        String level = request.getParameter("level");
        String currentPage = request.getParameter("page");
        String order = request.getParameter("order");

        // Default values for ordering
        String orderBy = "QuestionID"; // Default sorting column
        String direction = "ASC"; // Default sorting direction
        // Parse the order parameter
        if (order != null && !order.isEmpty()) {
            String[] parts = order.split("_");
            if (parts.length == 2) {
                orderBy = parts[0];
                direction = parts[1].toUpperCase(); // Ensure it's in uppercase for SQL
            }
        }
        // Default is 1
        if (currentPage == null) {
            currentPage = "1";
        }
        
        //calculate pagination
        int pageNumber = (currentPage != null) ? Integer.parseInt(currentPage) : 1;
        int pageSize = (setting != null) ? setting.getNumberOfItems() : 10;

        // Load questions based on the parameters
        List<Question> questions = questionDAO.getAllQuestions(pageNumber, pageSize, search, lessonID, status, level, orderBy, direction);
        int totalQuestions = questionDAO.getTotalQuestionCount(search, lessonID, status, level);

        int totalPages = (int) Math.ceil((double) totalQuestions / pageSize);

        // Set attributes for the JSP
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("questionList", questions);
        request.setAttribute("currentPage", currentPage);
        List<Lesson> lessons = lessonDAO.getAllLessons();
        request.setAttribute("lessonList", lessons);

        // Forward to the JSP
        request.getRequestDispatcher("QuestionList.jsp").forward(request, response);
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
