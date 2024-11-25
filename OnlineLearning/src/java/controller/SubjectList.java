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
import dal.Customer_SubjectDAO;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SubjectList", urlPatterns = {"/SubjectList"})
public class SubjectList extends HttpServlet {

    private SubjectDAO subjectDAO;
    private CategoryDAO categoryDAO;
    private UserDAO userDAO;
    private LessonDAO lessonDAO;
    private Customer_SubjectDAO csubjectDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        subjectDAO = new SubjectDAO();
        categoryDAO = new CategoryDAO();
        lessonDAO = new LessonDAO();
        csubjectDAO = new Customer_SubjectDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }
        try {
            HttpSession session = request.getSession();
            Users currentUser = (Users) session.getAttribute("user");
            if (currentUser == null) {
                response.sendRedirect("Homepage.jsp");
                return;
            }

            // Get pagination parameters and search/filter criteria
            int page = getPageNumber(request);
            int recordsPerPage = 6;
            String query = request.getParameter("search");
            String categoryId = request.getParameter("category");
            String status = request.getParameter("status");

            // Pass the current user's role and ID to the getSubjects method
            SubjectListResult subjectResult = getSubjects(query, categoryId, status, page, recordsPerPage, currentUser);

            List<SubjectCategory> categories = categoryDAO.getAllCategories();
            // Calculate average progress for each subject
            Map<Integer, Double> averageProgressMap = new HashMap<>();
            for (Subject subject : subjectResult.subjects) {
                double avgProgress = csubjectDAO.calculateAverageProgress(subject.getSubjectID());
                averageProgressMap.put(subject.getSubjectID(), avgProgress);
            }
            request.setAttribute("averageProgressMap", averageProgressMap);

            Map<Integer, Integer> lessonCounts = new HashMap<>();
            for (Subject subject : subjectResult.subjects) {
                int lessonCount = lessonDAO.countLessonsBySubjectId(subject.getSubjectID());
                lessonCounts.put(subject.getSubjectID(), lessonCount);
            }
            request.setAttribute("lessonCounts", lessonCounts);

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

    private SubjectListResult getSubjects(String query, String categoryId, String status,
            int page, int recordsPerPage, Users currentUser) throws SQLException {
        int offset = (page - 1) * recordsPerPage;
        List<Subject> subjects;
        int totalRecords;

        // Check user role and modify query accordingly
        boolean isAdmin = "Admin".equals(currentUser.getRole());
        Integer ownerId = isAdmin ? null : currentUser.getUserID();

        if ((query != null && !query.trim().isEmpty())
                || (categoryId != null && !categoryId.trim().isEmpty())
                || (status != null && !status.trim().isEmpty())) {
            // Search with filters and role-based restrictions
            subjects = subjectDAO.searchSubjectsWithFilters(query, categoryId, status, offset, recordsPerPage, ownerId);
            totalRecords = subjectDAO.getTotalSubjectsWithFilters(query, categoryId, status, ownerId);
        } else {
            // Get all subjects with role-based restrictions
            subjects = subjectDAO.getAllSubjects(offset, recordsPerPage, ownerId);
            totalRecords = subjectDAO.getTotalSubjects(ownerId);
        }

        return new SubjectListResult(subjects, totalRecords);
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
    }
     private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        String userRole = currentUser.getRole();
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

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
