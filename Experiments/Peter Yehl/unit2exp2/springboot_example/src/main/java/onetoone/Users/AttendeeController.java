package onetoone.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 
 * @author Peter Yehl
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
        Attendee attendee = attendeeRepository.findById(id);
        if(attendee == null)
            return null;
        attendeeRepository.save(request);
        return attendeeRepository.findById(id);
    }

    @DeleteMapping(path = "/attendees/{id}")
    String deleteAttendee(@PathVariable int id){
        attendeeRepository.deleteById(id);
        return success;
    }
}
