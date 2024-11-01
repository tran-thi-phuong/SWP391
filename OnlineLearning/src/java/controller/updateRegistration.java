package controller;

import dal.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import model.*;

public class updateRegistration extends HttpServlet { // Class names should start with an uppercase letter

    private static final long serialVersionUID = 1L;
    private final RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    private final PackagePriceDAO packageDAO = new PackagePriceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         if (!hasPermission(request, response)) return;
        // Retrieve registration ID from request
        String registrationID = request.getParameter("registrationId");

        // Validate registration ID
        if (registrationID == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing registration ID");
            return;
        }

        int registrationId;
        try {
            registrationId = Integer.parseInt(registrationID);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid registration ID");
            return;
        }

        Registrations registration = registrationsDAO.getRegistrationById(registrationId);

        if (registration != null) {
            request.setAttribute("registration", registration);
            request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registration not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        // Check if user is logged in
        if (user == null) {
            request.setAttribute("errorMessage", "User not found in session.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        Integer staffId = user.getUserID();
        if (staffId == null) {
            request.setAttribute("errorMessage", "Staff ID is null.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Retrieve and validate registration ID
        String registrationIdStr = request.getParameter("registrationId");
        int registrationId;
        try {
            registrationId = Integer.parseInt(registrationIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid registration ID.");
            request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
            return;
        }

        String newStatus = request.getParameter("status");
        String note = request.getParameter("note");

        // Retrieve the current status of the registration using the integer ID
        Registrations currentRegistration = registrationsDAO.getRegistrationById(registrationId);
        String currentStatus = currentRegistration.getStatus();
        Date validFrom = (Date) currentRegistration.getValidFrom(); // Get the existing validFrom value
        Date validTo = null;

        // Set validity dates based on status change
        if ("Processing".equals(currentStatus) && "Active".equals(newStatus)) {
            int packageId = registrationsDAO.getPackageIdByRegistrationId(registrationId);
            LocalDate currentDate = LocalDate.now();
            validFrom = Date.valueOf(currentDate); // Set validFrom to current date

            int durationDays = packageDAO.getDurationByPackageId(packageId);
            validTo = Date.valueOf(currentDate.plusDays(durationDays)); // Set validTo to current date + duration
        } else if ("Active".equals(currentStatus) && "Inactive".equals(newStatus)) {
            // Do not change validFrom
            validTo = Date.valueOf(LocalDate.now()); // Set validTo to current date
        } else {
            // If the status is not changing from Processing to Active or Active to Inactive, keep validFrom and validTo as they are.
            validTo = (Date) currentRegistration.getValidTo(); // Keep the existing validTo if not updating
        }

        // Create and populate the Registrations object
        Registrations registrationToUpdate = new Registrations();
        registrationToUpdate.setRegistrationId(registrationId); // Set RegistrationID
        registrationToUpdate.setStatus(newStatus);
        registrationToUpdate.setValidFrom(validFrom); // Set validFrom (will retain its value if not changed)
        registrationToUpdate.setValidTo(validTo);
        registrationToUpdate.setNote(note); // Set the note
        registrationToUpdate.setStaffId(staffId); // Set the staff ID

        // Update the registration in the database
        boolean isUpdated = registrationsDAO.updateRegistration(registrationToUpdate);

        // Handle update result
        if (isUpdated) {
            // Store success message in session
            request.getSession().setAttribute("success", "Registration updated successfully!");
            // Redirect to registration list page
            response.sendRedirect(request.getContextPath() + "/listRegistration");
        } else {
            request.setAttribute("error", "Failed to update registration.");
            request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
        }
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
