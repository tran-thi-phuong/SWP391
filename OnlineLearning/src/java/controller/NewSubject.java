/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;
import dal.CategoryDAO;
import dal.SubjectDAO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.sql.SQLException;
import java.util.List;
import model.SubjectCategory;
/**
 *
 * @author Admin
 */
@MultipartConfig
public class NewSubject extends HttpServlet {
    private SubjectDAO subjectDAO;
    private CategoryDAO categoryDAO;
    private static final String UPLOAD_DIR = "images";
    @Override
    public void init() throws ServletException {
        super.init();
        subjectDAO = new SubjectDAO();
        categoryDAO = new CategoryDAO();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<SubjectCategory> categories = categoryDAO.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("newSubject.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form parameters
        String courseName = request.getParameter("courseName");
        String category = request.getParameter("category");
        String status = request.getParameter("status");
        String description = request.getParameter("description");
        Part filePart = request.getPart("thumbnail"); 
        String fileName = null;
        String filePath = null;
        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            File file = new File(uploadDir, fileName);
            filePart.write(file.getAbsolutePath());
            filePath = UPLOAD_DIR + "/" + fileName;
        }
        // Save course information to the database (giả sử bạn có SubjectDAO hoặc SubjectService để thêm vào DB)
        SubjectDAO subjectDAO = new SubjectDAO();
        boolean isAdded = subjectDAO.addSubject(courseName, category, status, description, filePath);
        if (isAdded) {
            // Redirect to success page or list of courses if add is successful
            response.sendRedirect("SubjectList.jsp");
        } else {
            // Handle failure to add course (gửi lỗi và chuyển về trang thêm mới)
            request.setAttribute("errorMessage", "Failed to add the course.");
            request.getRequestDispatcher("newSubject").forward(request, response);
        }
    }

}