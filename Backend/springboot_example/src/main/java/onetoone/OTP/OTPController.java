package onetoone.OTP;

import onetoone.Users.LoginInfo;
import onetoone.Users.LoginInfoController;
import onetoone.Users.LoginInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class OTPController  {

    @Autowired
    private OTPServiceImpl otpService;



    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody OTPInfo otpInfo) {

      String accountNumber = otpInfo.getId();
        String email = otpInfo.getEmailId();
        String name = otpInfo.getName();


        // Generate OTP and save it in the database
        String otp = otpService.generateOTP(accountNumber);

        // Send the OTP to the user's email address asynchronously
CompletableFuture<Boolean> emailSendingFuture = otpService.sendOTPByEmail(email, name, accountNumber, otp, otpInfo.getRequest());

        // Wait for the email sending process to complete and handle the response
        try {
            boolean otpSent = emailSendingFuture.get(); // This will block until the email sending is complete

            if (otpSent) {
                // Return JSON response with success message
                return ResponseEntity.ok().body("{\"message\": \"OTP sent successfully\",  \"OTP Code\": " + otp + " }");
            } else {
                // Return JSON response with error message
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to send OTP\"}");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Return JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to send OTP\"}");
        }
    }
}
