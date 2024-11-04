/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//database access
import dal.TestDAO;
import dal.TestMediaDAO;

//servlet default
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//for saving media
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
//for debugging
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

//model
import model.Test;
import model.TestMedia;

/**
 *
 * @author 84336
 */
@MultipartConfig
@WebServlet(name = "AddQuiz", urlPatterns = {"/AddQuiz"})
public class AddQuiz extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //Method to save media to an uploadDir custom
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
    TestDAO testDAO = new TestDAO();
    TestMediaDAO mediaDAO = new TestMediaDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get data from the form
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String type = request.getParameter("type");
        int duration = Integer.parseInt(request.getParameter("duration"));
        double passCondition = Double.parseDouble(request.getParameter("passCondition"));
        String level = request.getParameter("level");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int subjectId = Integer.parseInt(request.getParameter("subjectId"));

        // Create a new Test object
        Test newTest = new Test();
        newTest.setTitle(title);
        newTest.setDescription(description);
        newTest.setType(type);
        newTest.setDuration(duration);
        newTest.setPassCondition(passCondition);
        newTest.setLevel(level);
        newTest.setQuantity(quantity);
        newTest.setSubjectID(subjectId);
        int testID = testDAO.addTest(newTest);
        //Save media
        List<Part> mediaFilesParts = request.getParts().stream()
                .filter(part -> "mediaFiles".equals(part.getName()))
                .collect(Collectors.toList());
        List<String> mediaDescriptions = new ArrayList<>();
        if (mediaFilesParts != null && mediaFilesParts.size() > 0) {
        for (Part mediaFilePart : mediaFilesParts) {
                String mediaDescription = request.getParameter("mediaDescription");
                mediaDescriptions.add(mediaDescription);

                String uploadDir = "questionmedia/"; // Directory to save media

                // Save the file and get the saved file URL
                String mediaLink = saveMediaFile(mediaFilePart, uploadDir);

                if (mediaLink != null) {
                    // Create a new QuestionMedia object
                    TestMedia mediaToAdd = new TestMedia();
                    mediaToAdd.setMediaLink(mediaLink);
                    mediaToAdd.setDescription(mediaDescription);
                    mediaToAdd.setTestId(testID); // Set this to the appropriate Question ID

                    // Save the media to the database
                    mediaDAO.saveMedia(mediaToAdd);
                }

            }
        }
            System.out.println("");
            response.sendRedirect("QuizDetail?id=" + testID);
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
