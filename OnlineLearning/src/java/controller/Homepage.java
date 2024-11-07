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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Blog;
import model.Slider;
import model.Subject;

public class Homepage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Create an instance of SliderDAO to interact with the database
        SliderDAO sliderDAO = new SliderDAO();
        try {
            sliderDAO.autoCreateSliders();
        } catch (SQLException ex) {
            Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Retrieve all sliders from the database
        List<Slider> sliders = sliderDAO.getAllSliders();
        
        // Retrieve the latest 5 blogs from the database
        List<Blog> latestBlogs = sliderDAO.getLatestBlogs(5);

        // Set the lists of latest blogs and sliders as request attributes for use in the JSP
        request.setAttribute("latestBlogs", latestBlogs);
        request.setAttribute("sliders", sliders);
        
        try {
            // Retrieve the top 5 subjects from the database
            List<Subject> topSubjects = sliderDAO.getTopSubjects(5);
            // Set the list of top subjects as a request attribute
            request.setAttribute("topSubjects", topSubjects);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }

        // Forward the request to the homepage.jsp for rendering
        request.getRequestDispatcher("Homepage.jsp").forward(request, response);
    }
}
