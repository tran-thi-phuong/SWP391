package controller;

import dal.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AddRegistrationBySale extends HttpServlet {

    private static final int TOKEN_LENGTH = 8; // Length of the generated token
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Characters for random string generation
    private static final SecureRandom RANDOM = new SecureRandom(); // Secure random generator for creating tokens

    // DAO instances for database operations
    private final RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    private final UserDAO userDAO = new UserDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final PackagePriceDAO packageDAO = new PackagePriceDAO();

    // Generates a random string of specified length
    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()))); // Pick a random character
        }
        return result.toString(); // Return the generated string
    }

    // Handles GET requests to show the registration form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user"); // Get the user from session
        String subjectIdStr = request.getParameter("subjectID"); // Get subject ID from request parameters
        String packageIdStr = request.getParameter("packageID"); // Get package ID from request parameters
        int subjectID = 0; // Default subject ID
        int packageId = 0; // Default package ID

        // Redirect to login if user is not found in session
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Parse subjectID
        if (subjectIdStr != null) {
            try {
                subjectID = Integer.parseInt(subjectIdStr); // Convert subject ID to integer
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Log parsing error
            }
        }
        // Parse packageId
        if (packageIdStr != null) {
            try {
                packageId = Integer.parseInt(packageIdStr); // Convert package ID to integer
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Log parsing error
            }
        }

        // Retrieve subjects and package prices
        Map<Integer, Subject> subjects = subjectDAO.getAllSubject(); // Fetch all subjects
        List<PackagePrice> prices = packageDAO.searchBySubjectId(subjectID); // Fetch prices by subject ID
        PackagePrice packagePrice = packageDAO.getPriceByPackageId(packageId); // Fetch price by package ID

        // Calculate total cost
        double totalCost = 0.0;
        if (packagePrice != null) {
            Double price = packagePrice.getPrice();
            Double salePrice = packagePrice.getSalePrice();
            // Use sale price if available and less than regular price
            totalCost = (salePrice != null && salePrice < price) ? salePrice : price;
        }

        // Set attributes to request for use in JSP
        request.setAttribute("subject", subjects);
        request.setAttribute("price", prices);
        request.setAttribute("selectedSubjectID", subjectID);
        request.setAttribute("totalCost", totalCost);

        // Forward request to JSP page
        request.getRequestDispatcher("AddRegistrationBySale.jsp").forward(request, response);
    }

    // Handles POST requests to process the registration form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve parameters from the request
            String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";

            // Check if the email input is empty
            if (email.isEmpty()) {
                // Set an error message for empty input
                request.setAttribute("errorMessage", "Email field cannot be empty.");
                // Forward the request to findAccount.jsp
                request.getRequestDispatcher("/findAccount.jsp").forward(request, response);
                return;
            }

            int subjectId = Integer.parseInt(request.getParameter("subjectID"));
            int packageId = Integer.parseInt(request.getParameter("packageID"));
            double totalCost = Double.parseDouble(request.getParameter("totalCost"));
            String phone = request.getParameter("mobile");
            String gender = request.getParameter("gender");
            String note = request.getParameter("note");

            HttpSession session = request.getSession();
            Users user = (Users) session.getAttribute("user"); // Get user from session

            // Check if user is found in session
            if (user == null) {
                request.setAttribute("errorMessage", "User not found in session.");
                request.getRequestDispatcher("error.jsp").forward(request, response); // Forward to error page
                return;
            }

            Integer staffId = user.getUserID(); // Get staff ID
            if (staffId == null) {
                request.setAttribute("errorMessage", "Staff ID is null.");
                request.getRequestDispatcher("error.jsp").forward(request, response); // Forward to error page
                return;
            }

            int userId;
            Users existingUser = userDAO.getUserByEmail(email); // Check if user exists by email
            if (existingUser != null) {
                userId = existingUser.getUserID(); // Use existing user's ID
            } else {
                String randomPassword = generateRandomString(10); // Generate a random password
                userId = userDAO.addUser(email, randomPassword, gender, phone, generateRandomString(10)); // Add new user and get ID
                sendEmail(email, request); // Send email to new user
            }

            // Add the registration to the database
            addRegistration(userId, subjectId, packageId, totalCost, staffId, note);

            // Redirect to the registration list page after successful processing
            response.sendRedirect(request.getContextPath() + "/listRegistration");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format in form data.");
            request.getRequestDispatcher("error.jsp").forward(request, response); // Forward to error page
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response); // Forward to error page
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error processing registration: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response); // Forward to error page
        }
    }

    // Method to add a registration record in the database
    private void addRegistration(int userId, int subjectId, int packageId, double totalCost, Integer staffId, String note) throws SQLException {
        Date registrationTime = new Date(); // Get current date and time for registration
        Date validFrom = registrationTime; // Valid from the registration time
        int durationDays = packageDAO.getDurationByPackageId(packageId); // Get duration from package
        Date validTo = new Date(validFrom.getTime() + durationDays * 24 * 60 * 60 * 1000L); // Calculate valid to date based on duration

        // Add new registration using the DAO
        registrationsDAO.addNewRegistration(userId, subjectId, packageId, totalCost, registrationTime, validFrom, validTo, staffId, note);
    }

    // Method to send an email to the user
    private void sendEmail(String recipientEmail, HttpServletRequest request) {
        String token = generateToken(); // Generate a reset token
        userDAO.updateResetToken(recipientEmail, token); // Store the token in the database
        String subject = "Complete Your Profile"; // Email subject

        // Create the reset link for completing the profile
        String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/CompleteProfile?token=" + token;

        // Email message body
        String messageBody = "Dear Customer,\n\n"
                + "Thank you for registering for our course. Since you have not registered an account with our system, we have created an account for you with the email you provided.\n"
                + "Your account has been created successfully.\nYour temporary password is: 12345678\n\n"
                + "Please complete your profile by clicking the link below:\n"
                + resetLink + "\n\n"
                + "Thank you!";

        // SMTP email setup
        final String username = "phuongtthe186681@fpt.edu.vn"; // Email sender
        final String password = "mslazpojvjimeuzt"; // Email password
        Properties props = new Properties(); // Email properties
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password); // Authenticate with SMTP server
            }
        });

        try {
            Message message = new MimeMessage(session); // Create a new email message
            message.setFrom(new InternetAddress(username)); // Set sender's email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Set recipient's email
            message.setSubject(subject); // Set email subject
            message.setText(messageBody); // Set email body
            Transport.send(message); // Send the email
        } catch (MessagingException e) {
            e.printStackTrace(); // Log email sending error
        }
    }

    // Generate a random token for account verification
    private String generateToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()))); // Generate token characters
        }
        return token.toString(); // Return generated token
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
            response.sendRedirect("/Homepage");
            return false;
        } else if (pageID == null) {
            // Nếu không tìm thấy trang trong hệ thống phân quyền, chuyển đến trang lỗi
            response.sendRedirect("error.jsp");
            return false;
        }

        return true; // Người dùng có quyền truy cập trang này
    }
}
