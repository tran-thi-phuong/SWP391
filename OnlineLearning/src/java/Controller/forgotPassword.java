package Controller;

import Model.User;
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

    private static final int TOKEN_LENGTH = 8; // 8 bytes

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy email từ session
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            request.setAttribute("message", "Invalid Email.");
            request.getRequestDispatcher("/recoverAccount.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            String token = generateToken();
            userDAO.updateResetToken(email, token); // Lưu token vào cơ sở dữ liệu

            String resetLink = request.getRequestURL().toString().replace("forgotPassword", "resetPassword") + "?token=" + token;
            String message = "Please click the following link to reset your password: " + resetLink;

            // Gửi email cho người dùng với liên kết đặt lại mật khẩu
            sendEmail(user.getEmail(), "Reset Password", message);

            request.setAttribute("message", "A reset password link has been sent to your email.");
        } else {
            request.setAttribute("message", "Email account not found.");
        }

        request.getRequestDispatcher("/recoverAccount.jsp").forward(request, response);
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[TOKEN_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private void sendEmail(String to, String subject, String body) {
        final String username = "phuongtthe186681@fpt.edu.vn";
        final String password = "mslazpojvjimeuzt";

        // Cấu hình thuộc tính cho phiên email
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Tạo phiên email
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Gửi email
            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
