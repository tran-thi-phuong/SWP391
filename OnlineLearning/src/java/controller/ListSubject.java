package controller;

import dal.CategoryDAO;
import dal.SubjectDAO;
import dal.Customer_SubjectDAO; // Include DAO for counting users
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Subject;
import model.SubjectCategory;

@WebServlet("/CourseList")
public class ListSubject extends HttpServlet {

    private SubjectDAO subjectDAO;
    private CategoryDAO categoryDAO;
    private Customer_SubjectDAO courseDAO; // Added CourseDAO for user counting

    @Override
    public void init() throws ServletException {
        super.init();
        subjectDAO = new SubjectDAO();
        categoryDAO = new CategoryDAO();
        courseDAO = new Customer_SubjectDAO(); // Initialize the CourseDAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = getPageNumber(request);
            int recordsPerPage = 6;
            String query = request.getParameter("query");
            String categoryId = request.getParameter("category");

            // Get list of subjects
            SubjectListResult subjectResult = getSubjects(query, categoryId, page, recordsPerPage);

            // Get all categories
            List<SubjectCategory> categories = categoryDAO.getAllCategories();

            // Count the number of users for each subject
            Map<Integer, Integer> userCounts = getUserCounts(subjectResult.subjects);

            // Set attributes for request
            setRequestAttributes(request, subjectResult, categories, userCounts, page, recordsPerPage, query, categoryId);

            // Forward to the JSP page
            request.getRequestDispatcher("CourseList.jsp").forward(request, response);
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
                    subjectDAO.searchActiveSubjects(query, offset, recordsPerPage),
                    subjectDAO.getTotalActiveSearchSubjects(query)
            );
        } else if (categoryId != null && !categoryId.trim().isEmpty()) {
            int catId = Integer.parseInt(categoryId);
            return new SubjectListResult(
                    subjectDAO.getActiveSubjectsByCategory(catId, offset, recordsPerPage),
                    subjectDAO.getTotalActiveSubjectsByCategory(catId)
            );
        } else {
            return new SubjectListResult(
                    subjectDAO.getAllActiveSubjects(offset, recordsPerPage),
                    subjectDAO.getTotalActiveSubjects()
            );
        }
    }

    private Map<Integer, Integer> getUserCounts(List<Subject> subjects) throws SQLException {
        Map<Integer, Integer> userCounts = new HashMap<>();
        for (Subject subject : subjects) {
            int count = courseDAO.countUsersByCourseID(subject.getSubjectID()); // Count users for each subject
            userCounts.put(subject.getSubjectID(), count);
        }
        return userCounts;
    }

    private void setRequestAttributes(HttpServletRequest request, SubjectListResult subjectResult,
            List<SubjectCategory> categories, Map<Integer, Integer> userCounts, int currentPage, 
            int recordsPerPage, String query, String categoryId) {
        int totalPages = (int) Math.ceil(subjectResult.totalRecords * 1.0 / recordsPerPage);

        request.setAttribute("subjects", subjectResult.subjects);
        request.setAttribute("categories", categories);
        request.setAttribute("userCounts", userCounts); // Add userCounts to request attributes
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
}
