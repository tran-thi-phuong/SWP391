/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PackagePriceDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import model.PackagePrice;
import model.Users;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns = {"/SubjectDetailPricePackage"})
public class SubjectPricePackage extends HttpServlet {

    // Handles GET requests to display the price packages related to a subject
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the user has permission to access this page
        if (!hasPermission(request, response)) {
            return;
        }

        String subjectId = request.getParameter("id"); // Get subject ID from the request
        request.getSession().setAttribute("subjectID", subjectId); // Store subject ID in the session

        // Retrieve the list of price packages for the given subject ID from the database
        PackagePriceDAO pDAO = new PackagePriceDAO();
        List<PackagePrice> pricePackages = pDAO.searchBySubjectId(Integer.parseInt(subjectId));
        request.setAttribute("pricePackages", pricePackages); // Set the price packages in the request attribute

        // Forward the request to the JSP page for rendering
        request.getRequestDispatcher("/SubjectDetailPricePackage.jsp").forward(request, response);
    }

    // Handles POST requests for adding, updating, or deleting price packages
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the action parameter (add, update, delete) from the request
        String action = request.getParameter("action");
        String subjectId = request.getParameter("subjectId"); // Get subject ID from the request

        try {
            PackagePriceDAO packagePriceDAO = new PackagePriceDAO();

            // Perform actions based on the action parameter
            if ("add".equals(action)) {
                addPackage(request, packagePriceDAO, subjectId); // Add a new price package
            } else if ("update".equals(action)) {
                updatePackage(request, packagePriceDAO, subjectId); // Update an existing price package
            } else if ("delete".equals(action)) {
                deletePackage(request, packagePriceDAO); // Delete a price package
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            // Forward the request to the JSP page to display the error message
            request.getRequestDispatcher("/SubjectDetailPricePackage.jsp").forward(request, response);
        }

        // Redirect to the price package page after processing the request
        response.sendRedirect("SubjectDetailPricePackage.jsp");
    }

    // Adds a new price package for the subject
    private void addPackage(HttpServletRequest request, PackagePriceDAO packagePriceDAO, String subjectId) {
        // Create a new PackagePrice object and set its properties from the request parameters
        PackagePrice packagePrice = new PackagePrice();
        packagePrice.setSubjectId(Integer.parseInt(subjectId)); // Set the subject ID
        packagePrice.setName(request.getParameter("packageName")); // Set the package name
        packagePrice.setDurationTime(Integer.parseInt(request.getParameter("durationTime"))); // Set the duration time
        packagePrice.setSalePrice(Double.parseDouble(request.getParameter("price"))); // Set the sale price
        packagePrice.setPrice(Double.parseDouble(request.getParameter("price"))); // Set the price

        // Add the new price package to the database
        packagePriceDAO.addPackagePrice(packagePrice);
    }

    // Updates an existing price package for the subject
    private void updatePackage(HttpServletRequest request, PackagePriceDAO packagePriceDAO, String subjectId) {
        // Create a new PackagePrice object and set its properties from the request parameters
        PackagePrice packagePrice = new PackagePrice();
        packagePrice.setPackageId(Integer.parseInt(request.getParameter("id"))); // Set the package ID
        packagePrice.setSubjectId(Integer.parseInt(subjectId)); // Set the subject ID
        packagePrice.setName(request.getParameter("name")); // Set the name of the price package
        packagePrice.setDurationTime(Integer.parseInt(request.getParameter("durationTime"))); // Set the duration time
        packagePrice.setSalePrice(Double.parseDouble(request.getParameter("price"))); // Set the sale price
        packagePrice.setPrice(Double.parseDouble(request.getParameter("price"))); // Set the price

        // Update the price package in the database
        packagePriceDAO.updatePackagePrice(packagePrice);
    }

    // Deletes a price package from the database
    private void deletePackage(HttpServletRequest request, PackagePriceDAO packagePriceDAO) {
        // Get the package price ID from the request
        int packagePriceId = Integer.parseInt(request.getParameter("id"));
        // Delete the price package from the database
        packagePriceDAO.deletePackagePrice(packagePriceId);
    }

    // Checks if the user has permission to access this page
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user"); // Get the current user from the session

        // If the user is not logged in, redirect to the login page
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString()); // Get the page ID from the URL
        String userRole = currentUser.getRole(); // Get the user's role

        // Check if the user has permission to access this page
        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage"); // Redirect to homepage if no permission
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp"); // Redirect to error page if no page ID
            return false;
        }

        return true; // User has permission to access the page
    }
}
