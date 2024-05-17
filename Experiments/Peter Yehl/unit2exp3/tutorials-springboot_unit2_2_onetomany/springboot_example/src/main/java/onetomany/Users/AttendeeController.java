package onetomany.Users;

import onetomany.Phones.Favorites;
import onetomany.Phones.FavoritesRepository;
import onetomany.Phones.Favorites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    FavoritesRepository favoritesRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/attendees")
    List<Attendee> getAllUsers(){
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
    
   /* @PutMapping("/attendees/{attendeeId}/favorites/{favoritesId}")
    String assignFavoritesToAttendee(@PathVariable int attendeeId,@PathVariable int favoritesId){
        Attendee attendee = attendeeRepository.findById(attendeeId);
        Favorites favorites = favoritesRepository.findById(favoritesId);
        if(attendee == null || favorites == null)
            return failure;
        favorites.setAttendee(attendee);
        attendee.setFavorites(favorites);
        attendeeRepository.save(attendee);
        return success;
    }*/

    @DeleteMapping(path = "/attendees/{id}")
    String deleteAttendee(@PathVariable int id){
        attendeeRepository.deleteById(id);
        return success;
    }
}
