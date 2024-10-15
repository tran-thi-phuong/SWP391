/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.SliderDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import model.Blog;
import model.Slider;
import model.Subject;

public class Homepage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SliderDAO sliderDAO = new SliderDAO();
        List<Slider> sliders = sliderDAO.getAllSliders();
        List<Blog> latestBlogs = sliderDAO.getLatestBlogs(5);

        // Đẩy danh sách blog mới nhất sang trang homepage.jsp
        request.setAttribute("latestBlogs", latestBlogs);
        request.setAttribute("sliders", sliders);
        
        try {
        List<Subject> topSubjects = sliderDAO.getTopSubjects(5);
        request.setAttribute("topSubjects", topSubjects);
    } catch (SQLException e) {
        e.printStackTrace();
    }

        // Điều hướng đến trang homepage.jsp
        request.getRequestDispatcher("Homepage.jsp").forward(request, response);
    }
}