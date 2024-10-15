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
import java.util.Base64;
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

    private static final int TOKEN_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    private final UserDAO userDAO = new UserDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final PackagePriceDAO packageDAO = new PackagePriceDAO();

    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return result.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        String subjectIdStr = request.getParameter("subjectID");
        String packageIdStr = request.getParameter("packageID");
        int subjectID = 0;
        int packageId = 0;
if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        // Parse subjectID
        if (subjectIdStr != null) {
            try {
                subjectID = Integer.parseInt(subjectIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        // Parse packageId
        if (packageIdStr != null) {
            try {
                packageId = Integer.parseInt(packageIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        // Retrieve subjects and package prices
        Map<Integer, Subject> subjects = subjectDAO.getAllSubject();
        List<PackagePrice> prices = packageDAO.searchBySubjectId(subjectID);
        PackagePrice packagePrice = packageDAO.getPriceByPackageId(packageId);

        // Calculate total cost
        double totalCost = 0.0;
        if (packagePrice != null) {
            Double price = packagePrice.getPrice();
            Double salePrice = packagePrice.getSalePrice();
            totalCost = (salePrice != null && salePrice < price) ? salePrice : price;
        }

        // Set attributes to request
        request.setAttribute("subject", subjects);
        request.setAttribute("price", prices);
        request.setAttribute("selectedSubjectID", subjectID);
        request.setAttribute("totalCost", totalCost);

        // Forward to JSP
        request.getRequestDispatcher("AddRegistrationBySale.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            int subjectId = Integer.parseInt(request.getParameter("subjectID"));
            int packageId = Integer.parseInt(request.getParameter("packageID"));
            double totalCost = Double.parseDouble(request.getParameter("totalCost"));
            String fullname = request.getParameter("full-name");
            String phone = request.getParameter("mobile");
            String gender = request.getParameter("gender");
            String note = request.getParameter("note");

            HttpSession session = request.getSession();
            Users user = (Users) session.getAttribute("user");

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

            int userId;
            Users existingUser = userDAO.getUserByEmail(email);
            if (existingUser != null) {
                userId = existingUser.getUserID();
            } else {
                String randomPassword = generateRandomString(10); // Generate random password
                userId = userDAO.addUser(email, randomPassword, gender, phone, generateRandomString(10));
                sendEmail(email, request);
            }

            // Add the registration
            addRegistration(userId, subjectId, packageId, totalCost, staffId, note);

            // Redirect to the registration list page after successful processing
            response.sendRedirect(request.getContextPath() + "/listRegistration");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format in form data.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error processing registration: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void addRegistration(int userId, int subjectId, int packageId, double totalCost, Integer staffId, String note) throws SQLException {
        Date registrationTime = new Date();
        Date validFrom = registrationTime;
        int durationDays = packageDAO.getDurationByPackageId(packageId);
        Date validTo = new Date(validFrom.getTime() + durationDays * 24 * 60 * 60 * 1000L);

        registrationsDAO.addNewRegistration(userId, subjectId, packageId, totalCost, registrationTime, validFrom, validTo, staffId, note);
    }

    private void sendEmail(String recipientEmail, HttpServletRequest request) {
    String token = generateToken();
    userDAO.updateResetToken(recipientEmail, token); // Make sure you're using the correct email parameter
    String subject = "Complete Your Profile";
    
    // Change the link to point to the CompleteProfile servlet
   String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                   request.getContextPath() + "/CompleteProfile?token=" + token;

    
    String messageBody = "Dear Customer,\n\n"
            + "Thank you for registering for our course. Since you have not registered an account with our system, we have created an account for you with the email you provided.\n"
            + "Your account has been created successfully.\nYour temporary password is: 12345678\n\n"
            + "Please complete your profile by clicking the link below:\n"
            + resetLink + "\n\n"
            + "Thank you!";

    final String username = "phuongtthe186681@fpt.edu.vn";
    final String password = "mslazpojvjimeuzt";
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        @Override
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

        // Send email
        Transport.send(message);
        System.out.println("Email sent successfully");
    } catch (MessagingException e) {
        throw new RuntimeException(e);
    }
}


    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[TOKEN_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
