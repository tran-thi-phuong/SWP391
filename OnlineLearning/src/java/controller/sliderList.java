/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.SliderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import model.Slider;

public class sliderList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SliderDAO sliderDAO = new SliderDAO();
        String searchQuery = request.getParameter("searchQuery"); // Tìm kiếm tiêu đề hoặc backlink
        String statusFilter = request.getParameter("status");
        List<Slider> slider;
        if ((searchQuery != null && !searchQuery.trim().isEmpty()) || (statusFilter != null && !statusFilter.equals("All"))) {
        slider = sliderDAO.searchAndFilterSliders(searchQuery, statusFilter); // Phương thức xử lý tìm kiếm và lọc
    } else {
        slider = sliderDAO.getAllSliders(); // Lấy toàn bộ slider nếu không có bộ lọc và tìm kiếm
    }

        request.setAttribute("slider", slider);
        request.setAttribute("searchQuery", searchQuery); // Để giữ lại giá trị tìm kiếm trong ô input
        request.setAttribute("statusFilter", statusFilter);
        request.getRequestDispatcher("sliderList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sliderIdStr = request.getParameter("sliderID");
        String action = request.getParameter("action");

        // Parse the slider ID
        int sliderId = Integer.parseInt(sliderIdStr);

        SliderDAO sliderDAO = new SliderDAO();

        // Update the status based on the action
        if ("Show".equals(action)) {
            sliderDAO.updateSliderStatus(sliderId, "Show");
        } else if ("Hide".equals(action)) {
            sliderDAO.updateSliderStatus(sliderId, "Hide");
        }

        // Redirect back to the slider list after the status update
        response.sendRedirect("sliderList"); // Ensure "sliderList" maps to the servlet displaying the sliders
    }
}
