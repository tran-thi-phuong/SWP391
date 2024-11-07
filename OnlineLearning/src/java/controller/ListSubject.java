/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import dal.SubjectDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Subject;
import model.SubjectCategory;

@WebServlet("/CourseList")
public class ListSubject extends HttpServlet {

    // DAO objects for database interaction
    private SubjectDAO subjectDAO;
    private CategoryDAO categoryDAO;

    // Initialize DAO objects when servlet is created
    @Override
    public void init() throws ServletException {
        super.init();
        subjectDAO = new SubjectDAO();
        categoryDAO = new CategoryDAO();
    }

    // Handle GET request to display list of subjects
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get pagination and search information from request
            int page = getPageNumber(request);
            int recordsPerPage = 6; // Number of subjects displayed per page
            String query = request.getParameter("query"); // Search keyword
            String categoryId = request.getParameter("category"); // Category ID for filtering

            // Get list of subjects based on conditions
            SubjectListResult subjectResult = getSubjects(query, categoryId, page, recordsPerPage);
            // Get all subject categories
            List<SubjectCategory> categories = categoryDAO.getAllCategories();

            // Set attributes for request
            setRequestAttributes(request, subjectResult, categories, page, recordsPerPage, query, categoryId);

            // Forward to display page
            request.getRequestDispatcher("CourseList.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    // Get page number from request parameter
    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        return (pageParam != null) ? Integer.parseInt(pageParam) : 1;
    }

    // Get list of subjects based on search criteria
    private SubjectListResult getSubjects(String query, String categoryId, int page, int recordsPerPage) throws SQLException {
        int offset = (page - 1) * recordsPerPage;

        // If search keyword exists
        if (query != null && !query.trim().isEmpty()) {
            return new SubjectListResult(
                    subjectDAO.searchActiveSubjects(query, offset, recordsPerPage),
                    subjectDAO.getTotalActiveSearchSubjects(query)
            );
        } // If filtering by category
        else if (categoryId != null && !categoryId.trim().isEmpty()) {
            int catId = Integer.parseInt(categoryId);
            return new SubjectListResult(
                    subjectDAO.getActiveSubjectsByCategory(catId, offset, recordsPerPage),
                    subjectDAO.getTotalActiveSubjectsByCategory(catId)
            );
        } // Get all subjects if no conditions
        else {
            return new SubjectListResult(
                    subjectDAO.getAllActiveSubjects(offset, recordsPerPage),
                    subjectDAO.getTotalActiveSubjects()
            );
        }
    }

    // Set request attributes for JSP display
    private void setRequestAttributes(HttpServletRequest request, SubjectListResult subjectResult,
            List<SubjectCategory> categories, int currentPage, int recordsPerPage, String query, String categoryId) {
        // Calculate total pages
        int totalPages = (int) Math.ceil(subjectResult.totalRecords * 1.0 / recordsPerPage);

        // Set attributes in request
        request.setAttribute("subjects", subjectResult.subjects);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("query", query);
        request.setAttribute("categoryId", categoryId);
    }

    // Inner class to hold subject list query results
    private static class SubjectListResult {

        List<Subject> subjects; // List of subjects
        int totalRecords; // Total number of records

        SubjectListResult(List<Subject> subjects, int totalRecords) {
            this.subjects = subjects;
            this.totalRecords = totalRecords;
        }
    }
}
