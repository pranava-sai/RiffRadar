package onetoone.Addresses;

//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
import onetoone.Users.Venue;
import onetoone.Users.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;


//@Tag(name = "Address", description = "Address API")
@RestController
public class AddressController {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    VenueRepository venueRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


//    @Operation(summary = "Get all addresses")
    @GetMapping(path = "/addresses")
    List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }


//    @Operation(summary = "Get specific address")
    @GetMapping(path = "/addresses/{id}")
    Address getAddressById( @PathVariable int id){
        return addressRepository.findById(id);
    }



//    @Operation(summary = "Create new address")
    @PostMapping(path = "/addresses")
    String createAddress(@RequestBody Address address){
        if (address == null)
            return failure;
        addressRepository.save(address);
        return success;
    }


//    @Operation(summary = "Change a specific address")
    @PutMapping("/addresses/{id}")
    Address updateAddress(@PathVariable int id, @RequestBody Address request){
        Address user = addressRepository.findById(id);
        if(user == null)
            return null;
        addressRepository.save(request);
        return addressRepository.findById(id);
    }


//    @Operation(summary = "delete a specific address")
    @DeleteMapping(path = "/addresses/{id}")
    String deleteAddress(@PathVariable int id){
        Venue venue = venueRepository.findByAddress_Id(id);
        venue.setAddress(null);
        venueRepository.save(venue);

        addressRepository.deleteById(id);
        return success;
    }
}
