package TTT.emailService;

import TTT.databaseUtils.CustomUserDAO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailService {

    CustomUserDAO customUserDAO = new CustomUserDAO();

    public boolean sendEmail(String from, String to, String subject, String email) throws IOException {
        boolean flag = false;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // avoid hang by setting timeout; 5 seconds
        properties.put("mail.smtp.timeout", "15000");
        properties.put("mail.smtp.connectiontimeout", "15000");

        String username = "togethertothetop2025@gmail.com";
        String password = readPasswordFromResource("static/passwords/emailPassword.txt");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });


        try {
            String text = "Hello, you received this email because you chose to change your password in " +
                    "the Together to The top app. Below is a unique password reset code, please enter it " +
                    "in the space provided." +
                    "\nYour Code is: " + customUserDAO.findCustomUserByEmail(email).getActivationCode();

            Message message = new MimeMessage(session);

            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(text);

            System.out.println("sending email with code...");
            Transport.send(message);

            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    private String readPasswordFromResource(String resourcePath) throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IOException("cannot find file with password to email: " + resourcePath);
        }

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

}
