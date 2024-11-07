/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.SliderDAO;
import model.Slider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 *
 * @author sonna
 */
@WebServlet(name = "AddSlider", urlPatterns = {"/AddSlider"})
@MultipartConfig
public class AddSlider extends HttpServlet {
    private static final String UPLOAD_DIR = "images"; 
    private SliderDAO sliderDAO = new SliderDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to AddSlider.jsp to fill in the slider details
        request.getRequestDispatcher("AddSlider.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String title = request.getParameter("title");
        String backlink = request.getParameter("backlink");
        String status = request.getParameter("status");
        Part filePart = request.getPart("image"); // get image file part
        String fileName = null;
        String filePath = null;
        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // get file name
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR; //define save path
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // create folder for saving
            }
            File file = new File(uploadDir, fileName);
            filePart.write(file.getAbsolutePath()); // save the image
            filePath = UPLOAD_DIR + "/" + fileName; // new file path
        }

        // Tạo đối tượng Slider mới và thiết lập các thuộc tính
        Slider newSlider = new Slider();
        newSlider.setTitle(title);
        newSlider.setImage(filePath);
        newSlider.setBacklink(backlink);
        newSlider.setStatus(status);

        // Thêm slider vào cơ sở dữ liệu thông qua DAO
        boolean success = sliderDAO.addSlider(newSlider);

        if (success) {
            // Nếu thành công, chuyển hướng về trang sliderList.jsp
            response.sendRedirect("sliderList");
        } else {
            // Nếu không thành công, bạn có thể hiển thị thông báo lỗi (tuỳ chọn)
            request.setAttribute("error", "Failed to add slider.");
            request.getRequestDispatcher("AddSlider.jsp").forward(request, response);
        }
    }
}
