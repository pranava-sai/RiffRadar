package onetoone.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@RestController
public class AttendeeController {

    @Autowired
    AttendeeRepository attendeeRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/attendees")
    List<Attendee> getAllAttendees(){
        return attendeeRepository.findAll();
    }

    @GetMapping(path = "/attendees/{id}")
    Attendee getAttendeeById( @PathVariable int id){
        return attendeeRepository.findById(id);
    }

    @PostMapping(path = "/attendees")
    String createAttendee(@RequestBody Attendee attendee){
        if (attendee == null)
            return failure;
        attendeeRepository.save(attendee);
        return success;
    }

    @PutMapping("/attendees/{id}")
    Attendee updateAttendee(@PathVariable int id, @RequestBody Attendee request){
        Attendee user = attendeeRepository.findById(id);
        if(user == null)
            return null;
        attendeeRepository.save(request);
        return attendeeRepository.findById(id);
    }

    @DeleteMapping(path = "/attendee/{id}")
    String deleteAttendee(@PathVariable int id){
        attendeeRepository.deleteById(id);
        return success;
    }
}
