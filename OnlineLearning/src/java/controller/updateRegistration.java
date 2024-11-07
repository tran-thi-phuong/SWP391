package controller;

import dal.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.*;

public class updateRegistration extends HttpServlet { // Class names should start with an uppercase letter

    private static final long serialVersionUID = 1L;
    private final RegistrationsDAO registrationsDAO = new RegistrationsDAO();
    private final PackagePriceDAO packageDAO = new PackagePriceDAO();
    private final Customer_SubjectDAO csDAO = new Customer_SubjectDAO();
    private final LessonDAO lessonDAO = new LessonDAO();
    private final Lesson_UserDAO lesson_User = new Lesson_UserDAO();
    private static final int TOKEN_LENGTH = 8; // Length of the generated token
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Characters for random string generation
    private static final SecureRandom RANDOM = new SecureRandom(); // Secure random generator for creating tokens

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!hasPermission(request, response)) {
            return;
        }
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

        // Retrieve the current registration record
        Registrations currentRegistration = registrationsDAO.getRegistrationById(registrationId);
        if (currentRegistration == null) {
            request.setAttribute("error", "Registration not found.");
            request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
            return;
        }

        String currentStatus = currentRegistration.getStatus();
        int subjectID = currentRegistration.getSubjectId();
        int userID = currentRegistration.getUserId();
        Date validFrom = (Date) currentRegistration.getValidFrom();
        Date validTo = (Date) currentRegistration.getValidTo();
        String customerEmail = request.getParameter("customerEmail");

        if (customerEmail == null || customerEmail.isEmpty()) {
            request.setAttribute("error", "Customer email is missing.");
            request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
            return;
        }

        // Determine new validFrom and validTo based on status changes
        if ("Processing".equals(currentStatus) && "Active".equals(newStatus)) {
            int packageId = registrationsDAO.getPackageIdByRegistrationId(registrationId);
            LocalDate currentDate = LocalDate.now();
            validFrom = Date.valueOf(currentDate);

            int durationDays = packageDAO.getDurationByPackageId(packageId);
            validTo = Date.valueOf(currentDate.plusDays(durationDays));
            csDAO.addCustomerSubject(subjectID, userID);
            List<Lesson> lessons = lessonDAO.getLessonBySubjectId(subjectID);
            if (lessons == null || lessons.isEmpty()) {
                request.setAttribute("error", "No lessons found for the subject.");
                request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
                return;
            }

            boolean allLessonsAdded = true;
            for (Lesson lesson : lessons) {
                boolean lessonAdded = lesson_User.addLesson(lesson.getLessonID(), userID);
                if (!lessonAdded) {
                    allLessonsAdded = false;
                    request.setAttribute("error", "Failed to add lessonID = " + lesson.getLessonID() + " for userID = " + userID);
                    request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
                    return;
                }
            }
            // Check if all lessons were added successfully
            if (allLessonsAdded) {
                sendEmail(customerEmail, request); // Notify customer on status update
            } else {
                request.setAttribute("error", "Failed to add all lessons for the customer.");
                request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
                return;
            }

        } else if ("Active".equals(currentStatus) && "Inactive".equals(newStatus)) {
            validTo = Date.valueOf(LocalDate.now()); // Set validTo to current date if status is changed to Inactive
        }

        // Prepare the updated registration record
        Registrations registrationToUpdate = new Registrations();
        registrationToUpdate.setRegistrationId(registrationId);
        registrationToUpdate.setStatus(newStatus);
        registrationToUpdate.setValidFrom(validFrom);
        registrationToUpdate.setValidTo(validTo);
        registrationToUpdate.setNote(note);
        registrationToUpdate.setStaffId(staffId);

        // Attempt to update the registration
        boolean isUpdated = registrationsDAO.updateRegistration(registrationToUpdate);

        if (isUpdated) {
            session.setAttribute("success", "Registration updated successfully!");
            response.sendRedirect(request.getContextPath() + "/listRegistration");
        } else {
            request.setAttribute("error", "Failed to update registration.");
            request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
        }
    }

// Method to send an email to the user
    private void sendEmail(String recipientEmail, HttpServletRequest request) {
        UserDAO usersDAO = new UserDAO(); // Tạo một đối tượng UsersDAO
        String token = generateToken(); // Tạo một mã token ngẫu nhiên

        // Cập nhật token cho tài khoản dựa trên email
        boolean isTokenUpdated = usersDAO.updateResetToken(recipientEmail, token);
        if (!isTokenUpdated) {
            // Xử lý khi không thể cập nhật token
            System.err.println("Failed to update reset token for email: " + recipientEmail);
            return; // Thoát khỏi hàm nếu cập nhật thất bại
        }
        String subject = "Complete Your Profile"; // Email subject

        // Create the reset link for completing the profile
        String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/CompleteProfile?token=" + token;

        // Email message body
        String messageBody = """
                             Dear Customer,
                             
                             Thank you for registering for our course. Your register has been approved by our system.
                             Since you have not registered an account with our system, we have created an account for you with the email you provided.
                             Your account has been created successfully.
                             Your temporary password is: 12345678
                             
                             Please complete your profile by clicking the link below:
                             """
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
            @Override
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
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());
        String userRole = currentUser.getRole();

        if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
            response.sendRedirect(request.getContextPath() + "/Homepage");
            return false;
        } else if (pageID == null) {
            response.sendRedirect("error.jsp");
            return false;
        }

        return true;
    }
}
