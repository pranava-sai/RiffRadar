package onetoone.OTP;

import onetoone.Email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPServiceImpl implements OTPService{
    // Existing code for OTP generation and database operations

     @Autowired
    EmailServiceImpl emailService;

    @Override
    @Async
    public CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp, String request) {
        // Compose the email content
        String subject = "OTP Verification";
        String emailText = "";

        if(request.equals("reset-password")) {
            emailText = emailService.getOtpPasswordResetEmailTemplate(email, name, "xxx" + accountNumber, otp);
        } else if(request.equals("signup")){
            emailText = emailService.getOtpSignupEmailTemplate(email, name, "xxx" + accountNumber, otp);
        }

        CompletableFuture<Void> emailSendingFuture = emailService.sendEmail(email, subject, emailText);

        return emailSendingFuture.thenApplyAsync(result -> true)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }

    @Override
    public String generateOTP(String accountNumber) {
        String otpDigits = "0123456789";
        Random random = new Random();
        char[] otpArray = new char[6];

        for(int i = 0; i < 6; i++){
            otpArray[i] = otpDigits.charAt(random.nextInt(otpDigits.length()));
        }

        String OTPCode = new String (otpArray);
        return OTPCode;
    }

    @Override
    public boolean validateOTP(String accountNumber, String otp) {
        return false;
    }
}
