/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//database access
import dal.QuestionDAO;
import dal.TestDAO;
import dal.TestMediaDAO;
import dal.TestQuestionDAO;

//servlet default
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//for save media
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

//data structure
import java.util.List;

//for debugging
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//model
import model.Question;
import model.Test;
import model.TestMedia;
import model.TestQuestion;

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
        request.setAttribute("errorMessage", "You cannot access this page directly.");
        request.getRequestDispatcher("error.jsp").forward(request, response);
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
    TestMediaDAO mediaDAO = new TestMediaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get action and id of test
        String action = request.getParameter("action");
        String testIdStr = request.getParameter("testId");

        System.out.println("Test ID String: " + testIdStr); // Debug log
        //check null id
        if (testIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Test ID is missing.");
            return;
        }
        //check attempt of quiz
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
                //edit Test
                handleEditTest(request);
                response.sendRedirect("QuizDetail?id=" + testId);
                break;

            case "updateQuestions":
                //update Test
                handleUpdateQuestions(request);
                request.getRequestDispatcher("QuestionQuiz.jsp").forward(request, response);
                break;
            case "updateQuestionQuiz":
                //updateQuestionQuiz
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
    //Edit Test
    private void handleEditTest(HttpServletRequest request) throws IOException, ServletException {
        int testId = Integer.parseInt(request.getParameter("testId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String type = request.getParameter("type");
        int duration = Integer.parseInt(request.getParameter("duration"));
        double passCondition = Double.parseDouble(request.getParameter("passCondition"));
        String level = request.getParameter("level");
        int quantity = testQuestionDAO.countQuestionsByTestId(testId);
        int subjectId = Integer.parseInt(request.getParameter("subjectId"));
        Test current = testDAO.getTestById(testId);
        current.setDescription(description);
        current.setDuration(duration);
        current.setLevel(level);
        current.setPassCondition(passCondition);
        current.setQuantity(quantity);
        current.setSubjectID(subjectId);
        current.setTitle(title);
        current.setType(type);
        List<String> mediaFilesExist = new ArrayList<>();
        List<String> mediaDescriptionsExist = new ArrayList<>();
        List<Part> mediaFilesParts = request.getParts().stream()
                .filter(part -> "mediaFiles".equals(part.getName()))
                .collect(Collectors.toList());
        List<String> mediaDescriptions = new ArrayList<>();
        
        String[] mediaCurrent = request.getParameterValues("current-media");
        int mediaid;
        //save current change
        if (mediaCurrent != null) {
                    for (String string : mediaCurrent) {
                        mediaid = Integer.parseInt(string);
                        TestMedia m = mediaDAO.getMediaById(mediaid);
                        mediaFilesExist.add(m.getMediaLink());
                        mediaDescriptionsExist.add(m.getDescription());
                    }
                }
        //delete all
        mediaDAO.deleteMedia(testId);
        for (Part mediaFilePart : mediaFilesParts) {
            String mediaDescription = request.getParameter("mediaDescription");
            mediaDescriptions.add(mediaDescription);

            String uploadDir = "TestMedia/"; // Directory to save media

            // Save the file and get the saved file URL
            String mediaLink = saveMediaFile(mediaFilePart, uploadDir);

            if (mediaLink != null) {
                // Create a new QuestionMedia object
                TestMedia mediaToAdd = new TestMedia();
                mediaToAdd.setMediaLink(mediaLink);
                mediaToAdd.setDescription(mediaDescription);
                mediaToAdd.setTestId(testId); // Set this to the appropriate Question ID

                // Save the media to the database
                mediaDAO.saveMedia(mediaToAdd);
            }
        //add current
        for (int i = 0; i < mediaFilesExist.size(); i++) {
                    mediaLink = mediaFilesExist.get(i);
                    mediaDescription = (i < mediaDescriptionsExist.size()) ? mediaDescriptionsExist.get(i) : ""; // Avoid IndexOutOfBounds

                    // Create a new QuestionMedia object for existing media
                    TestMedia existingMedia = new TestMedia();
                    existingMedia.setMediaLink(mediaLink);
                    existingMedia.setDescription(mediaDescription);
                    existingMedia.setTestId(testId); // Assuming you have the questionId available

                    // Save the existing media back to the database
                    mediaDAO.saveMedia(existingMedia);
                }
        testDAO.updateTest(current); // Update quiz details in the database
    }
    }
    //Update Question
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
    //Save media
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
    //for update quiz
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
        if(selectedQuestions!=null)
        {
            test.setQuantity(selectedQuestions.length);
        }
        testDAO.updateTest(test);
    }

}
