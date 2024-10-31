/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//for access database
import dal.QuestionDAO;
import dal.QuestionMediaDAO;

//servlet default
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//for save media file
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;

//data structure
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//model
import model.Question;
import model.QuestionMedia;

/**
 *
 * @author 84336
 */
@MultipartConfig
@WebServlet(name = "AddQuestion", urlPatterns = {"/AddQuestion"})
public class AddQuestion extends HttpServlet {

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
            out.println("<title>Servlet AddQuestion</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddQuestion at " + request.getContextPath() + "</h1>");
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
    QuestionDAO questionDAO = new QuestionDAO();
    QuestionMediaDAO mediaDAO = new QuestionMediaDAO();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // To save a question after editing it
        String content = request.getParameter("content");
        String status = request.getParameter("status");
        String level = request.getParameter("level");
        int lessonID = Integer.parseInt(request.getParameter("lessonID"));
        //check content valid
        if (content.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

// Create an updated Question object
        Question updated = new Question(lessonID, status, content, level);

// Update the question first
        int questionId = questionDAO.addQuestion(updated);

// Collect new media files and their descriptions
        List<Part> mediaFilesParts = request.getParts().stream()
                .filter(part -> "mediaFiles".equals(part.getName()))
                .collect(Collectors.toList());

        List<String> mediaDescriptions = new ArrayList<>();
        for (Part mediaFilePart : mediaFilesParts) {
            String mediaDescription = request.getParameter("mediaDescription");
            mediaDescriptions.add(mediaDescription);

            String uploadDir = "questionmedia/"; // Directory to save media

            // Save the file and get the saved file URL
            String mediaLink = saveMediaFile(mediaFilePart, uploadDir);

            if (mediaLink != null) {
                // Create a new QuestionMedia object
                QuestionMedia mediaToAdd = new QuestionMedia();
                mediaToAdd.setMediaLink(mediaLink);
                mediaToAdd.setDescription(mediaDescription);
                mediaToAdd.setQuestionID(questionId); // Set this to the appropriate Question ID

                // Save the media to the database
                mediaDAO.saveMedia(mediaToAdd);
            }
        }

// Redirect to the question detail page
        response.sendRedirect("QuestionList");
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
