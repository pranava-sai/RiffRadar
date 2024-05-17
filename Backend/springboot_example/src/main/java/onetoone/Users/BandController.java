package onetoone.Users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import onetoone.Addresses.Address;
import onetoone.Addresses.AddressRepository;
import onetoone.Concerts.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 
 * @author Vivek Bengre
 * 
 */

@Tag(name = "Band", description = "Band API")
@RestController
public class BandController {

    @Autowired
    BandRespository bandRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    LoginInfoRepository loginInfoRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all Bands")
    @GetMapping(path = "/bands")
    List<Band> getAllBands(){
        return bandRepository.findAll();
    }

   /* @Operation(summary = "Get a list of Bands from venueId")
    @GetMapping(path = "/bands/venues/{id}")
    public Set<Venue> getBandsByVenueId(@PathVariable int id){
        Band band = bandRepository.findById(id);
        return band.getVenues();
    }*/

    @Operation(summary = "Get a specific Band")
    @GetMapping(path = "/bands/{id}")
    Band getBandById( @PathVariable int id){
        return bandRepository.findById(id);
    }

    @Operation(summary = "Get a specific Attendee")
    @GetMapping(path = "/band/{id}")
    Band getAttendeeByLoginId(@PathVariable int id){
        return bandRepository.findByLoginInfoId(id);
    }

    @Operation(summary = "Create a new Band")
    @PostMapping(path = "/bands")
    String createBand(@RequestBody Band band){
        if (band == null)
            return failure;
        bandRepository.save(band);
        return success;
    }

    @Operation(summary = "Change a specific Band")
    @PutMapping("/bands/{id}")
    Band updateBand(@PathVariable int id, @RequestBody Band request) {
        Band band = bandRepository.findById(id);
        if (band == null)
            return null;
        if (request.getName() != null) {
            band.setName(request.getName());
        }
        if (request.getImage() != null) {
            band.setImage(request.getImage());
        }
        if (request.getGenre() != null) {
            band.setGenre(request.getGenre());
        }
        if (request.getDescription() != null) {
            band.setDescription(request.getDescription());
        }
        if (request.getLoginInfo() != null) {
            band.setLoginInfo(request.getLoginInfo());
        }
        if (request.getAddress() != null) {
            band.setAddress(request.getAddress());
        }
        //NO MANY TO MANY (USE SPECIFIC PUT AND DELETE)
        /*if (request.getVenues() != null) {
            band.setVenues(request.getVenues());
        }*/
        //NO MANY TO MANY (USE SPECIFIC PUT AND DELETE)
        /*if (request.getConcertsList() != null){
            band.setConcertsList(request.getConcertsList());
        }*/
        bandRepository.save(band);
        return bandRepository.findById(id);
    }

    @Operation(summary = "Delete a specific band")
    @DeleteMapping(path = "/bands/{id}")
    String deleteBand(@PathVariable int id){
        Band band = bandRepository.findById(id);
        LoginInfo loginInfo = band.getLoginInfo();

        loginInfoRepository.deleteById(loginInfo.getId());
        bandRepository.deleteById(id);
        return success;
    }

    @Operation(summary = "Get a list of bands by state")
    @GetMapping(path = "/bands/addresses/{state}")
    List<Band> getAllBandsByState(@PathVariable String state){
        List<Address> addresses = addressRepository.findAllByState(state);
        List<Band> bands = new ArrayList<>();
        for(Address address: addresses){
            bands.addAll(bandRepository.findAllByAddress_Id(address.getId()));
        }
        return bands;
    }
}
