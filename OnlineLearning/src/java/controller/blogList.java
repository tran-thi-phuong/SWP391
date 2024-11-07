/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.BlogDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Blog;
import model.BlogCategory;

/**
 *
 * @author Admin
 */
@WebServlet( urlPatterns={"/blogList"})
public class blogList extends HttpServlet {
   
    private static final int PAGE_SIZE = 8; // Defining the number of blogs to display per page

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BlogDAO blogDAO = new BlogDAO(); // Creating an instance of BlogDAO to access blog data

        int page = 1; // Initialize the current page to 1
        String pageStr = request.getParameter("page"); // Get the page number from request parameters
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr); // Parse the page number
            } catch (NumberFormatException e) {
                page = 1; // If parsing fails, reset to page 1
            }
        }

        // Get the title and categoryID from request parameters for filtering
        String title = request.getParameter("title");
        String categoryIdStr = request.getParameter("categoryID");
        Integer categoryID = null;

        // Parse the categoryID if present
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                categoryID = Integer.parseInt(categoryIdStr); // Convert categoryID to Integer
            } catch (NumberFormatException e) {
                categoryID = null; // If parsing fails, set categoryID to null
            }
        }

        // Retrieve the list of blogs based on title, categoryID, and pagination
        List<Blog> blogs = blogDAO.getBlogsByPage(title, categoryID, page, PAGE_SIZE);

        // Get the list of all blog categories
        List<BlogCategory> categories = blogDAO.getAllCategories();

        // Calculate the total number of blogs based on filters
        int totalBlogs = blogDAO.getTotalBlogs(title, categoryID);
        int totalPages = (int) Math.ceil((double) totalBlogs / PAGE_SIZE); // Calculate total pages for pagination

        // Set attributes to be accessed in the JSP
        request.setAttribute("blogs", blogs); // List of blogs
        request.setAttribute("categories", categories); // List of blog categories
        request.setAttribute("currentPage", page); // Current page number
        request.setAttribute("totalPages", totalPages); // Total number of pages
        request.setAttribute("searchTitle", title); // Preserve the search title during pagination
        request.setAttribute("filterCategory", categoryID); // Preserve the selected category during pagination

        // Forward the request to BlogList.jsp to display the results
        request.getRequestDispatcher("BlogList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // For POST requests, delegate to doGet method
    }
}
