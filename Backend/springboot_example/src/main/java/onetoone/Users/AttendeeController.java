package onetoone.Users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import onetoone.Concerts.Concert;
import onetoone.Concerts.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 *
 * @author Vivek Bengre
 *
 */

@Tag(name = "Attendee", description = "Attendee API")
@RestController
public class AttendeeController {

    @Autowired
    AttendeeRepository attendeeRepository;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    LoginInfoRepository loginInfoRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all Attendees")
    @GetMapping(path = "/attendees")
    List<Attendee> getAllAttendees(){
        return attendeeRepository.findAll();
    }

    @Operation(summary = "Get a specific Attendee by LoginInfo")
    @GetMapping(path = "/attendee/{id}")
    Attendee getAttendeeByLoginId(@PathVariable int id){
        return attendeeRepository.findByLoginInfoId(id);
    }

    @Operation(summary = "Get a specific Attendee by their ID")
    @GetMapping(path = "/attendees/{id}")
    Attendee getAttendeeById(@PathVariable int id){return attendeeRepository.findById(id);}

    @Operation(summary = "Get a specific Attendee by their name")
    @GetMapping(path = "/attendeesbyname/{name}")
    Attendee getAttendeeByName(@PathVariable String name){return attendeeRepository.findByName(name);}

    @Operation(summary = "Create a new Attendee")
    @PostMapping(path = "/attendees")
    String createAttendee(@RequestBody Attendee attendee){
        if (attendee == null)
            return failure;
        attendeeRepository.save(attendee);
        return success;
    }

    @Operation(summary = "Change a specific Attendee")
    @PutMapping("/attendees/{id}")
    Attendee updateAttendee(@PathVariable int id, @RequestBody Attendee request){
        Attendee user = attendeeRepository.findById(id);
        if(user == null)
            return null;
        if(request.getName() != null){
            user.setName(request.getName());
        }
        attendeeRepository.save(user);
        return attendeeRepository.findById(id);
    }

    @Operation(summary = "Add a Concert to an Attendee's favorites")
    @PutMapping("/attendees/{attendeeId}/concerts/{concertId}")
    String addConcertToAttendee(@PathVariable int attendeeId, @PathVariable int concertId){
        Attendee attendee = attendeeRepository.findById(attendeeId);

        Concert concert = concertRepository.findById(concertId);

        if(attendee == null || concert == null){
            return failure;
        }
        attendee.addFavorite(concert);
        concert.addAttendee(attendee);

        attendeeRepository.save(attendee);
        concertRepository.save(concert);
        return success;
    }

    @Operation(summary = "Remove a Concert to an Attendee's favorites")
    @DeleteMapping("/attendees/{attendeeId}/concerts/{concertId}")
    String deleteConcertToAttendee(@PathVariable int attendeeId, @PathVariable int concertId){
        Attendee attendee = attendeeRepository.findById(attendeeId);
        Concert concert = concertRepository.findById(concertId);

        if(attendee == null || concert == null){
            return failure;
        }
        attendee.removeFavorite(concert);
        concert.removeAttendee(attendee);

        attendeeRepository.save(attendee);
        concertRepository.save(concert);
        return success;
    }



    @Operation(summary = "Delete a specific Attendee")
    @DeleteMapping(path = "/attendees/{id}")
    String deleteAttendee(@PathVariable int id){
        Attendee attendee = attendeeRepository.findById(id);
        attendee.setFavorites(null);
        LoginInfo loginInfo = attendee.getLoginInfo();
        loginInfoRepository.deleteById(loginInfo.getId());
        attendeeRepository.deleteById(id);
        return success;
    }
}

