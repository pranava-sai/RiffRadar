package onetoone.Email;

import onetoone.OTP.OTPService;
import onetoone.Users.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class EmailController {

    EmailConfig config = new EmailConfig();

    @Autowired
    private EmailServiceImpl emailService = new EmailServiceImpl();

    @PostMapping("/generate")
    public String generateEmail(@RequestBody LoginInfo loginInfo) {
        //LoginInfo user = loginInfoRepository.findByEmailId(email);

        String accountNumber = String.valueOf(loginInfo.getId());
        String email = loginInfo.getEmailId();
        String name = "test";

        emailService.sendBasicEmail("kadenb816@gmail.com", "test", "body");


        return "email sent";
    }

}
