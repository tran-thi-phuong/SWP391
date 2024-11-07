/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;
import dal.CategoryDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import dal.UserDAO;
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
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.sql.SQLException;
import java.util.List;
import model.SubjectCategory;
import model.Users;
/**
 *
 * @author Admin
 */
@MultipartConfig
public class NewSubject extends HttpServlet {
    private CategoryDAO categoryDAO;
    private UserDAO UserDAO;
    private static final String UPLOAD_DIR = "images";
    @Override
    public void init() throws ServletException {
        super.init();
        categoryDAO = new CategoryDAO();
        UserDAO = new UserDAO();
    }
    /**
     * Handles GET requests
     * Displays the form for creating a new subject
     * Loads all available categories for the dropdown menu
     * @param request
     * @param response
     * @throws jakarta.servlet.ServletException
     * @throws java.io.IOException
     **/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        if (!hasPermission(request, response)) {
//            return;
//        }
        try {
            List<SubjectCategory> categories = categoryDAO.getAllCategories();
            List<Users> instructor = UserDAO.getInstructors();
            request.setAttribute("Instructor", instructor);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("newSubject.jsp").forward(request, response);
        } catch (ServletException | IOException ex) {
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
        String instructor = request.getParameter("instructor");
        // Process the uploaded file (thumbnail image)
        Part filePart = request.getPart("thumbnail");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        
        // Create uploads directory if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the uploaded file
        File file = new File(uploadDir, fileName);
            filePart.write(file.getAbsolutePath());
        String filePath =UPLOAD_DIR + "/" + fileName;
        // After saving the file, you could save the form data and file path to a database
        SubjectDAO subjectDAO = new SubjectDAO();
        boolean isAdded = subjectDAO.addSubject(courseName, category, status, description, filePath, instructor);
        if (isAdded) {
            // Redirect to success page or list of courses if add is successful
            response.sendRedirect("SubjectList");
        } else {
            // Handle failure to add course
            request.setAttribute("errorMessage", "Failed to add the course.");
            request.getRequestDispatcher("newSubject").forward(request, response);
        }
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
