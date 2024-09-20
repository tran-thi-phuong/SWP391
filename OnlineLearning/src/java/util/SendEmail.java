/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator; // Use javax.mail.Authenticator
import javax.mail.PasswordAuthentication; // Use javax.mail.PasswordAuthentication
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.Transport;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;


/**
 *
 * @author tuant
 */
public class SendEmail {

    // email: tuando1007@gmail.com
    // pass: aixt uews lypz qwnx
    static final String from = "tuando1007@gmail.com";
    static final String password = "aixtuewslypzqwnx";

    public static void sendCode(String toEmail, String verificationCode) {
        Properties pros = new Properties();
        pros.put("mail.smtp.host", "smtp.gmail.com"); //SMTP host
        pros.put("mail.smtp.port", "587"); //TLS: port 587 
        pros.put("mail.smtp.auth", "true");
        pros.put("mail.smtp.starttls.enable", "true"); //protocol declaration

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }
        };
        Session session = Session.getInstance(pros, auth);
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            msg.setSubject("Register verification");
            msg.setSentDate(new Date());
            msg.setText("Your verification code is: " + verificationCode);
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        sendCode("tuancrok@gmail.com", "203817");
    }
}
