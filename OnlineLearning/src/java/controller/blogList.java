/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.BlogDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Blog;
import model.BlogCategory;

public class blogList extends HttpServlet {

    private static final int PAGE_SIZE = 8;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BlogDAO blogDAO = new BlogDAO();

        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        String title = request.getParameter("title");
        String categoryIdStr = request.getParameter("categoryID");
        Integer categoryID = null;

        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            try {
                categoryID = Integer.parseInt(categoryIdStr);
            } catch (NumberFormatException e) {
                categoryID = null;
            }
        }

        // Lấy blog dựa trên title và categoryID, bao gồm cả phân trang
        List<Blog> blogs = blogDAO.getBlogsByPage(title, categoryID, page, PAGE_SIZE);

        // Lấy danh sách tất cả các danh mục
        List<BlogCategory> categories = blogDAO.getAllCategories();

        // Tính tổng số blog dựa trên bộ lọc
        int totalBlogs = blogDAO.getTotalBlogs(title, categoryID);
        int totalPages = (int) Math.ceil((double) totalBlogs / PAGE_SIZE);

        // Đặt các thuộc tính cho JSP
        request.setAttribute("blogs", blogs);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchTitle", title);        // Để giữ giá trị tìm kiếm khi phân trang
        request.setAttribute("filterCategory", categoryID);

        request.getRequestDispatcher("BlogList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
