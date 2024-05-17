package onetoone.OTP;

import java.util.concurrent.CompletableFuture;

public interface OTPService {
    String generateOTP(String accountNumber);
    CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp, String request);
    boolean validateOTP(String accountNumber, String otp);
}
