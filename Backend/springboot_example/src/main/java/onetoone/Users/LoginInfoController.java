package onetoone.Users;

import com.mysql.cj.log.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * @author Vivek Bengre
 *
 */

@Tag(name = "LoginInfo", description = "LoginInfo API")
@RestController
public class LoginInfoController {

    @Autowired
    LoginInfoRepository loginInfoRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/logininfo")
    List<LoginInfo> getAllLoginInfo(){
        return loginInfoRepository.findAll();
    }

    //@GetMapping(path = "/attendees/{email}")
    //Attendee getAttendeeById( @PathVariable String email){
    //    return attendeeRepository.findByEmailId(email);
    //
    //}

    @Operation(summary = "Log a specific user in")
    @PostMapping(path = "/login")
    LoginInfo loginAttendee(@RequestBody LoginInfo loginInfo){
        if (loginInfo == null)
            return null;
        LoginInfo foundUser = loginInfoRepository.findByEmailId(loginInfo.getEmailId());
        if(foundUser.getPassword().equals(loginInfo.getPassword())){
            return foundUser;
        }
        return null;
    }

    @Operation(summary = "Create a new set of Login Info")
    @PostMapping(path = "/logininfo")
    String createLoginInfo(@RequestBody LoginInfo loginInfo){
        if (loginInfo == null)
            return failure;
        loginInfoRepository.save(loginInfo);
        return success;
    }

    @Operation(summary = "Change a specific set of Login Info")
    @PutMapping("/logininfo/{id}")
    LoginInfo updateLoginInfo(@PathVariable int id, @RequestBody LoginInfo request){
        LoginInfo user = loginInfoRepository.findById(id);
        if(user == null)
            return null;
        loginInfoRepository.save(request);
        return loginInfoRepository.findById(id);
    }

    @Operation(summary = "Delete a specific set of Login Info")
    @DeleteMapping(path = "/logininfo/{id}")
    String deleteAttendee(@PathVariable int id){
        loginInfoRepository.deleteById(id);
        return success;
    }
}

