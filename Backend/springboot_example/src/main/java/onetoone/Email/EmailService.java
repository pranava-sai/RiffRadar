package onetoone.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

public interface EmailService
{
    CompletableFuture<Void> sendEmail(String to, String subject, String text);

     String getOtpPasswordResetEmailTemplate(String email, String name, String accountNumber, String otp);

     String getOtpSignupEmailTemplate(String email, String name, String accountNumber, String otp);

     String getOrderEmailTemplate(String concertName, String name, String bookingNumber, String today, String currentTime, String numTickets);

     void sendBasicEmail(String toEmail, String subject, String body);

}
