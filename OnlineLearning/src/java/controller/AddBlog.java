/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.BlogDAO;
import model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Blog;
import model.BlogCategory;

public class AddBlog extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Thêm thông tin người dùng vào request để sử dụng trong JSP
        request.setAttribute("user", user);

        // Chuyển hướng đến AddBlog.jsp
        request.getRequestDispatcher("AddBlog.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    Users user = (Users) session.getAttribute("user");

    // Kiểm tra nếu người dùng không đăng nhập
    if (user == null) {
        response.sendRedirect("login.jsp"); // Chuyển đến trang đăng nhập nếu chưa đăng nhập
        return;
    }

    String title = request.getParameter("title");
    String content = request.getParameter("content");
    int categoryId = Integer.parseInt(request.getParameter("categoryId"));

    Blog blog = new Blog();
    blog.setTitle(title);
    blog.setContent(content);
    blog.setUserId(user); // Sử dụng ID của người dùng
    BlogCategory category = new BlogCategory();
    category.setBlogCategoryId(categoryId);
    blog.setBlogCategoryId(category);

    BlogDAO blogDAO = new BlogDAO();
    blogDAO.createBlog(blog); // Gọi hàm để thêm blog

    // Sau khi thêm thành công, chuyển hướng về lại PostDetail.jsp
    response.sendRedirect("PostDetail");
}


}
