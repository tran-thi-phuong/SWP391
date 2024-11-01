package controller;

import dal.PagesDAO; 
import dal.RolePermissionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import model.Users;

@WebServlet("/deletePage")
public class DeletePage extends HttpServlet {
    private PagesDAO pagesDAO;

    @Override
    public void init() throws ServletException {
        pagesDAO = new PagesDAO();
    }
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
    
        String pageIdParam = request.getParameter("pageId");
        
        if (pageIdParam != null) {
            try {
                int pageId = Integer.parseInt(pageIdParam);
                boolean isDeleted = pagesDAO.deletePage(pageId);
                
                if (isDeleted) {
                    request.setAttribute("message", "Page deleted successfully!");
                } else {
                    request.setAttribute("message", "Failed to delete page. It may not exist.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("message", "Invalid page ID.");
                e.printStackTrace(); // Logging the error
            }
        } else {
            request.setAttribute("message", "Page ID is required.");
        }

        // Forward to RolePermissionServlet
        // Ensure that this servlet is properly set up to handle the message attribute
        response.sendRedirect("RolePermission");

    
        
}
    
}
