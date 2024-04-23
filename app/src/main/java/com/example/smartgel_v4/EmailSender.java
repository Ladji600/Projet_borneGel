package com.example.smartgel_v4;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail(String recipient, String subject, String message) {
        final String username = "your_email@gmail.com"; // Votre adresse e-mail
        final String password = "your_password"; // Votre mot de passe

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Serveur SMTP Gmail
        props.put("mail.smtp.port", "587"); // Port SMTP Gmail

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Transport.send(mimeMessage);

            System.out.println("E-mail envoyé avec succès.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
