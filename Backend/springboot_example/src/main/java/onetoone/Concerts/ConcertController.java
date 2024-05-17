package onetoone.Concerts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import onetoone.Addresses.Address;
import onetoone.Addresses.AddressRepository;
import onetoone.Images.Image;
import onetoone.Images.ImageRepository;
import onetoone.Users.Band;
import onetoone.Users.BandRespository;
import onetoone.Users.Venue;
import onetoone.Users.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Tag(name = "Concert", description = "Concert API")
@RestController
public class ConcertController {

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    BandRespository bandRespository;

    @Autowired
    AddressRepository addressRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all concerts")
    @GetMapping(path = "/concerts")
    List<Concert> getAllConcerts(){return concertRepository.findAll();}

    @Operation(summary = "Get a specific concert")
    @GetMapping(path = "/concerts/{id}")
    Concert getConcertById( @PathVariable int id){
        return concertRepository.findById(id);
    }

    @Operation(summary = "Get a specific concert")
    @GetMapping(path = "/concertbyname/{name}")
    Concert getConcertByName( @PathVariable String name){
        return concertRepository.findByName(name);
    }



    @Operation(summary = "Get a list of concerts by date")
    @GetMapping(path = "/concerts/dates/{date}")
    List<Concert> getAllConcertsByDate(@PathVariable String date){return concertRepository.findAllByDate(date);}

    @Operation(summary = "Get a list of concerts by specific band")
    @GetMapping(path = "/concerts/bands/{id}")
    Set<Concert> getAllConcertsByBand(@PathVariable int id){return bandRespository.findById(id).getConcertsList();}

    @Operation(summary = "Get a list of concerts by state")
    @GetMapping(path = "/concerts/addresses/{state}")
    List<Concert> getAllConcertsByState(@PathVariable String state){
        List<Address> addresses = addressRepository.findAllByState(state);
        List<Venue> venues = new ArrayList<>();
        List<Concert> concerts = new ArrayList<>();
        for(Address address: addresses){
            venues.addAll(venueRepository.findAllByAddress_Id(address.getId()));
        }
        for(Venue venue: venues){
            concerts.addAll(concertRepository.findAllByVenue_Id(venue.getId()));
        }
        return concerts;
    }

    @Operation(summary = "Create a concert")
    @PostMapping(path = "/concerts")
    String createConcert(@RequestBody Concert concert){
        if (concert == null)
            return failure;
        concertRepository.save(concert);
        int i = concert.getId();
        return "{\"id\":\"" + i + "\"}";
    }

    @Operation(summary = "Change a specific concert")
    @PutMapping("/concerts/{id}")
    Concert updateConcert(@PathVariable int id, @RequestBody Concert request){
        Concert concert = concertRepository.findById(id);
        if(concert == null)
            return null;
        if(request.getName() != null) {
            concert.setName(request.getName());
        }
        if(request.getDate() != null) {
            concert.setDate(request.getDate());
        }
        if(request.getImage() != null) {
            concert.setImage(request.getImage());
        }
        if(request.getPrice() != null) {
            concert.setPrice(request.getPrice());
        }
        if(request.getGenre() != null) {
            concert.setGenre(request.getGenre());
        }
        if(request.getDescription() != null) {
            concert.setDescription(request.getDescription());
        }
        if(request.getAges() != null) {
            concert.setAges(request.getAges());
        }
        if(request.getBands() != null) {
            concert.setBands(request.getBands());
        }
        if(request.getVenue() != null) {
            concert.setVenue(request.getVenue());
        }

        concertRepository.save(concert);
        return concertRepository.findById(id);
    }

    @Operation(summary = "Add a Concert to a Venue's concert list")
    @PutMapping("/venues/{venueId}/concerts/{concertId}")
    String addConcertToVenue(@PathVariable int venueId, @PathVariable int concertId){
        Venue venue = venueRepository.findById(venueId);

        Concert concert = concertRepository.findById(concertId);

        if(venue == null || concert == null){
            return failure;
        }
        venue.addConcerts(concert);
        concert.setVenue(venue);

        venueRepository.save(venue);
        concertRepository.save(concert);
        return success;
    }

    @Operation(summary = "Remove a Concert from a Venue's concert list")
    @DeleteMapping("/venues/{venueId}/concerts/{concertId}")
    String RemoveConcertFromVenue(@PathVariable int venueId, @PathVariable int concertId){
        Venue venue = venueRepository.findById(venueId);

        Concert concert = concertRepository.findById(concertId);

        if(venue == null || concert == null){
            return failure;
        }
        venue.removeConcerts(concert);
        concert.setVenue(null);

        venueRepository.save(venue);
        concertRepository.save(concert);
        return success;
    }

    @Operation(summary = "Add a Band to a Concerts's list (& vis versa)")
    @PutMapping("/concerts/{concertId}/bands/{bandId}")
    String addBandToConcert(@PathVariable int concertId, @PathVariable int bandId){
        Concert concert = concertRepository.findById(concertId);

        Band band = bandRespository.findById(bandId);

        if(concert == null || band == null){
            return failure;
        }
        concert.addBand(band);
        band.addConcertsList(concert);

        concertRepository.save(concert);
        bandRespository.save(band);
        return success;
    }

    @Operation(summary = "Delete a Band from a Concert's list (& vis versa)")
    @DeleteMapping("/concerts/{concertId}/bands/{bandId}")
    String deleteBandFromVenue(@PathVariable int concertId, @PathVariable int bandId){
        Concert concert = concertRepository.findById(concertId);

        Band band = bandRespository.findById(bandId);

        if(concert == null || band == null){
            return failure;
        }
        concert.removeBand(band);
        band.removeConcert(concert);

        concertRepository.save(concert);
        bandRespository.save(band);
        return success;
    }

    @Operation(summary = "delete a specific concert")
    @DeleteMapping(path = "/concerts/{id}")
    String deleteConcert(@PathVariable int id){
        concertRepository.deleteById(id);
        return success;
    }
}
