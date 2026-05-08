package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    // ⚠️ Change these to your Gmail credentials
    private static final String SENDER_EMAIL = "your_email@gmail.com";
    private static final String SENDER_PASSWORD = "your_app_password"; // App Password, NOT Gmail password

    // ⚠️ Change this to faculty's email
    private static final String FACULTY_EMAIL = "faculty_email@gmail.com";

    public static void sendLeaveNotification(
            String studentUsername,
            String leaveType,
            String startDate,
            String endDate,
            String reason) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(FACULTY_EMAIL));

            message.setSubject("📋 New Leave Application - " + studentUsername);

            String body = "Dear Faculty,\n\n"
                    + "A new leave application has been submitted.\n\n"
                    + "━━━━━━━━━━━━━━━━━━━━━━\n"
                    + "Student     : " + studentUsername + "\n"
                    + "Leave Type  : " + leaveType + "\n"
                    + "From Date   : " + startDate + "\n"
                    + "To Date     : " + endDate + "\n"
                    + "Reason      : " + reason + "\n"
                    + "━━━━━━━━━━━━━━━━━━━━━━\n\n"
                    + "Please login to SmartLeave to review this application.\n\n"
                    + "URL: http://localhost:8080/LeaveManagementSystem/login.jsp\n\n"
                    + "Regards,\n"
                    + "SmartLeave System";

            message.setText(body);
            Transport.send(message);

            System.out.println("✅ Email sent to faculty successfully!");

        } catch (MessagingException e) {
            System.out.println("❌ Email sending failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}