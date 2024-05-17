package onetoone.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import onetoone.OTP.OTPServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImpl implements EmailService {

    EmailConfig config = new EmailConfig();

    @Autowired
    private JavaMailSender mailSender = config.getJavaMailSender();

    @Override
    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            // No need to set the "from" address; it is automatically set by Spring Boot based on your properties
            helper.setSubject(subject);
            helper.setText(text, true); // Set the second parameter to true to send HTML content
            mailSender.send(message);

            future.complete(null); // Indicate that the email sending is successful
        } catch (MessagingException e) {
            e.printStackTrace();
            future.completeExceptionally(e); // Indicate that the email sending failed
        }

        return future;
    }

    @Override
    public String getOtpPasswordResetEmailTemplate(String email, String name, String accountNumber, String otp) {
        // Create the formatted email template with the provided values

        String emailTemplate = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>RiffRadar Verification Code</title>"
                + "<style>"
                + "body {font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; min-height: 100vh;}"
                + ".container {background-color: #fff; width: 600px; box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1); text-align: center;}"
                + ".header {background-color: #4285f4; color: white; padding: 20px;}"
                + ".content {padding: 20px;}"
                + ".code {font-size: 2em; margin: 20px;}"
                + ".footer {font-size: 0.8em; color: #666; padding: 10px;}"
                + ".branding {font-size: 2.5em; color: #00466a; font-weight: 700; text-decoration: none; padding: 30px 0; margin-left: 3%;}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='branding'>RiffRadar</div>"
                + "<div class='header'>Verification Code</div>"
                + "<div class='content'>"
                + "<p style='font-size:1.1em; text-align: left;'>Dear " + name + ",</p>"
                + "<p style='font-size:1.1em;'>We have received a request for password reset on your account " + email
                + " through the <strong>RiffRadar App</strong>.</p>"
                + "<div class='code'>" + otp + "</div>"
                + "If you did not request this verification or if you're having trouble entering your OTP, please ignore this email or contact us at <a href='mailto:riff-radar@outlook.com'>riff-radar@outlook.com</a>"
                + ". <strong>Do not forward or give this code to anyone.</strong></p>"
                + "</div>"
                + "<p style='font-size:1.1em; text-align: left; margin-left: 3%;'>Sincerely yours,<br>"
                + "<strong>The RiffRadar Team</strong>"
                + "</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        return emailTemplate;
    }

    @Override
    public String getOtpSignupEmailTemplate(String email, String name, String accountNumber, String otp) {

        String emailTemplate = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>RiffRadar Verification Code</title>"
                + "<style>"
                + "body {font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; min-height: 100vh;}"
                + ".container {background-color: #fff; width: 600px; box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1); text-align: center;}"
                + ".header {background-color: #4285f4; color: white; padding: 20px;}"
                + ".content {padding: 20px;}"
                + ".code {font-size: 2em; margin: 20px;}"
                + ".footer {font-size: 0.8em; color: #666; padding: 10px;}"
                + ".branding {font-size: 2.5em; color: #00466a; font-weight: 700; text-decoration: none; padding: 30px 0; margin-left: 3%;}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='branding'>RiffRadar</div>"
                + "<div class='header'>Verification Code</div>"
                + "<div class='content'>"
                + "<p style='font-size:1.1em; text-align: left;'>Dear " + name + "</p>"
                + "<p style='font-size:1.1em;'>Welcome to <strong>RIFFRADAR</strong>! We're excited to have you join our community. To get started, we just need to verify your email address.<br><br> Your OTP is:</p>"
                + "<div class='code'>" + otp + "</div>"
                + "If you did not request this verification or if you're having trouble entering your OTP, please ignore this email or contact us at <a href='mailto:riff-radar@outlook.com'>riff-radar@outlook.com</a>"
                + "<p>Thank you for choosing <strong>RIFFRADAR</strong>. We're thrilled to help you discover and enjoy new music experiences.</p>"
                + "</div>"
                + "<p style='font-size:1.1em; text-align: left;margin-left: 3%;'>Sincerely yours,<br>"
                + "<strong>The RiffRadar Team</strong>"
                + "</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        return emailTemplate;
        }

    @Override
    public String getOrderEmailTemplate(String concertName, String name, String bookingNumber, String today, String currentTime, String numTickets) {
        String emailTemplate = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>RiffRadar Verification Code</title>"
                + "<style>"
                + "body {font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; min-height: 100vh;}"
                + ".container {background-color: #fff; width: 600px; box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1); text-align: center;}"
                + ".header {background-color: #4285f4; color: white; padding: 20px;}"
                + ".content {padding: 20px;}"
                + ".code {font-size: 2em; margin: 20px;}"
                + ".footer {font-size: 0.8em; color: #666; padding: 10px;}"
                + ".branding {font-size: 2.5em; color: #00466a; font-weight: 700; text-decoration: none; padding: 30px 0; margin-left: 3%;}"
                + ".confirmed {display: flex; align-items: center; justify-content: flex-start;}"
                + ".image-container {flex: 0 0 auto; margin-right: 20px; margin-left: 15%;}"
                + ".text-container {flex: 1; margin-right: 15%;}"
                + ".confirmed img {width: 150px; height: 150px;}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='branding'>RiffRadar</div>"
                + "<div class='header'>Booking Confirmation</div>"
                + "<div class='content'>"
                + "<p style='font-size:1.1em; text-align: left;'>Hi " + name + "</p>"
                + "<p style='font-size: 1.1em; text-align: center;'>You are going to </p>"
                + "<p style='font-size: 2.2em; text-align: center; color: #208fc7;'>" + concertName + "</p>"
                + "<p style='font-size:1.1em;'>Thank you for choosing <strong>RIFFRADAR</strong>! This email contains important information about your order. Please save it for future reference.<br></p>"
                + "<div class='confirmed'>"
                + "<div class='image-container'>"
                + "<img src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVmuUTNdKjbXp8C3Ml301Os3b4z9lTjcigYv4aE_YZ7Q&s' alt='confirmed image'>"
                + "</div>"
                + "<div class='text-container'>"
                + "<p><strong>Order Confirmed</strong></p>"
                + "<p>Order No. " + bookingNumber + "</p>"
                + "<p>Booking Date: " + today + "</p>"
                + "<p>Booking Time: " + currentTime + "</p>"
                + "<p>No. Of Tickets: " + numTickets + "</p>"
                + "</div>"
                + "</div><br />"
                + "<p>If you didn't make this purchase, please send us an email immediately at<br /> <a href='mailto:riff-radar@outlook.com'>riff-radar@outlook.com</a></p>"
                + "<div class='content'>"
                + "<p>Thank you for choosing <strong>RIFFRADAR</strong>. We're excited to assist you in exploring and relishing new music adventures.</p>"
                + "</div>"
                + "</div>"
                + "<p style='font-size:1.1em; text-align: left;margin-left: 3%;'>Sincerely yours,<br>"
                + "<strong>The RiffRadar Team</strong>"
                + "</p>"
                + "</div>"
                + "</body>"
                + "</html>";

       return emailTemplate;
    }


    public void sendBasicEmail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("do.not.reply.riffradar@gmail.com");
        message.setTo(toEmail);
        message.setText("test");
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Mail sent Succesfully");
    }

}
