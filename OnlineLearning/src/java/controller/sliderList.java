/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.SliderDAO; // Importing the SliderDAO for database operations
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Slider; // Importing the Slider model

public class sliderList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SliderDAO sliderDAO = new SliderDAO(); // Create an instance of SliderDAO
        String searchQuery = request.getParameter("searchQuery"); // Retrieve the search query for slider title or backlink
        String statusFilter = request.getParameter("status"); // Retrieve the status filter
        List<Slider> slider; // Declare a list to hold the sliders

        // Check if there is a search query or if the status filter is not set to "All"
        if ((searchQuery != null && !searchQuery.trim().isEmpty()) || (statusFilter != null && !statusFilter.equals("All"))) {
            // Search and filter sliders based on the provided query and status
            slider = sliderDAO.searchAndFilterSliders(searchQuery, statusFilter); 
        } else {
            // Retrieve all sliders if no search or filter is applied
            slider = sliderDAO.getAllSliders();
        }

        // Set the retrieved sliders and filter values as request attributes
        request.setAttribute("slider", slider);
        request.setAttribute("searchQuery", searchQuery); // Keep the search value for the input field
        request.setAttribute("statusFilter", statusFilter); // Keep the current status filter
        request.getRequestDispatcher("sliderList.jsp").forward(request, response); // Forward the request to the JSP page
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sliderIdStr = request.getParameter("sliderID"); // Retrieve the slider ID from the request
        String action = request.getParameter("action"); // Retrieve the action (Show or Hide)

        // Parse the slider ID to an integer
        int sliderId = Integer.parseInt(sliderIdStr);

        SliderDAO sliderDAO = new SliderDAO(); // Create an instance of SliderDAO

        // Update the status of the slider based on the action provided
        if ("Show".equals(action)) {
            sliderDAO.updateSliderStatus(sliderId, "Show"); // Show the slider
        } else if ("Hide".equals(action)) {
            sliderDAO.updateSliderStatus(sliderId, "Hide"); // Hide the slider
        }

        // Redirect back to the slider list after the status update
        response.sendRedirect("sliderList"); // Ensure "sliderList" maps to the servlet displaying the sliders
    }
}
