package controller;

import dal.SettingDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.RegistrationSetting;
import model.Users;

@WebServlet("/registrationSetting")
public class RegistrationSettings extends HttpServlet {
   
    private final SettingDAO settingDAO = new SettingDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        RegistrationSetting setting = (RegistrationSetting) session.getAttribute("setting");

        if (setting == null) {
            setting = new RegistrationSetting();
            setting.setUserID(user.getUserID());
            settingDAO.addRegistrationSetting(user.getUserID());  // Pass the setting object, not just userID
        }

        // Retrieve form data
        String numberOfItemsStr = request.getParameter("numberOfItems");
        int numberOfItems = (numberOfItemsStr != null && !numberOfItemsStr.isEmpty()) ? Integer.parseInt(numberOfItemsStr) : 20;

        // Retrieve checkbox values
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

        // Update settings object
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

        settingDAO.updateRegistrationSetting(setting);

        // Update the session
        session.setAttribute("setting", setting);
        response.sendRedirect("listRegistration");
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

    @Override
    public String getServletInfo() {
        return "Handles registration settings";
    }
}
