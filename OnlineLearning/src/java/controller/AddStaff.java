package controller;

import dal.PagesDAO;
import dal.RolePermissionDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.Users;

@WebServlet("/AddStaff")
public class AddStaff extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private static final int TOKEN_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return result.toString();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return; // Exit if the user lacks permission
        }
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String username = generateRandomString(10); // Generating a random username
        // Check if the email already exists
        if (userDAO.isEmailExists(email)) {
            request.setAttribute("errorMessage", "The email address is already registered.");
            request.getRequestDispatcher("UserList.jsp").forward(request, response);
            return; // Stop execution here if email exists
        }

        boolean isAdded = userDAO.addStaff(email, username, role);

        // Log the result of the addStaff method
        if (isAdded) {
            sendEmail(email, request); // Send email on successful addition
            request.setAttribute("successMessage", "Staff member added successfully, and an email has been sent.");
        } else {
            request.setAttribute("errorMessage", "Failed to add staff member.");
        }

        // Forward to the UserList page after the action
        response.sendRedirect("UserList");
    }

    private void sendEmail(String recipientEmail, HttpServletRequest request) {
        String token = generateToken();
        userDAO.updateResetToken(recipientEmail, token); // Store the token in the database
        String subject = "Complete Your Profile";

        // Create the reset link for completing the profile
        String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/CompleteProfile?token=" + token;

        // Email message body
        String messageBody = """
                             Dear our staff,
                             Your account has been created successfully.
                             You can login with your email and the temporary password we send to you. 
                             Your temporary password is: 12345678
                             Otherwise, you can also complete your profile by clicking the link below:
                             """ + resetLink + "\n\nThank you!";

        // SMTP email setup
        final String username = "phuongtthe186681@fpt.edu.vn"; // Sender's email
        final String password = "mslazpojvjimeuzt"; // Email password
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Generate a random token for account verification
    private String generateToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }
     private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");

        // Check if the user is logged in
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        // Check permissions
        String userRole = currentUser.getRole();
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage");
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true; // User has permission to access the page
    }
}
