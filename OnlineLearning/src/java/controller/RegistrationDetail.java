package controller;

import dal.PackagePriceDAO;
import dal.RegistrationsDAO;
import dal.SubjectDAO;
import dal.UserDAO;
import model.Registrations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RegistrationDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve the registration ID from the request parameters
            String registrationIdParam = request.getParameter("id");
            // Check if the registration ID is missing or empty
            if (registrationIdParam == null || registrationIdParam.isEmpty()) {
                // Respond with a bad request error if the ID is not provided
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing registration ID");
                return;
            }

            // Parse the registration ID to an integer
            int registrationId = Integer.parseInt(registrationIdParam);

            // Create DAO instances for database access
            RegistrationsDAO registrationsDAO = new RegistrationsDAO();
            UserDAO userDAO = new UserDAO();
            SubjectDAO subjectDAO = new SubjectDAO();
            PackagePriceDAO packageDAO = new PackagePriceDAO();

            // Retrieve the registration details by ID
            Registrations registration = registrationsDAO.getRegistrationById(registrationId);

            // Check if the registration exists
            if (registration != null) {
                // Fetch additional details related to the registration
                String customerEmail = userDAO.getEmailByUserId(registration.getUserId());
                String subjectTitle = subjectDAO.getSubjectNameById(registration.getSubjectId());
                String packageName = packageDAO.getNameByPackageId(registration.getPackageId());
                String saleUsername = userDAO.getStaffUserNameByUserId(registration.getStaffId());

                // Set attributes to be used in the JSP for displaying registration details
                request.setAttribute("registration", registration);
                request.setAttribute("customerEmail", customerEmail);
                request.setAttribute("subjectTitle", subjectTitle);
                request.setAttribute("packageName", packageName);
                request.setAttribute("saleUsername", saleUsername);

                // Forward the request to the RegistrationDetail.jsp page to display the details
                request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
            } else {
                // If no registration is found, set an error message and forward to the JSP
                request.setAttribute("errorMessage", "Registration not found");
                request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            // Handle case where registration ID is not a valid number
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid registration ID format");
        } catch (Exception e) {
            // Handle general exceptions, such as database access errors
            throw new ServletException("Database access error", e);
        }
    }
}
