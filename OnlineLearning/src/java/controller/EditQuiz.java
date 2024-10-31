/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.QuestionDAO;
import dal.RolePermissionDAO;
import dal.TestDAO;
import dal.TestQuestionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Question;
import model.Test;
import model.TestQuestion;
import model.Users;

/**
 *
 * @author 84336
 */
@MultipartConfig
@WebServlet(name = "EditQuiz", urlPatterns = {"/EditQuiz"})
public class EditQuiz extends HttpServlet {

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
            out.println("<title>Servlet EditQuiz</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditQuiz at " + request.getContextPath() + "</h1>");
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
         if (!hasPermission(request, response)) {
            return;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    TestDAO testDAO = new TestDAO();
    QuestionDAO questionDAO = new QuestionDAO();
    TestQuestionDAO testQuestionDAO = new TestQuestionDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String testIdStr = request.getParameter("testId");

        System.out.println("Test ID String: " + testIdStr); // Debug log

        if (testIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Test ID is missing.");
            return;
        }
        int testId = Integer.parseInt(testIdStr);
        int attemptCount = testDAO.countAttemptsByTestID(testId);

        if (attemptCount > 0) {
            // Set error message and redirect to error page
            request.setAttribute("errorMessage", "Test already taken.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return; // Exit to prevent further processing
        }
        request.setAttribute("testId", testId);
        switch (action) {
            case "edit":
                handleEditTest(request);
                response.sendRedirect("QuizDetail?id=" + testId);
                break;

            case "updateQuestions":
                handleUpdateQuestions(request);
                request.getRequestDispatcher("QuestionQuiz.jsp").forward(request, response);
                break;
            case "updateQuestionQuiz":
                handleUpdateQuestionQuiz(request);
                response.sendRedirect("QuizDetail?id=" + testId);
                break;
            default:
                // Handle unexpected action or provide a default response
                request.setAttribute("errorMessage", "Invalid action specified.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                break;
        }
    }

    private void handleEditTest(HttpServletRequest request) {
        int testId = Integer.parseInt(request.getParameter("testId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String mediaType = request.getParameter("mediaType");
        String mediaDescription = request.getParameter("mediaDescription");
        String type = request.getParameter("type");
        int duration = Integer.parseInt(request.getParameter("duration"));
        double passCondition = Double.parseDouble(request.getParameter("passCondition"));
        String level = request.getParameter("level");
        int quantity = testQuestionDAO.countQuestionsByTestId(testId);
        int subjectId = Integer.parseInt(request.getParameter("subjectId"));
        Test current = testDAO.getTestById(testId);
        // Handle file upload
        String mediaURL = current.getMediaURL();
        Part mediaFilePart;
        try {
            mediaFilePart = request.getPart("mediaURL");

            if (mediaFilePart != null && mediaFilePart.getSize() > 0) {
                // Determine the target directory based on media type
                String uploadDir = mediaType.equals("image") ? "images/" : "videos/";
                mediaURL = saveMediaFile(mediaFilePart, uploadDir); // Save the file and get the URL
            }
        } catch (IOException | ServletException ex) {
            Logger.getLogger(EditQuiz.class.getName()).log(Level.SEVERE, null, ex);
        }

        Test updatedTest = new Test(testId, subjectId, title, description, type, level, mediaType, mediaURL, duration, passCondition, mediaDescription, quantity);

        testDAO.updateTest(updatedTest); // Update quiz details in the database
    }

    private void handleUpdateQuestions(HttpServletRequest request) throws ServletException, IOException {
        int testId = Integer.parseInt(request.getParameter("testId"));
        int subjectId = Integer.parseInt(request.getParameter("subjectId")); // Get subjectId from the form

        // Retrieve questions related to the subject
        List<Question> questionsBySubject = questionDAO.getQuestionsBySubjectID(subjectId);

        // Retrieve questions already associated with the test
        List<Question> questionsByTest = questionDAO.getQuestionsByTestId(testId);

        // Set attributes to send to JSP
        request.setAttribute("questionsBySubject", questionsBySubject);
        request.setAttribute("questionsByTest", questionsByTest);

    }

    private String saveMediaFile(Part filePart, String uploadDir) throws IOException {
        if (filePart != null && filePart.getSize() > 0) {
            // Get the file name from the uploaded part
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Construct the upload path using the web application context
            String uploadPath = getServletContext().getRealPath("/") + uploadDir;

            // Create the directory if it does not exist
            File uploadDirectory = new File(uploadPath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs(); // Create any necessary parent directories
            }

            // Create a new file object for the uploaded file
            File file = new File(uploadDirectory, fileName);

            // Write the file to the upload path
            filePart.write(file.getAbsolutePath());

            // Return the relative path for accessing the file
            return uploadDir + "" + fileName; // Ensure the path is relative to the web context
        }
        return null; // Return null if no file was uploaded
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

    private void handleUpdateQuestionQuiz(HttpServletRequest request) {
        int testId = Integer.parseInt(request.getParameter("testId"));
        String[] selectedQuestions = request.getParameterValues("selectedQuestions");
        testQuestionDAO.clearQuestionsByTestId(testId);

        if (selectedQuestions != null) {
            for (String questionIdStr : selectedQuestions) {
                int questionId = Integer.parseInt(questionIdStr);
                // Logic to associate each selected question with the quiz
                testQuestionDAO.addTestQuestion(new TestQuestion(testId, questionId));

            }
        }
        Test test = testDAO.getTestById(testId);
        test.setQuantity(selectedQuestions.length);
        testDAO.updateTest(test);
    }

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
            response.sendRedirect("error.jspF");
            return false;
        }

        return true; // Người dùng có quyền truy cập trang này
    }
}