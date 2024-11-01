/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.BlogDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import jakarta.servlet.http.HttpSession;
import model.Blog;
import model.BlogCategory;
import java.io.IOException;
import java.util.List;
import model.Users;

public class EditBlog extends HttpServlet {

    private BlogDAO blogDAO = new BlogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         if (!hasPermission(request, response)) return;
        // Giả sử bạn đã có một phương thức để lấy danh sách các danh mục
        List<BlogCategory> categories = blogDAO.getAllCategories();
        request.setAttribute("categories", categories);

        // Giả sử bạn lấy thông tin blog dựa trên blogId
        int blogId = Integer.parseInt(request.getParameter("blogId"));
        Blog blog = blogDAO.getBlogById(blogId);
        request.setAttribute("blog", blog);

        // Chuyển tiếp đến trang JSP
        request.getRequestDispatcher("EditBlog.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int blogId = Integer.parseInt(request.getParameter("blogId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        // Lấy Blog hiện tại từ cơ sở dữ liệu
        Blog existingBlog = blogDAO.getBlogById(blogId); // Giả sử bạn có phương thức này

        // Tạo đối tượng Blog với thông tin mới
        Blog updatedBlog = new Blog(
                existingBlog.getBlogId(), 
                existingBlog.getUserId(), 
                title, 
                content, 
                existingBlog.getCreateAt(), 
                new BlogCategory(categoryId, "") 
        );

        // Cập nhật blog
        blogDAO.updateBlog(updatedBlog);

        // Chuyển hướng trở lại PostDetail.jsp
        response.sendRedirect("PostDetail");
    }
private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());
        String userRole = currentUser.getRole();

        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect("/Homepage");
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true;
    }
}
