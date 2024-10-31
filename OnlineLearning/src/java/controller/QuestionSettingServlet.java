/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.SettingDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.QuestionSetting;
import model.Users;

/**
 *
 * @author 84336
 */
@WebServlet(name = "QuestionSettingServlet", urlPatterns = {"/QuestionSettingServlet"})
public class QuestionSettingServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    SettingDAO questionSettingDAO = new SettingDAO();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        QuestionSetting questionSetting = (QuestionSetting) session.getAttribute("questionSetting");

        // Initialize questionSetting if it doesn't exist
        if (questionSetting == null) {
            questionSetting = questionSettingDAO.getQuestionSettingsByUserID(user.getUserID());
            if (questionSetting == null) {
                questionSetting = new QuestionSetting();
                questionSetting.setUserID(user.getUserID());
                questionSettingDAO.addSetting(user.getUserID());
            }
        }

        // Retrieve form data
        String numberOfItemsStr = request.getParameter("numberOfItems");
        int numberOfItems = (numberOfItemsStr != null && !numberOfItemsStr.isEmpty())
                ? Integer.parseInt(numberOfItemsStr) : 10;

        // Retrieve checkbox values
        boolean showContent = request.getParameter("showContent") != null;
        boolean showLessonID = request.getParameter("showLessonID") != null;
        boolean showStatus = request.getParameter("showStatus") != null;
        boolean showLevel = request.getParameter("showLevel") != null;

        // Update questionSetting object
        questionSetting.setNumberOfItems(numberOfItems);
        questionSetting.setShowContent(showContent);
        questionSetting.setShowLessonID(showLessonID);
        questionSetting.setShowStatus(showStatus);
        questionSetting.setShowLevel(showLevel);

        // Save the updated settings
        questionSettingDAO.updateQuestionSetting(questionSetting);

        // Update the session
        session.setAttribute("QuestionSetting", questionSetting);

        // Redirect to Question List page
        response.sendRedirect("QuestionList");
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
