/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PackagePriceDAO;
import dal.CategoryDAO;
import dal.SubjectDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.PackagePrice;
import model.Subject;
import model.SubjectCategory;
import java.sql.*;

/**
 *
 * @author 84336
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/registerCourse")
public class registerCourse extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            SubjectDAO subjectDAO = new SubjectDAO();
            Subject currentSub = subjectDAO.getSubjectById(id);

            PackagePriceDAO packagePriceDAO = new PackagePriceDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            SubjectCategory currentCat = categoryDAO.getCategoryById(currentSub.getSubjectCategoryId());
            List<PackagePrice> currentPackage = packagePriceDAO.searchBySubjectId(id);
            double lowestOption = packagePriceDAO.findLowestPrice(currentPackage);
            List<SubjectCategory> categories = categoryDAO.getAllCategories();
            // Get 3 random Subjects and their lowestPrice
            List<Subject> randomSubs = getRandomItems(subjectDAO.getAllSubjects(), 3);
            if (randomSubs.isEmpty()) {
                randomSubs = subjectDAO.getAllSubjects();
            }

            Map<Subject, Double> subjectPriceMap = new HashMap<>();
            for (Subject randomSub : randomSubs) {
                List<PackagePrice> randomSubPack = packagePriceDAO.searchBySubjectId(randomSub.getSubjectId());
                double lowestPrice = packagePriceDAO.findLowestPrice(randomSubPack);
                subjectPriceMap.put(randomSub, lowestPrice);
            }
            request.setAttribute("categories", categories);
            request.setAttribute("subjectPriceMap", subjectPriceMap);
            request.setAttribute("currentCat", currentCat);
            request.setAttribute("currentSub", currentSub);
            request.setAttribute("currentPackage", currentPackage);
            request.setAttribute("lowestOption", lowestOption);

            request.getRequestDispatcher("course-detail.jsp").forward(request, response);
        }catch (NumberFormatException e) {
            // Handle the case where the ID is not a valid integer
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid subject ID.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
         catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    //Method get random
    public static List<Subject> getRandomItems(List<Subject> list, int count) {
        if (list.size() > count) {
            Collections.shuffle(list);
            return new ArrayList<>(list.subList(0, count));
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
