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
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Subject;
import model.SubjectCategory;
import model.Users;
/**
 *
 * @author Admin
 */
@WebServlet(name = "SubjectList", urlPatterns = {"/SubjectList"})
public class SubjectList extends HttpServlet {

    private SubjectDAO subjectDAO;
    private CategoryDAO categoryDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        subjectDAO = new SubjectDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         if (!hasPermission(request, response)) {
            return;
        }
        try {
            int page = getPageNumber(request);
            int recordsPerPage = 6;
            String query = request.getParameter("search");
            String categoryId = request.getParameter("category");
            SubjectListResult subjectResult = getSubjects(query,categoryId, page, recordsPerPage);
            List<SubjectCategory> categories = categoryDAO.getAllCategories();
            setRequestAttributes(request, subjectResult, categories, page, recordsPerPage, query, categoryId);
            request.getRequestDispatcher("SubjectList.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        return (pageParam != null) ? Integer.parseInt(pageParam) : 1;
    }

    private SubjectListResult getSubjects(String query, String categoryId, int page, int recordsPerPage) throws SQLException {
        int offset = (page - 1) * recordsPerPage;
        if (query != null && !query.trim().isEmpty()) {
            return new SubjectListResult(
                    subjectDAO.searchSubjects(query, offset, recordsPerPage),
                    subjectDAO.getTotalSearchSubjects(query)
            );
        } else if (categoryId != null && !categoryId.trim().isEmpty()) {
            int catId = Integer.parseInt(categoryId);
            return new SubjectListResult(
                    subjectDAO.getSubjectsByCategory(catId, offset, recordsPerPage),
                    subjectDAO.getTotalSubjectsByCategory(catId)
            );
        } else {
            return new SubjectListResult(
                    subjectDAO.getAllSubjects(offset, recordsPerPage),
                    subjectDAO.getTotalSubjects()
            );
        }
    }

    private void setRequestAttributes(HttpServletRequest request, SubjectListResult subjectResult,
            List<SubjectCategory> categories, int currentPage, int recordsPerPage, String query, String categoryId) {
        int totalPages = (int) Math.ceil(subjectResult.totalRecords * 1.0 / recordsPerPage);
        request.setAttribute("subjects", subjectResult.subjects);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("query", query);
        request.setAttribute("categoryId", categoryId);
    }

    private static class SubjectListResult {

        List<Subject> subjects;
        int totalRecords;

        SubjectListResult(List<Subject> subjects, int totalRecords) {
            this.subjects = subjects;
            this.totalRecords = totalRecords;
        }

        public SubjectListResult() {
        }

        public List<Subject> getSubjects() {
            return subjects;
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
