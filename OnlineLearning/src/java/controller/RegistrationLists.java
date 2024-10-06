package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.RegistrationsDAO;
import dal.UserDAO;
import dal.SubjectDAO;
import dal.PackagePriceDAO;
import model.Registrations;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RegistrationLists extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Initialize DAOs
            RegistrationsDAO registrationsDAO = new RegistrationsDAO();
            UserDAO userDAO = new UserDAO();
            SubjectDAO subjectDAO = new SubjectDAO();
            PackagePriceDAO packageDAO = new PackagePriceDAO();

            // Pagination parameters
            int pageSize = 20; // Number of items per page
            int currentPage = 1; // Default current page

            // Get the current page from the request
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                currentPage = Integer.parseInt(pageParam);
            }

            // Retrieve the total number of registrations for pagination
            int totalRegistrations = registrationsDAO.getTotalRegistrations();
            int totalPages = (int) Math.ceil((double) totalRegistrations / pageSize);

            // Retrieve the list of registrations for the current page
            List<Registrations> listR = registrationsDAO.getRegistrationsByPage(currentPage, pageSize);

            if (listR != null && !listR.isEmpty()) {
                // Initialize maps to hold user, subject, and package information
                Map<Integer, String> customerEmail = new HashMap<>();
                Map<Integer, String> subjectTitle = new HashMap<>();
                Map<Integer, String> packageName = new HashMap<>();
                Map<Integer, String> staffUsername = new HashMap<>();

                // Retrieve additional information for each registration
                for (Registrations registration : listR) {
                    int userId = registration.getUserId();
                    int subjectId = registration.getSubjectId();
                    int packageId = registration.getPackageId();
                    int staffId = registration.getStaffId();

                    // Fetching additional details
                    customerEmail.put(userId, userDAO.getEmailByUserId(userId));
                    subjectTitle.put(subjectId, subjectDAO.getSubjectNameById(subjectId));
                    packageName.put(packageId, packageDAO.getNameByPackageId(packageId));
                    staffUsername.put(staffId, userDAO.getStaffUserNameByUserId(staffId));
                }

                // Set attributes for the request
                request.setAttribute("listR", listR);
                request.setAttribute("customerEmail", customerEmail);
                request.setAttribute("subjectTitle", subjectTitle);
                request.setAttribute("packageName", packageName);
                request.setAttribute("staffUsername", staffUsername);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPages", totalPages); // Set total pages for pagination

                // Forward the request to the JSP page
                request.getRequestDispatcher("RegistrationList.jsp").forward(request, response);
            } else {
                response.getWriter().println("No registrations found.");
            }
        } catch (Exception e) {
            throw new ServletException("Database access error", e);
        }
    }
}
