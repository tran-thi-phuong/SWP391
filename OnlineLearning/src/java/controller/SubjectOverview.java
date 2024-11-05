/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import dal.PackagePriceDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import java.io.IOException;
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
import java.util.Date;
import java.util.List;
import model.PackagePrice;
import model.Subject;
import model.SubjectCategory;
import model.Users;

/**
 *
 * @author Admin
 */
@MultipartConfig
@WebServlet(urlPatterns = {"/SubjectDetailOverview"})
public class SubjectOverview extends HttpServlet {

    public static final String UPLOAD_DIR = "images";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }
        String subjectId = request.getParameter("id");
        request.getSession().setAttribute("subjectID", subjectId);
        SubjectDAO sDAO = new SubjectDAO();
        Subject subject = sDAO.getSubjectById(Integer.parseInt(subjectId));
        request.setAttribute("subject", subject);
        CategoryDAO cDAO = new CategoryDAO();
        List<SubjectCategory> categories = cDAO.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/SubjectDetailOverview.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String subjectId = request.getParameter("subjectId");
        String subjectName = request.getParameter("subjectName");
        String category = request.getParameter("category");
        String status = request.getParameter("status");
        String description = request.getParameter("description");
        Part filePart = request.getPart("thumbnail");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uploadPath = getServletContext().getRealPath("/") + "images" + File.separator;

        // Create uploads directory if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the uploaded file
        filePart.write(uploadPath + fileName);
        String filePath = fileName;

        try {
            SubjectDAO sDAO = new SubjectDAO();
            sDAO.updateSubject(subjectName, category, status, description, subjectId, filePath);
            response.sendRedirect("SubjectList");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error saving subject: " + e.getMessage());
            request.getRequestDispatcher("/SubjectDetailOverview.jsp").forward(request, response);
        }
    }

    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());
        String userRole = currentUser.getRole();

        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
           response.sendRedirect(request.getContextPath() + "/Homepage");

            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true;
    }
}
