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
        blogDAO = new BlogDAO(); // Khởi tạo DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Blog> blogs = blogDAO.getAllBlogs();
        request.setAttribute("blogs", blogs);

        // Chuyển hướng tới JSP
        request.getRequestDispatcher("PostDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String blogId = request.getParameter("blogId");
        String currentStatus = request.getParameter("currentStatus");

        // Xác định trạng thái mới
        String newStatus = currentStatus.equals("Show") ? "Hide" : "Show";

        // Gọi hàm cập nhật trạng thái
        blogDAO.updateStatus(blogId, newStatus);

        // Gửi phản hồi về trạng thái mới
        response.getWriter().write(newStatus); 
    }

}
