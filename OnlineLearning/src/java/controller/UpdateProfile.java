/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import javax.mail.Session;
import model.Users;

/**
 *
 * @author tuant
 */
@MultipartConfig
public class UpdateProfile extends HttpServlet {
    private static final String UPLOAD_DIR = "images";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        Part filePart = request.getPart("avatar"); 
        String fileName = null;
        String filePath = null;
        HttpSession session = request.getSession();
        UserDAO uDAO = new UserDAO();
        Users u1 = (Users) session.getAttribute("user");
        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            File file = new File(uploadDir, fileName);
            filePart.write(file.getAbsolutePath());
            filePath = UPLOAD_DIR + "/" + fileName;
        }
        if (phone != null && phone.isEmpty()) {
            phone = null;
        }
        if (gender != null && gender.equals("none")) {
            gender = null;
        }
        if (address != null && address.isEmpty()) {
            address = null;
        }
        if (filePath == null) {
                filePath = u1.getAvatar();
            }
        
        try {
            Users usernameCheck = uDAO.getUserByInfo("Username", username);
            Users phoneCheck = uDAO.getUserByInfo("Phone", phone);
            if (usernameCheck != null && !u1.getUsername().equals(username)) {
                request.setAttribute("invalidUsername", true);
                request.getRequestDispatcher("profile.jsp").forward(request, response);
            } else if (phone != null && !phone.equals(u1.getPhone())) {

                if (phoneCheck != null) {
                    request.setAttribute("invalidPhone", true);
                    request.getRequestDispatcher("profile.jsp").forward(request, response);
                }
            }
            uDAO.updateProfile(name, username, phone, address, gender, u1.getUserID(), filePath);
            Users u2 = uDAO.getUserByInfo("Email", u1.getEmail());
            session.setAttribute("user", u2);
            request.setAttribute("success", true);
            request.getRequestDispatcher("profile.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
