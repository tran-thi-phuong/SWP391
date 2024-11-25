/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//for database access
import dal.AnswerDAO;
import dal.PagesDAO;
import dal.QuestionDAO;
import dal.QuestionMediaDAO;
import dal.RolePermissionDAO;
import dal.LessonDAO;
import dal.QuestionDAO;
import dal.QuestionMediaDAO;
import dal.TestDAO;
import dal.TestQuestionDAO;

//Servlet default
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//for save file
import jakarta.servlet.http.Part;
import java.io.File;

//for Call API
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;

//data structure to save data
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//object model
import model.Answer;
import model.Lesson;
import model.Question;
import model.QuestionMedia;
import model.Users;
import model.Test;

//to handle json response
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author 84336
 */
@MultipartConfig
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
    //Initialize data access layer
    QuestionDAO questionDAO = new QuestionDAO();
    AnswerDAO answerDAO = new AnswerDAO();
    QuestionMediaDAO mediaDAO = new QuestionMediaDAO();
    LessonDAO lessonDAO = new LessonDAO();
    TestQuestionDAO testQuestionDAO = new TestQuestionDAO();
    TestDAO testDAO = new TestDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         if (!hasPermission(request, response)) {
            return;
        }
        //get the id of question
        String questionIdParam = request.getParameter("id");
        if (questionIdParam != null) {
            try {
                int questionId = Integer.parseInt(questionIdParam);

                // Retrieve the question using the DAO
                Question currentQuestion = questionDAO.getQuestionById(questionId);

                // Check if the question exists
                if (currentQuestion == null) {
                    // Redirect to error page if question does not exist
                    response.sendRedirect("error.jsp?message=Question ID does not exist");
                    return; // Exit to prevent further processing
                }

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
    //setup URL for call API
    String API_KEY = "AIzaSyDYjULffQIiLVTTSSSMa73IryeF_PriRW8"; //Replace with your API key
    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get action and id of question
        String action = request.getParameter("action");
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        switch (action) {
            case "edit":
                //forward data to page QuestionEdit.jsp
                Question currentQuestion = questionDAO.getQuestionById(questionId);
                List<QuestionMedia> media = mediaDAO.getMediaByQuestionId(questionId);
                List<Lesson> lesson = lessonDAO.getAllLessons();
                request.setAttribute("lessonList", lesson);
                request.setAttribute("currentQuestion", currentQuestion);
                request.setAttribute("media", media);
                request.getRequestDispatcher("QuestionEdit.jsp").forward(request, response);
                break;

            case "delete":
                List<Integer> testIDs = testQuestionDAO.getTestIDsByQuestionID(questionId);
                testQuestionDAO.deleteTestQuestionsByQuestionID(questionId);
                if (!testIDs.isEmpty()) {
                    for (int testID : testIDs) {
                        // Count remaining questions for the test
                        int remainingQuestions = testQuestionDAO.countQuestionsByTestId(testID); // Update the test with the new question count
                        Test test = testDAO.getTestById(testID);// Assuming you have a way to retrieve or build the Test object
                        test.setQuantity(remainingQuestions); // Update with remaining question count

                        testDAO.updateTest(test);
                    }
                }
                mediaDAO.deleteMedia(questionId);
                answerDAO.clearAnswersByQuestionId(questionId);
                questionDAO.deleteQuestionById(questionId);
                response.sendRedirect("QuestionList");
                break;
            case "summarize":
                //summarize question
                Question q = questionDAO.getQuestionById(questionId);
                String userInput = q.getContent();
                userInput = userInput.replace("\"","");
                userInput += "Summarize it with simple words, keep the meaning, if it is question, don't answer";
                String jsonResponse = callGenerativeAPI(userInput);
                String extractedText = extractTextFromJson(jsonResponse);
                extractedText = extractedText.replace("*", "");
                String summary = extractedText.trim();
                HttpSession session = request.getSession();

                // Check if the summary attribute is already set
                String existingSummary = (String) session.getAttribute("summarize");
                if (existingSummary == null) {
                    // If not, set the summary in the session
                    session.setAttribute("summarize", summary);
                }
                response.sendRedirect("QuestionDetail?id=" + questionId);
                break;
            case "save":
                //To save a question after edit it
                String content = request.getParameter("content");
                String status = request.getParameter("status");
                String level = request.getParameter("level");
                int lessonID = Integer.parseInt(request.getParameter("lessonID"));

                if (content.trim().equals("")) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                }
                Question updated = new Question(lessonID, status, content, level);
                //update question first
                questionDAO.updateQuestion(questionId, updated);
                // Collect media files and descriptions
                List<String> mediaFiles = new ArrayList<>();

                List<String> mediaDescriptions = new ArrayList<>();
                List<String> mediaFilesExist = new ArrayList<>();
                List<String> mediaDescriptionsExist = new ArrayList<>();

                // Get data from rquest
                String[] mediaFileParams = request.getParameterValues("mediaFiles");
                String[] mediaDescriptionParams = request.getParameterValues("mediaDescription");
                String[] mediaCurrent = request.getParameterValues("current-media");

                int mediaid = 0;
                //Add infomation from request to list
                if (mediaFileParams != null) {
                    mediaFiles.addAll(Arrays.asList(mediaFileParams));
                }

                if (mediaDescriptionParams != null) {
                    mediaDescriptions.addAll(Arrays.asList(mediaDescriptionParams));
                }
                if (mediaCurrent != null) {
                    for (String string : mediaCurrent) {
                        mediaid = Integer.parseInt(string);
                        QuestionMedia m = mediaDAO.getMediaById(mediaid);
                        mediaFilesExist.add(m.getMediaLink());
                        mediaDescriptionsExist.add(m.getDescription());
                    }
                }
                //Get all part of new media to a part
                List<Part> mediaFiless = request.getParts().stream()
                        .filter(part -> "mediaFiles".equals(part.getName()) && part.getSize()>0)
                        .collect(Collectors.toList());
                //delete current media first
                mediaDAO.deleteMedia(questionId);
                //loop to save new media
                for (int i = 0; i < mediaFiless.size(); i++) {
                    Part mediaFilePart = mediaFiless.get(i);
                    String mediaDescription = (i < mediaDescriptions.size()) ? mediaDescriptions.get(i) : ""; // Avoid IndexOutOfBounds

                    String uploadDir = "questionmedia/"; // Directory to save media

                    // Save the file and get the saved file URL
                    String mediaLink = saveMediaFile(mediaFilePart, uploadDir);

                    if (mediaLink != null) {
                        // Create a new QuestionMedia object or however your DAO is set up
                        QuestionMedia mediaToAdd = new QuestionMedia();
                        mediaToAdd.setMediaLink(mediaLink);
                        mediaToAdd.setDescription(mediaDescription);
                        mediaToAdd.setQuestionID(questionId); // Set this to the appropriate Question ID

                        mediaDAO.saveMedia(mediaToAdd);
                        // Save the mediaToAdd object to your database or list
                    }
                }
                //Loop to save exist media
                for (int i = 0; i < mediaFilesExist.size(); i++) {
                    String mediaLink = mediaFilesExist.get(i);
                    String mediaDescription = (i < mediaDescriptionsExist.size()) ? mediaDescriptionsExist.get(i) : ""; // Avoid IndexOutOfBounds

                    // Create a new QuestionMedia object for existing media
                    QuestionMedia existingMedia = new QuestionMedia();
                    existingMedia.setMediaLink(mediaLink);
                    existingMedia.setDescription(mediaDescription);
                    existingMedia.setQuestionID(questionId); // Assuming you have the questionId available

                    // Save the existing media back to the database
                    mediaDAO.saveMedia(existingMedia);
                }
                //back to detail
                response.sendRedirect("QuestionDetail?id=" + questionId);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    //Save media file into custom uploadDir
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

    //Call gemini API
    private String callGenerativeAPI(String input) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", input);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] inputBytes = jsonInputString.getBytes("utf-8");
            os.write(inputBytes, 0, inputBytes.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    //Hanlde json response to get text (Need clean manually)
    private static String extractTextFromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray candidates = jsonObject.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONObject content = firstCandidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        return parts.getJSONObject(0).getString("text");
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
    private void handleDeleteQuestion(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
