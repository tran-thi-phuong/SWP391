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

    // DAO instances for accessing the database
    private final SettingDAO settingDAO = new SettingDAO();
    private final RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    private final UserDAO userDAO = new UserDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final PackagePriceDAO packageDAO = new PackagePriceDAO();
    private final CampaignsDAO campaignDAO = new CampaignsDAO();

    // Process the request for both GET and POST methods
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         if (!hasPermission(request, response)) {
            return;
        }
        // Get the current session
        HttpSession session = request.getSession();
        // Retrieve the user object from the session
        Users user = (Users) session.getAttribute("user");

        // Attempt to get registration settings for the user from the session
        RegistrationSetting setting = (RegistrationSetting) session.getAttribute("setting");
        if (setting == null) {
            // If not found in session, retrieve from the database
            setting = settingDAO.getRegistrationSettingsByUserID(user.getUserID());
            // If settings exist, store them in the session
            if (setting != null) {
                session.setAttribute("setting", setting);
            } else {
                // If no settings found, create a new default setting
                setting = new RegistrationSetting(); // Set default values or handle appropriately
            }
        }

        // Get and validate request parameters for filtering registrations
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String campaign = request.getParameter("campaign");
        String status = request.getParameter("status");
        String registrationTime = request.getParameter("registrationFrom");
        String validTo = request.getParameter("validTo");

        // Get pagination parameters from request
        String currentPageParam = request.getParameter("page");
        int pageSize = setting != null ? setting.getNumberOfItems() : 20; // Default page size if not set
        if (pageSize <= 0) {
            pageSize = 20; // Ensure page size is positive
        }

        // Parse the current page number
        int currentPage = 1; // Default to the first page
        if (currentPageParam != null) {
            try {
                currentPage = Integer.parseInt(currentPageParam);
            } catch (NumberFormatException e) {
                // If parsing fails, stay on the first page
                currentPage = 1;
            }
        }

        // Fetch registrations with pagination and filtering
        List<Registrations> registrations = registrationsDAO.filterRegistration(
                currentPage, // Pass currentPage instead of pageSize twice
                pageSize,
                email,
                subject,
                campaign,
                parseDate(registrationTime), // Convert string to Date
                parseDate(validTo), // Convert string to Date
                status
        );

        // Calculate total number of registrations for pagination
        int totalRegistrations = registrationsDAO.getTotalRegistrationsCount(
                email,
                subject,
                campaign,
                parseDate(registrationTime), // Convert string to Date
                parseDate(validTo), // Convert string to Date
                status
        );
        // Ensure totalPages is calculated correctly and is non-negative
        int totalPages = (int) Math.ceil((double) totalRegistrations / pageSize);
        if (totalPages < 1) {
            totalPages = 1; // At least one page must be available
        }

        // Retrieve additional information for each registration
        Map<Integer, String> customerEmail = new HashMap<>();
        Map<Integer, String> subjectTitle = new HashMap<>();
        Map<Integer, String> packageName = new HashMap<>();
        Map<Integer, String> staffUsername = new HashMap<>();

        // Loop through registrations to fetch additional details
        for (Registrations registration : registrations) {
            // Fetch and map additional details
            customerEmail.put(registration.getUserId(), userDAO.getEmailByUserId(registration.getUserId()));
            subjectTitle.put(registration.getSubjectId(), subjectDAO.getSubjectNameById(registration.getSubjectId()));
            packageName.put(registration.getPackageId(), packageDAO.getNameByPackageId(registration.getPackageId()));
            staffUsername.put(registration.getStaffId(), userDAO.getStaffUserNameByUserId(registration.getStaffId()));
        }

        // Get lists for dropdowns (for filtering)
        Map<Integer, Subject> subjects = subjectDAO.getAllSubject();
        Map<Integer, Campaigns> campaigns = campaignDAO.getAllCampaigns();
        
        // Set attributes to be used in the JSP for rendering the list and filters
        request.setAttribute("listR", registrations);
        request.setAttribute("customerEmail", customerEmail);
        request.setAttribute("subjectTitle", subjectTitle);
        request.setAttribute("packageName", packageName);
        request.setAttribute("staffUsername", staffUsername);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("listS", subjects);
        request.setAttribute("listC", campaigns);
        
        // Forward the request to the RegistrationList.jsp page
        request.getRequestDispatcher("RegistrationList.jsp").forward(request, response);
    }

    // Helper method to parse a date from a string
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null; // Return null for empty or null date strings
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false); // Strict parsing
            return dateFormat.parse(dateStr); // Parse and return the date
        } catch (ParseException e) {
            // Log the parse error (consider using a logger)
            e.printStackTrace(); // Handle parse exception
            return null; // Return null on parse error
        }
    }
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response); // Handle GET requests
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response); // Handle POST requests
    }
}