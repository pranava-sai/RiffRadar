package onetoone.Users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import onetoone.Addresses.Address;
import onetoone.Addresses.AddressRepository;
import onetoone.Concerts.Concert;
import onetoone.Concerts.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Tag(name = "Venue", description = "Venue API")
@RestController
public class VenueController {

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    BandRespository bandRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all Venues")
    @GetMapping(path = "/venues")
    List<Venue> getAllVenues(){
        return venueRepository.findAll();
    }

    @Operation(summary = "Get a specific Venue")
    @GetMapping(path = "/venues/{id}")
    Venue getVenueById( @PathVariable int id){
        return venueRepository.findById(id);
    }

    /*@Operation(summary = "Get a list of Venues from bandId")
    @GetMapping(path = "/venues/bands/{id}")
    public Set<Band> getVenuesByBandId(@PathVariable int id){
        Venue venue = venueRepository.findById(id);
        return venue.getBands();
    }*/

    @Operation(summary = "Get a specific Venues Concerts")
    @GetMapping("/venues/{venueId}/concerts")
    public List<Concert> getConcertsByVenueId(@PathVariable int venueId){
        Venue venue = venueRepository.findById(venueId);
        return venue.getConcerts();
    }

    @Operation(summary = "Create a new Venue")
    @PostMapping(path = "/venues")
    String createVenue(@RequestBody Venue venue){
        if (venue == null)
            return failure;
        venueRepository.save(venue);
        return success;
    }

    @Operation(summary = "Change a specific Venue")
    @PutMapping("/venues/{id}")
    Venue updateVenue(@PathVariable int id, @RequestBody Venue request){
        Venue venue = venueRepository.findById(id);
        if(venue == null)
            return null;
        if(request.getName() != null) {
            venue.setName(request.getName());
        }
        if(request.getImage() != null) {
            venue.setImage(request.getImage());
        }
        if(request.getGenre() != null) {
            venue.setGenre(request.getGenre());
        }
        if(request.getDescription() != null) {
            venue.setDescription(request.getDescription());
        }
        if(request.getCapacity() != null) {
            venue.setCapacity(request.getCapacity());
        }
        if(request.getAddress() != null) {
            venue.setAddress(request.getAddress());
        }
        //NO MANY TO MANY (USE SPECIFIC PUT AND DELETE)
        /*if(request.getConcerts() != null) {
            venue.setConcerts(request.getConcerts());
        }*/
        if(request.isHasSeating() != venue.isHasSeating()) {
            venue.setHasSeating(request.isHasSeating());
        }
        //NO MANY TO MANY (USE SPECIFIC PUT AND DELETE)
        /*if(request.getBands() != null) {
            venue.setBands(request.getBands());
        }*/
        venueRepository.save(venue);
        return venueRepository.findById(id);
    }

    @Operation(summary = "Get a specific Venue from Login Info")
    @GetMapping(path = "/venue/{id}")
    Venue getAttendeeByLoginId(@PathVariable int id){
        return venueRepository.findByLoginInfoId(id);
    }

    @Operation(summary = "Assign a Venue with a specific Address")
    @PutMapping("/venues/{venueId}/addresses/{addressId}")
    String assignAddressToVenue(@PathVariable int venueId, @PathVariable int addressId){
        Venue venue = venueRepository.findById(venueId);
        Address address = addressRepository.findById(addressId);
        if(venue == null || address == null)
            return failure;
        address.setVenue(venue);
        venue.setAddress(address);
        venueRepository.save(venue);
        return success;
    }

    @Operation(summary = "Add a Band to a Venue's chat list (& vis versa)")
    @PutMapping("/venues/{venueId}/bands/{bandId}")
    String addBandToVenue(@PathVariable int venueId, @PathVariable int bandId){
        Venue venue = venueRepository.findById(venueId);

        Band band = bandRepository.findById(bandId);

        if(venue == null || band == null){
            return failure;
        }
        venue.addBand(band);
        band.addVenue(venue);

        venueRepository.save(venue);
        bandRepository.save(band);
        return success;
    }

    @Operation(summary = "Delete a Band from a Venue's chat list (& vis versa)")
    @DeleteMapping("/venues/{venueId}/bands/{bandId}")
    String deleteBandFromVenue(@PathVariable int venueId, @PathVariable int bandId){
        Venue venue = venueRepository.findById(venueId);

        Band band = bandRepository.findById(bandId);

        if(venue == null || band == null){
            return failure;
        }
        venue.removeBand(band);
        band.removeVenue(venue);

        venueRepository.save(venue);
        bandRepository.save(band);
        return success;
    }

    @Operation(summary = "Delete a specific Venue")
    @DeleteMapping(path = "/venues/{id}")
    String deleteVenue(@PathVariable int id){
        List<Concert> concerts = concertRepository.findAllByVenue_Id(id);
        if(concerts != null) {
            for (Concert concert : concerts) {
                concert.setVenue(null);
                concertRepository.save(concert);
            }
        }

        venueRepository.deleteById(id);
        return success;
    }

    @Operation(summary = "Get a list of venues by state")
    @GetMapping(path = "/venues/addresses/{state}")
    List<Venue> getAllVenuesByState(@PathVariable String state){
        List<Address> addresses = addressRepository.findAllByState(state);
        List<Venue> venues = new ArrayList<>();
        for(Address address: addresses){
            venues.addAll(venueRepository.findAllByAddress_Id(address.getId()));
        }
        return venues;
    }
}
