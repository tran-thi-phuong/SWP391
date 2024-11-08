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
import model.Lesson;
import dal.LessonDAO;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SubjectList", urlPatterns = {"/SubjectList"})
public class SubjectList extends HttpServlet {

    // Data Access Objects for database operations
    private SubjectDAO subjectDAO;
    private CategoryDAO categoryDAO;
    private UserDAO userDAO;
    private LessonDAO lessonDAO;

    /**
     * Initializes the servlet and creates necessary DAO instances
     */
    @Override
    public void init() throws ServletException {
        super.init();
        subjectDAO = new SubjectDAO();
        categoryDAO = new CategoryDAO();
        lessonDAO = new LessonDAO();
    }

    /**
     * Handles GET requests for subject listing Processes search queries,
     * category filters, and pagination
     *
     * @param request
     * @param response
     * @throws jakarta.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//         if (!hasPermission(request, response)) {
//            return;
//        }
        try {
            // Get pagination parameters and search/filter criteria
            int page = getPageNumber(request);
            int recordsPerPage = 6;  // Number of subjects displayed per page
            String query = request.getParameter("search");
            String categoryId = request.getParameter("category");
            String status = request.getParameter("status");
            // Fetch subjects based on search criteria and pagination
            SubjectListResult subjectResult = getSubjects(query, categoryId,status, page, recordsPerPage);

            // Get all categories for the filter dropdown
            List<SubjectCategory> categories = categoryDAO.getAllCategories();

            Map<Integer, Integer> lessonCounts = new HashMap<>();
            for (Subject subject : subjectResult.subjects) {
                int lessonCount = lessonDAO.countLessonsBySubjectId(subject.getSubjectID());
                lessonCounts.put(subject.getSubjectID(), lessonCount);
            }
            request.setAttribute("lessonCounts", lessonCounts);

            // Set attributes for JSP rendering
            setRequestAttributes(request, subjectResult, categories, page, recordsPerPage, query, categoryId);

            // Forward to the JSP view
            request.getRequestDispatcher("SubjectList.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        return (pageParam != null) ? Integer.parseInt(pageParam) : 1;
    }

    private SubjectListResult getSubjects(String query, String categoryId, String status, int page, int recordsPerPage) throws SQLException {
        int offset = (page - 1) * recordsPerPage;

        // Chuẩn bị các tiêu chí tìm kiếm
        List<Subject> subjects;
        int totalRecords;

        // Xây dựng điều kiện tìm kiếm linh hoạt
        if ((query != null && !query.trim().isEmpty())
                || (categoryId != null && !categoryId.trim().isEmpty())
                || (status != null && !status.trim().isEmpty())) {

            // Lấy kết quả dựa trên các điều kiện
            subjects = subjectDAO.searchSubjectsWithFilters(query, categoryId, status, offset, recordsPerPage);
            totalRecords = subjectDAO.getTotalSubjectsWithFilters(query, categoryId, status);
        } else {
            // Nếu không có điều kiện nào, trả về tất cả các môn học
            subjects = subjectDAO.getAllSubjects(offset, recordsPerPage);
            totalRecords = subjectDAO.getTotalSubjects();
        }

        return new SubjectListResult(subjects, totalRecords);
    }

    /**
     * Sets request attributes for JSP rendering Includes pagination
     * information, search criteria, and subject data
     */
    private void setRequestAttributes(HttpServletRequest request, SubjectListResult subjectResult,
            List<SubjectCategory> categories, int currentPage, int recordsPerPage, String query, String categoryId) {
        // Calculate total pages for pagination
        int totalPages = (int) Math.ceil(subjectResult.totalRecords * 1.0 / recordsPerPage);

        // Set attributes for JSP
        request.setAttribute("subjects", subjectResult.subjects);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("query", query);
        request.setAttribute("categoryId", categoryId);
    }

    /**
     * Inner class to hold subject list results Contains both the list of
     * subjects and the total record count
     */
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
