package onetoone.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserByUserName( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/fanuser")
    String createFanUser(@RequestBody FanUser user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }
    @PostMapping(path = "/venueuser")
    String createVenueUser(@RequestBody VenueUser user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }
    @PostMapping(path = "/banduser")
    String createBandUser(@RequestBody BandUser user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    /* not safe to update */
//    @PutMapping("/users/{id}")
//    User updateUser(@PathVariable int id, @RequestBody User request){
//        User user = userRepository.findById(id);
//        if(user == null)
//            return null;
//        userRepository.save(request);
//        return userRepository.findById(id);
//    }

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);

        if(user == null) {
            throw new RuntimeException("username does not exist");
        }
        else if (user.getId() != id){
            throw new RuntimeException("path variable Username does not match User request Username");
        }

        userRepository.save(request);
        return userRepository.findById(id);
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
}
