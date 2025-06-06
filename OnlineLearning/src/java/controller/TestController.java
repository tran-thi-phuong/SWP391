/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PagesDAO;
import dal.RolePermissionDAO;
//database access
import dal.SettingDAO;
import dal.TestDAO;

//default servlet
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//data structure
import java.util.List;

//model
import model.SystemSetting;
import model.Test;
import model.Users;

/**
 *
 * @author 84336
 */
@WebServlet(name = "TestController", urlPatterns = {"/QuizList"})
public class TestController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    TestDAO testDAO = new TestDAO();
    SettingDAO settingDAO = new SettingDAO();

protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
     if (!hasPermission(request, response)) {
            return;
        }
     
    

    //check user login
    HttpSession session = request.getSession();
    Users user = (Users) session.getAttribute("user");

    // Check if user is null, if so, redirect to login.jsp
    if (user == null) {
        response.sendRedirect("login.jsp");
        return; // Stop further processing
    }
    //get setting
    SystemSetting setting = (SystemSetting) session.getAttribute("setting");
    if (setting == null) {
        // Create a new setting if none exists
        setting = settingDAO.getSettingsByUserID(user.getUserID());
        if (setting != null) {
            session.setAttribute("setting", setting);
        }
    }
    //handle querry
    String search = request.getParameter("search");
    String currentPage = request.getParameter("page");
    // default is 1
    if (currentPage == null) {
        currentPage = "1";
    }
    String subjectId = request.getParameter("subjectId");
    String type = request.getParameter("quizType");

    int pageNumber = (currentPage != null) ? Integer.parseInt(currentPage) : 1;
    int pageSize = (setting != null) ? setting.getNumberOfItems() : 10;

    List<Test> tests = testDAO.getAllTests(pageNumber, pageSize, search, subjectId, type);
    int totalTests = testDAO.getTotalTestCount(search, subjectId, type);

    int totalPages = (int) Math.ceil((double) totalTests / pageSize);

    request.setAttribute("totalPages", totalPages);
    request.setAttribute("testList", tests);
    request.setAttribute("currentPage", currentPage);
    
    // Forward to the JSP
    request.getRequestDispatcher("QuizList.jsp").forward(request, response);
}


// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
  private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession();
    Users currentUser = (Users) session.getAttribute("user");

    // Kiểm tra nếu người dùng chưa đăng nhập thì chuyển hướng đến trang đăng nhập
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return false;
    }

    // Lấy quyền của người dùng và kiểm tra quyền truy cập với trang hiện tại
    String userRole = currentUser.getRole();
    RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
    Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

    // Nếu người dùng đã đăng nhập nhưng không có quyền, chuyển hướng về /homePage
    if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
       response.sendRedirect(request.getContextPath() + "/Homepage");

        return false;
    } else if (pageID == null) {
        // Nếu không tìm thấy trang trong hệ thống phân quyền, chuyển đến trang lỗi
        response.sendRedirect("error.jsp");
        return false;
    }

    return true; // Người dùng có quyền truy cập trang này
}
}
