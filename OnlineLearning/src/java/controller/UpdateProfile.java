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
import model.Users;

/**
 *
 * @author tuant
 */
//annotaion for image upload
@MultipartConfig
public class UpdateProfile extends HttpServlet {
    private static final String UPLOAD_DIR = "images"; // defalt path to save images
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        Part filePart = request.getPart("avatar"); // get image file part
        String fileName = null;
        String filePath = null;
        HttpSession session = request.getSession();
        UserDAO uDAO = new UserDAO();
        Users u1 = (Users) session.getAttribute("user");
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
            Users usernameCheck = uDAO.getUserByInfo("Username", username); // check if username is existed or not
            Users phoneCheck = uDAO.getUserByInfo("Phone", phone); // check if username is existed or not
            if (usernameCheck != null && !u1.getUsername().equals(username)) { // forward an error if existed
                request.setAttribute("invalidUsername", true);
                request.getRequestDispatcher("profile.jsp").forward(request, response);
            } else if (phone != null && !phone.equals(u1.getPhone())) {

                if (phoneCheck != null) {
                    request.setAttribute("invalidPhone", true); // display error if phone is existed
                    request.getRequestDispatcher("profile.jsp").forward(request, response);
                    return;
                }
            }
            // update in db and session
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
