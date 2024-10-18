package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.*;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class RegistrationLists extends HttpServlet {

    private final SettingDAO settingDAO = new SettingDAO();
    private final RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    private final UserDAO userDAO = new UserDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final PackagePriceDAO packageDAO = new PackagePriceDAO();
    private final CampaignsDAO campaignDAO = new CampaignsDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        // Check if the user is null
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        RegistrationSetting setting = (RegistrationSetting) session.getAttribute("setting");
        if (setting == null) {
            setting = settingDAO.getRegistrationSettingsByUserID(user.getUserID());
            if (setting != null) {
                session.setAttribute("setting", setting);
            } else {
                setting = new RegistrationSetting(); // Set default values or handle appropriately
            }
        }

        // Get and validate request parameters
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String campaign = request.getParameter("campaign");
        String status = request.getParameter("status");
        String registrationTime = request.getParameter("registrationFrom");
        String validTo = request.getParameter("validTo");

        // Get pagination parameters
        String currentPageParam = request.getParameter("page");
        int pageSize = setting != null ? setting.getNumberOfItems() : 20;
        if (pageSize <= 0) {
            pageSize = 20;
        }

        int currentPage = 1;
        if (currentPageParam != null) {
            try {
                currentPage = Integer.parseInt(currentPageParam);
            } catch (NumberFormatException e) {
                // Handle the error
                currentPage = 1;
            }
        }

        // Fetch registrations with pagination
        List<Registrations> registrations = registrationsDAO.getAllRegistration(
                currentPage, // Pass currentPage instead of pageSize twice
                pageSize,
                email,
                subject,
                campaign,
                parseDate(registrationTime),
                parseDate(validTo),
                status
        );

        // Calculate pagination details
        int totalRegistrations = registrationsDAO.getTotalRegistrationsCount(
                email,
                subject,
                campaign,
                parseDate(registrationTime),
                parseDate(validTo),
                status
        );
        // Ensure totalPages is calculated correctly and is non-negative
        int totalPages = (int) Math.ceil((double) totalRegistrations / pageSize);
        if (totalPages < 1) {
            totalPages = 1;
        }

        // Retrieve additional information for each registration
        Map<Integer, String> customerEmail = new HashMap<>();
        Map<Integer, String> subjectTitle = new HashMap<>();
        Map<Integer, String> packageName = new HashMap<>();
        Map<Integer, String> staffUsername = new HashMap<>();

        for (Registrations registration : registrations) {
            // Fetching additional details
            customerEmail.put(registration.getUserId(), userDAO.getEmailByUserId(registration.getUserId()));
            subjectTitle.put(registration.getSubjectId(), subjectDAO.getSubjectNameById(registration.getSubjectId()));
            packageName.put(registration.getPackageId(), packageDAO.getNameByPackageId(registration.getPackageId()));
            staffUsername.put(registration.getStaffId(), userDAO.getStaffUserNameByUserId(registration.getStaffId()));
        }

        // Get lists for dropdowns
        Map<Integer, Subject> subjects = subjectDAO.getAllSubject();
        Map<Integer, Campaigns> campaigns = campaignDAO.getAllCampaigns();
        request.setAttribute("listR", registrations);
        request.setAttribute("customerEmail", customerEmail);
        request.setAttribute("subjectTitle", subjectTitle);
        request.setAttribute("packageName", packageName);
        request.setAttribute("staffUsername", staffUsername);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("listS", subjects);
        request.setAttribute("listC", campaigns);
        request.getRequestDispatcher("RegistrationList.jsp").forward(request, response);
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            // Log the parse error (consider using a logger)
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
