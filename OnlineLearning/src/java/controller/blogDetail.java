/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.BlogDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Blog;
import dal.SliderDAO;
import java.util.List;

/**
 *
 * @author sonna
 */
public class blogDetail extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String blogId = request.getParameter("blogId");

        BlogDAO blogDAO = new BlogDAO();
        Blog blog = blogDAO.getBlogById(Integer.parseInt(blogId));
        request.setAttribute("blog", blog);
        
        SliderDAO sliderDAO = new SliderDAO();
        List<Blog> latestBlogs = sliderDAO.getLatestBlogs();
        request.setAttribute("latestBlogs", latestBlogs);

        request.getRequestDispatcher("blogDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
}