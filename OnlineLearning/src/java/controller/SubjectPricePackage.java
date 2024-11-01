/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PackagePriceDAO;
import dal.PagesDAO;
import dal.RolePermissionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import model.PackagePrice;
import model.Users;


/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns = {"/SubjectDetailPricePackage"})
public class SubjectPricePackage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }
        String subjectId = request.getParameter("id");
        request.getSession().setAttribute("subjectID", subjectId);
        PackagePriceDAO pDAO = new PackagePriceDAO();
        List<PackagePrice> pp = pDAO.searchBySubjectId(Integer.parseInt(subjectId));
        request.setAttribute("pricePackages", pp);

        // Chuyển tiếp đến JSP
        request.getRequestDispatcher("/SubjectDetailPricePackage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ request
        String action = request.getParameter("action");
        String subjectId = request.getParameter("subjectId");

        try {
            PackagePriceDAO packagePriceDAO = new PackagePriceDAO();

            if ("add".equals(action)) {
                addPackage(request, packagePriceDAO, subjectId);
            } else if ("update".equals(action)) {
                updatePackage(request, packagePriceDAO, subjectId);
            } else if ("delete".equals(action)) {
                deletePackage(request, packagePriceDAO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            request.getRequestDispatcher("/SubjectDetailPricePackage.jsp").forward(request, response);
        }

        response.sendRedirect("SubjectDetailPricePackage.jsp");
    }

    private void addPackage(HttpServletRequest request, PackagePriceDAO packagePriceDAO, String subjectId) {
        PackagePrice packagePrice = new PackagePrice();
        packagePrice.setSubjectId(Integer.parseInt(subjectId));
        packagePrice.setName(request.getParameter("packageName"));
        packagePrice.setDurationTime(Integer.parseInt(request.getParameter("durationTime")));
        packagePrice.setSalePrice(Double.parseDouble(request.getParameter("salePrice")));
        packagePrice.setPrice(Double.parseDouble(request.getParameter("price")));

        packagePriceDAO.addPackagePrice(packagePrice);
    }

    private void updatePackage(HttpServletRequest request, PackagePriceDAO packagePriceDAO, String subjectId) {
        PackagePrice packagePrice = new PackagePrice();
        packagePrice.setPackageId(Integer.parseInt(request.getParameter("id")));
        packagePrice.setSubjectId(Integer.parseInt(subjectId));
        packagePrice.setName(request.getParameter("name"));
        packagePrice.setDurationTime(Integer.parseInt(request.getParameter("durationTime")));
        packagePrice.setSalePrice(Double.parseDouble(request.getParameter("salePrice")));
        packagePrice.setPrice(Double.parseDouble(request.getParameter("price")));

        packagePriceDAO.updatePackagePrice(packagePrice);
    }

    private void deletePackage(HttpServletRequest request, PackagePriceDAO packagePriceDAO) {
        int packagePriceId = Integer.parseInt(request.getParameter("id"));
        packagePriceDAO.deletePackagePrice(packagePriceId);
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
