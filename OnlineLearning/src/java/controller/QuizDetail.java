/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.RolePermissionDAO;
//database access
import dal.TestDAO;
import dal.TestMediaDAO;
import dal.TestQuestionDAO;

//servlet default
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;
import java.util.List;

//model
import model.Test;
import model.TestMedia;

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
    TestMediaDAO mediaDAO = new TestMediaDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         if (!hasPermission(request, response)) {
            return;
        }
        //get id from parameter
        String testIdStr = request.getParameter("id");
        //Load data
        try {
            int testId = Integer.parseInt(testIdStr);
            Test currentTest = testDAO.getTestById(testId);
            int attemptCount = testDAO.countAttemptsByTestID(currentTest.getTestID());
            List<TestMedia> mediaList = mediaDAO.getMediaByTestId(testId);
            request.setAttribute("attemptCount", attemptCount);
            request.setAttribute("currentTest", currentTest);
            request.setAttribute("mediaList", mediaList);
            request.getRequestDispatcher("QuizDetail.jsp").forward(request, response);
        } catch (Exception e) {
            Test currentTest = new Test();
            int attemptCount = 1; //to navigate button
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
                List<TestMedia> mediaList = mediaDAO.getMediaByTestId(testId);

                // Check if the test exists
                if (currentTest != null) {
                    // Set the current test information as request attributes
                    request.setAttribute("currentTest", currentTest);
                    request.setAttribute("mediaList", mediaList);

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
                response.sendRedirect("QuizDetail?id=" + testId);
                break;

            default:
                // Handle unexpected action
                request.setAttribute("errorMessage", "Invalid action.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
        }

        // Redirect or forward to the success page or details page after the action
    }

    //edit test
    private void editTest(int testId, HttpServletRequest request, HttpServletResponse response) {

        // Get the test details from the database using the TestID
        Test currentTest = testDAO.getTestById(testId);
        List<TestMedia> mediaList = mediaDAO.getMediaByTestId(testId);

        // Check if the test exists
        if (currentTest != null) {
            // Set the current test information as request attributes
            request.setAttribute("currentTest", currentTest);
            request.setAttribute("mediaList", mediaList);

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
