/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Blog;
import dal.BlogDAO;

public class PostDetail extends HttpServlet {

    private BlogDAO blogDAO;

    @Override
    public void init() throws ServletException {
        blogDAO = new BlogDAO(); // Initialize DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Blog> blogs = blogDAO.getAllBlogs();
        request.setAttribute("blogs", blogs);

        // Forward to JSP
        request.getRequestDispatcher("PostDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String blogId = request.getParameter("blogId");
        String currentStatus = request.getParameter("currentStatus");

        // Determine the new status
        String newStatus = currentStatus.equals("Show") ? "Hide" : "Show";

        // Call the function to update status
        blogDAO.updateStatus(blogId, newStatus);

        // Send the new status as a response
        response.getWriter().write(newStatus); 
    }

}
