/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;
import java.sql.Date;
import dal.SubjectDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.List;
import model.SubjectCategoryCount;

/**
 *
 * @author tuant
 */
public class CourseStat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        LocalDate endDateLocal = LocalDate.now();
        LocalDate startDateLocal = endDateLocal.minus(7, ChronoUnit.DAYS);
        Date endDate = Date.valueOf(endDateLocal);
        Date startDate = Date.valueOf(startDateLocal);
        SubjectDAO sDAO = new SubjectDAO();
        int totalCourse = sDAO.getTotalSubjects();
        int totalActiveCourse = sDAO.getTotalActiveSubjects();
        int newCourse = sDAO.getNewSubjectByTime(startDate, endDate);
        List<SubjectCategoryCount> subjectAllocation = sDAO.getSubjectAllocation();
        request.setAttribute("inactiveCourse", totalCourse - totalActiveCourse);
        request.setAttribute("newCourse", newCourse);
        request.setAttribute("totalCourse", totalCourse);
        request.setAttribute("subjectAllocation", subjectAllocation);
        request.setAttribute("action", "courseStat");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
