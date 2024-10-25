package controller;

import dal.SettingDAO; // Importing the SettingDAO class to manage registration settings in the database.
import java.io.IOException; // Importing IOException for handling input-output exceptions.
import jakarta.servlet.ServletException; // Importing ServletException for servlet-related exceptions.
import jakarta.servlet.annotation.WebServlet; // Importing annotation to define a servlet.
import jakarta.servlet.http.HttpServlet; // Importing HttpServlet as the base class for servlets.
import jakarta.servlet.http.HttpServletRequest; // Importing HttpServletRequest for handling request data.
import jakarta.servlet.http.HttpServletResponse; // Importing HttpServletResponse for handling response data.
import jakarta.servlet.http.HttpSession; // Importing HttpSession for session management.
import model.RegistrationSetting; // Importing the RegistrationSetting model class.
import model.Users; // Importing the Users model class.

@WebServlet("/registrationSetting") // Mapping this servlet to the "/registrationSetting" URL.
public class RegistrationSettings extends HttpServlet {

    // Creating an instance of SettingDAO to interact with the registration settings in the database.
    private final SettingDAO settingDAO = new SettingDAO();

    // Main processing method for both GET and POST requests.
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the current session
        HttpSession session = request.getSession();
        // Get the logged-in user from the session
        Users user = (Users) session.getAttribute("user");
        // Get the registration settings from the session
        RegistrationSetting setting = (RegistrationSetting) session.getAttribute("setting");

        // If there are no existing settings, create a new one for the user
        if (setting == null) {
            setting = new RegistrationSetting();
            setting.setUserID(user.getUserID()); // Set the user ID in the new settings object
            settingDAO.addRegistrationSetting(user.getUserID()); // Add default settings in the database
        }

        // Retrieve form data for registration settings
        String numberOfItemsStr = request.getParameter("numberOfItems");
        // Parse the number of items, defaulting to 20 if not provided
        int numberOfItems = (numberOfItemsStr != null && !numberOfItemsStr.isEmpty()) ? Integer.parseInt(numberOfItemsStr) : 20;

        // Retrieve checkbox values to determine which fields are enabled in the registration settings
        boolean registrationID = request.getParameter("RegistrationID") != null;
        boolean email = request.getParameter("email") != null;
        boolean subject = request.getParameter("subject") != null;
        boolean campaign = request.getParameter("campaign") != null;
        boolean packageID = request.getParameter("package") != null;
        boolean totalCost = request.getParameter("totalCost") != null;
        boolean registrationTime = request.getParameter("registrationTime") != null;
        boolean validFrom = request.getParameter("validFrom") != null;
        boolean validTo = request.getParameter("validTo") != null;
        boolean status = request.getParameter("status") != null;
        boolean staff = request.getParameter("staff") != null;
        boolean note = request.getParameter("note") != null;

        // Update the settings object with the retrieved values
        setting.setNumberOfItems(numberOfItems);
        setting.setRegistrationId(registrationID);
        setting.setEmail(email);
        setting.setSubject(subject);
        setting.setCampaign(campaign);
        setting.setPackageId(packageID);
        setting.setTotalCost(totalCost);
        setting.setRegistrationTime(registrationTime);
        setting.setValidFrom(validFrom);
        setting.setValidTo(validTo);
        setting.setStatus(status);
        setting.setStaff(staff);
        setting.setNote(note);

        // Update the settings in the database
        settingDAO.updateRegistrationSetting(setting);

        // Update the session with the new settings
        session.setAttribute("setting", setting);
        // Redirect the user to the listRegistration page after updating settings
        response.sendRedirect("listRegistration");
    }

    // Handle GET requests by calling processRequest
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Handle POST requests by calling processRequest
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Provide a description of the servlet
    @Override
    public String getServletInfo() {
        return "Handles registration settings"; // Description for the servlet
    }
}
