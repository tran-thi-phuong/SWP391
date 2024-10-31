/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.TestDAO;
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
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Test;
import model.Users;

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return; // Exit if the user lacks permission
        }
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String mediaType = request.getParameter("mediaType");
        String mediaDescription = request.getParameter("mediaDescription");
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
        newTest.setMediaType(mediaType);
        newTest.setMediaDescription(mediaDescription);
        newTest.setType(type);
        newTest.setDuration(duration);
        newTest.setPassCondition(passCondition);
        newTest.setLevel(level);
        newTest.setQuantity(quantity);
        newTest.setSubjectID(subjectId);

        String mediaURL = "";
        Part mediaFilePart;
        try {
            mediaFilePart = request.getPart("mediaURL");

            if (mediaFilePart != null && mediaFilePart.getSize() > 0) {
                // Determine the target directory based on media type
                String uploadDir = mediaType.equals("image") ? "images/" : "videos/";
                mediaURL = saveMediaFile(mediaFilePart, uploadDir);
            }
        } catch (IOException | ServletException ex) {
            Logger.getLogger(EditQuiz.class.getName()).log(Level.SEVERE, null, ex);
        }
        newTest.setMediaURL(mediaURL);
        // Use TestDAO to save the new quiz to the database
        TestDAO testDAO = new TestDAO();
        int id = testDAO.addTest(newTest);

        response.sendRedirect("QuizDetail?id=" + id);
        
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
        response.sendRedirect("error.jsp");
        return false;
    }

    return true; // Người dùng có quyền truy cập trang này
}
}
