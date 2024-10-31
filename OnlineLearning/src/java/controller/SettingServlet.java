/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//database access
import dal.SettingDAO;

//servlet default
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//model
import model.SystemSetting;
import model.Users;

/**
 *
 * @author 84336
 */
@WebServlet(name = "SettingServlet", urlPatterns = {"/SettingServlet"})
public class SettingServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    SettingDAO settingDAO = new SettingDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //check user login
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        SystemSetting setting = (SystemSetting) session.getAttribute("setting");
        //get setting
        if (setting == null) {
            setting = settingDAO.getSettingsByUserID(user.getUserID());
            if (setting == null) {
                setting = new SystemSetting();
                setting.setUserID(user.getUserID());
                settingDAO.addSetting(user.getUserID());
            }
        }

        // Retrieve form data
        String numberOfItemsStr = request.getParameter("numberOfItems");
        int numberOfItems = (numberOfItemsStr != null && !numberOfItemsStr.isEmpty()) ? Integer.parseInt(numberOfItemsStr) : 10;

        // Retrieve checkbox values
        boolean quizID = request.getParameter("quizID") != null;
        boolean name = request.getParameter("title") != null;
        boolean subject = request.getParameter("subject") != null;
        boolean description = request.getParameter("description") != null; // Added
        boolean level = request.getParameter("level") != null;
        boolean numberOfQuestions = request.getParameter("numberOfQuestions") != null;
        boolean duration = request.getParameter("duration") != null;
        boolean passRate = request.getParameter("passRate") != null;
        boolean passCondition = request.getParameter("passCondition") != null;
        boolean quizType = request.getParameter("quizType") != null;

// Retrieve the number of items per page from the request

// Update settings object
        setting.setNumberOfItems(numberOfItems);
        setting.setQuizID(quizID);
        setting.setTitle(name);
        setting.setSubject(subject);
        setting.setDescription(description); // Added
        setting.setLevel(level);
        setting.setQuantity(numberOfQuestions);
        setting.setDuration(duration);
        setting.setPassCondition(passCondition);
        setting.setPassRate(passRate);
        setting.setQuizType(quizType);

        settingDAO.updateSetting(setting);

        // Update the session
        session.setAttribute("setting", setting);
        response.sendRedirect("QuizList");
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

}
