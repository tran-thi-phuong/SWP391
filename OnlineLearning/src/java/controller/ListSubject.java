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

    private SubjectDAO subjectDAO;
    private CategoryDAO categoryDAO;

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
            int page = getPageNumber(request);
            int recordsPerPage = 6;
            String query = request.getParameter("query");
            String categoryId = request.getParameter("category");
            SubjectListResult subjectResult = getSubjects(query,categoryId, page, recordsPerPage);
            List<SubjectCategory> categories = categoryDAO.getAllCategories();
            setRequestAttributes(request, subjectResult, categories, page, recordsPerPage, query, categoryId);
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
    }
}
