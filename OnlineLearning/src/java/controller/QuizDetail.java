/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.TestDAO;
import dal.TestQuestionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Test;

/**
 *
 * @author 84336
 */
@WebServlet(name = "QuizDetail", urlPatterns = {"/QuizDetail"})
public class QuizDetail extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    TestDAO testDAO = new TestDAO();
    TestQuestionDAO testQuestionDAO = new TestQuestionDAO();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String testIdStr = request.getParameter("id");
        try {
            int testId = Integer.parseInt(testIdStr);
            Test currentTest = testDAO.getTestById(testId);
            int attemptCount = testDAO.countAttemptsByTestID(currentTest.getTestID());
            request.setAttribute("attemptCount", attemptCount);
            request.setAttribute("currentTest", currentTest);
            request.getRequestDispatcher("QuizDetail.jsp").forward(request, response);
        } catch (Exception e) {
            Test currentTest = new Test();
            int attemptCount = 1;
            request.setAttribute("attemptCount", attemptCount);
            request.setAttribute("currentTest", currentTest);
            request.getRequestDispatcher("QuizDetail.jsp").forward(request, response);
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
        String action = request.getParameter("action");
        String testIdStr = request.getParameter("testId");

        if (testIdStr == null || testIdStr.isEmpty()) {
            // Handle the case where testId is not provided
            request.setAttribute("errorMessage", "Test ID is required.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        int testId;
        try {
            testId = Integer.parseInt(testIdStr);
        } catch (NumberFormatException e) {
            // Handle invalid Test ID format
            request.setAttribute("errorMessage", "Invalid Test ID format.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Handle the action based on the button clicked
        switch (action) {
            case "edit":
                // Perform edit operation
                Test currentTest = testDAO.getTestById(testId);

                // Check if the test exists
                if (currentTest != null) {
                    // Set the current test information as request attributes
                    request.setAttribute("currentTest", currentTest);

                    // Forward to the editQuiz JSP
                    try {
                        request.getRequestDispatcher("EditQuiz.jsp").forward(request, response);
                    } catch (ServletException | IOException e) {
                        e.printStackTrace();
                        // Handle error in forwarding
                    }
                }
                break;

            case "delete":
                // Check attempts before deletion
                int attemptCount = testDAO.countAttemptsByTestID(testId);
                if (attemptCount > 0) {
                    request.setAttribute("errorMessage", "Cannot delete test; attempts exist.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                testQuestionDAO.clearQuestionsByTestId(testId);
                testDAO.deleteTest(testId);
                response.sendRedirect("QuizDetail?id="+testId);
                break;

            default:
                // Handle unexpected action
                request.setAttribute("errorMessage", "Invalid action.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
        }

        // Redirect or forward to the success page or details page after the action
    }

    private void editTest(int testId, HttpServletRequest request, HttpServletResponse response) {

        // Get the test details from the database using the TestID
        Test currentTest = testDAO.getTestById(testId);

        // Check if the test exists
        if (currentTest != null) {
            // Set the current test information as request attributes
            request.setAttribute("currentTest", currentTest);

            // Forward to the editQuiz JSP
            try {
                request.getRequestDispatcher("EditQuiz.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
                // Handle error in forwarding
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

}
