package controller;

import model.Users;
import dal.UserDAO;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class forgotPassword extends HttpServlet {

    // Length of the generated token in bytes
    private static final int TOKEN_LENGTH = 8; // 8 bytes

    // Handles POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the session and the email from the session
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("resetEmail");

        // Check if the email is null
        if (email == null) {
            // Set an error message attribute and forward to recoverAccount.jsp
            request.setAttribute("message", "Invalid Email.");
            request.getRequestDispatcher("/recoverAccount.jsp").forward(request, response);
            return;
        }

        // Instantiate the UserDAO to interact with the database
        UserDAO userDAO = new UserDAO();
        // Retrieve the user by the email
        Users user = userDAO.getUserByEmail(email);

        // Check if the user exists
        if (user != null) {
            // Generate a token and update the reset token in the database
            String token = generateToken();
            userDAO.updateResetToken(email, token);

            // Create the reset password link
            String resetLink = request.getRequestURL().toString().replace("forgotPassword", "resetPassword") + "?token=" + token;
            String message = "Please click the following link to reset your password: " + resetLink;

            // Send the reset password link to the user's email
            sendEmail(user.getEmail(), "Reset Password", message);

            // Set a success message attribute
            request.setAttribute("message", "A reset password link has been sent to your email.");
        } else {
            // Set an error message if the email account is not found
            request.setAttribute("message", "Email account not found.");
        }

        // Forward to recoverAccount.jsp
        request.getRequestDispatcher("/recoverAccount.jsp").forward(request, response);
    }

    // Generates a secure random token
    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[TOKEN_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Sends an email with the provided details
    private void sendEmail(String to, String subject, String body) {
        // Email account credentials
        final String username = "phuongtthe186681@fpt.edu.vn";
        final String password = "";

        // Configure email session properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create an email session
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
