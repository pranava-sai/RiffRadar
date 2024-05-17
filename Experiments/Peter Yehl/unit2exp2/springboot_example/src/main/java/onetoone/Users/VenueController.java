package onetoone.Users;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.HashMap;

@RestController
public class VenueController {

    HashMap<String, Venue> venueList = new HashMap<>();

    //LIST OPERATION
    //returns all venues in the list
    @GetMapping("/venues")
    public @ResponseBody HashMap<String, Venue> getAllVenues(){
        return venueList;
    }

    //CREATE OPERATION
    //enters venue into the list
    @PostMapping("/venues")
    public @ResponseBody String createVenue(@RequestBody Venue venue){
        System.out.println(venue);
        venueList.put(venue.getVenueName(), venue);
        return "New venue " + venue.getVenueName() + " Saved";
    }

    //READ OPERATION
    //extracts the venue from the HashMap
    @GetMapping("/venues/{venueName}")
    public @ResponseBody Venue getVenue(@PathVariable String venueName){
        Venue v = venueList.get(venueName);
        return v;
    }

    //UPDATE OPERATION
    //extracts the venue from the HashMap and modifies it
    @PutMapping("/venues/{venueName}")
    public @ResponseBody Venue updateVenue(@PathVariable String venueName, @RequestBody Venue v){
        venueList.replace(venueName, v);
        return venueList.get(venueName);
    }

    //DELETE OPERATION
    //deletes desired venue from HashMap, returns updated list
    @DeleteMapping("/venues/{venueName}")
    public @ResponseBody HashMap<String, Venue> deleteVenue(@PathVariable String venueName){
        venueList.remove(venueName);
        return venueList;
    }
}
