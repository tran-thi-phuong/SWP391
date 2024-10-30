/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.AnswerDAO;
import dal.QuestionDAO;
import dal.QuestionMediaDAO;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Answer;
import model.Question;
import model.QuestionMedia;
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
    QuestionDAO questionDAO = new QuestionDAO();
    AnswerDAO answerDAO = new AnswerDAO();
    QuestionMediaDAO mediaDAO = new QuestionMediaDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String questionIdParam = request.getParameter("id");
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
    String API_KEY = "YOUR_API_KEY";
    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        switch (action) {
            case "edit":

                Question currentQuestion = questionDAO.getQuestionById(questionId);
                List<QuestionMedia> media = mediaDAO.getMediaByQuestionId(questionId);
                request.setAttribute("currentQuestion", currentQuestion);
                request.setAttribute("media", media);
                request.getRequestDispatcher("QuestionEdit.jsp").forward(request, response);
                break;

            case "delete":
                handleDeleteQuestion(request);
                break;
            case "summarize":
                Question q = questionDAO.getQuestionById(questionId);
                String userInput = q.getContent();
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
            // Add more cases for additional actions
            case "save":
                String content = request.getParameter("content");
                String status = request.getParameter("status");
                String level = request.getParameter("level");
                String lessonID = request.getParameter("lessonID");

                // Collect media files and descriptions
                List<String> mediaFiles = new ArrayList<>();

                List<String> mediaDescriptions = new ArrayList<>();
                List<String> mediaFilesExist = new ArrayList<>();
                List<String> mediaDescriptionsExist = new ArrayList<>();

                // Assuming you have a way to handle the media files
                String[] mediaFileParams = request.getParameterValues("mediaFiles");
                String[] mediaDescriptionParams = request.getParameterValues("mediaDescription");
                String[] mediaCurrent = request.getParameterValues("current-media");
                int mediaid = 0;

                if (mediaFileParams != null) {
                    for (String mediaFile : mediaFileParams) {
                        mediaFiles.add(mediaFile);
                    }
                }

                if (mediaDescriptionParams != null) {
                    for (String mediaDescription : mediaDescriptionParams) {
                        mediaDescriptions.add(mediaDescription);
                    }
                }
                if (mediaCurrent != null) {
                    for (String string : mediaCurrent) {
                        mediaid = Integer.parseInt(string);
                        QuestionMedia m = mediaDAO.getMediaById(mediaid);
                        mediaFilesExist.add(m.getMediaLink());
                        mediaDescriptionsExist.add(m.getDescription());
                    }
                }
                // Assuming mediaFiles is a list of parts received from the request
                List<Part> mediaFiless = request.getParts().stream()
                        .filter(part -> "mediaFiles".equals(part.getName()))
                        .collect(Collectors.toList());
                mediaDAO.deleteMedia(questionId);
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
                        // Example: mediaDao.add(mediaToAdd);
                    }
                }
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
                response.sendRedirect("QuestionDetail?id=" + questionId);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
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

    private void handleDeleteQuestion(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
