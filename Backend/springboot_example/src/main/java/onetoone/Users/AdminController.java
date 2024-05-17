package onetoone.Users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import onetoone.Concerts.Concert;
import onetoone.Concerts.ConcertRepository;
import onetoone.OrderSystem.OrderInfo;
import onetoone.OrderSystem.OrderInfoRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.parseFloat;

@Tag(name = "Admin", description = "Admin API")
@RestController
public class AdminController {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    LoginInfoRepository loginInfoRepository;

    @Autowired
    AttendeeRepository attendeeRepository;

    @Autowired
    BandRespository bandRespository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    OrderInfoRepository orderInfoRepository;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all admins")
    @GetMapping(path = "/admins")
    List<Admin> getAllAttendees() {
        return adminRepository.findAll();
    }

    @Operation(summary = "Get a specific Admin by their ID")
    @GetMapping(path = "/admins/{id}")
    Admin getAdminById(@PathVariable int id) {
        return adminRepository.findById(id);
    }

    @Operation(summary = "Get the amount of users")
    @GetMapping(path = "/numberofusers")
    String getNumberOfUsers() throws JSONException {
        List<Attendee> attendeeList = attendeeRepository.findAll();
        int numberOfAttendees = attendeeList.size();

        List<Band> bandList = bandRespository.findAll();
        int numberOfBands = bandList.size();

        List<Venue> venueList = venueRepository.findAll();
        int numberOfVenues = venueList.size();

        List<Concert> concertList = concertRepository.findAll();
        int numberOfConcerts = concertList.size();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Attendees", numberOfAttendees);
        jsonObject.put("Bands", numberOfBands);
        jsonObject.put("Venues", numberOfVenues);
        jsonObject.put("Concerts", numberOfConcerts);
        return jsonObject.toString();
    }

    @Operation(summary = "Get all attendee emails")
    @GetMapping(path = "/attendeedetails")
    List<String> getAttendeeEmails() {
        List<String> attendeeEmailList = new ArrayList<>();

        for(LoginInfo loginInfo: loginInfoRepository.findAll()) {
            if(loginInfo.getUserType().equals("attendee"))
                attendeeEmailList.add(loginInfo.getEmailId());
        }
        return attendeeEmailList;
    }

    @Operation(summary = "Get all band emails")
    @GetMapping(path = "/banddetails")
    List<String> getBandEmails() {
        List<String> bandEmailList = new ArrayList<>();

        for(LoginInfo loginInfo: loginInfoRepository.findAll()) {
            if(loginInfo.getUserType().equals("band"))
                bandEmailList.add(loginInfo.getEmailId());
        }
        return bandEmailList;
    }

    @Operation(summary = "Get all venue emails")
    @GetMapping(path = "/venuedetails")
    List<String> getVenueEmails() {
        List<String> venueEmailList = new ArrayList<>();

        for(LoginInfo loginInfo: loginInfoRepository.findAll()) {
            if(loginInfo.getUserType().equals("venue"))
                venueEmailList.add(loginInfo.getEmailId());
        }
        return venueEmailList;
    }

    @Operation(summary = "Get all order details")
    @GetMapping(path = "/orderdetails")
    String getOrderDetails() throws JSONException {
        int totalOrders = 0;
        float totalIncome = 0;
        int totalTickets = 0;

        for(OrderInfo order: orderInfoRepository.findAll()) {
            totalIncome += parseFloat(order.getOrderCost());
            totalOrders++;
            totalTickets += Integer.parseInt(order.getNumberOfTickets());

        }

        float avgTicketsPerOrder = (float)((totalTickets * 1.)/totalOrders);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Orders", totalOrders);
        jsonObject.put("Total Income", totalIncome);
        jsonObject.put("Number of Tickets Bought", totalTickets);
        jsonObject.put("Average Tickets Per Order", avgTicketsPerOrder);
        return jsonObject.toString();
    }





    @Operation(summary = "Get a specific Attendee by their name")
    @GetMapping(path = "/adminbyname/{name}")
    Admin getAdminByName(@PathVariable String name) {
        return adminRepository.findByName(name);
    }

    @Operation(summary = "Create a new Admin")
    @PostMapping(path = "/admins")
    String createAdmin(@RequestBody Admin admin) {
        if (admin == null)
            return failure;
        adminRepository.save(admin);
        return success;
    }

    @Operation(summary = "Change a specific admin")
    @PutMapping("/admins/{id}")
    Admin updateAdmin(@PathVariable int id, @RequestBody Admin request) {
        Admin user = adminRepository.findById(id);
        if (user == null)
            return null;
        if (request.getName() != null) {
            user.setName(request.getName());
        } if(request.getLoginInfo() != null){
           int oldId = user.getLoginInfo().getId();
            user.setLoginInfo(request.getLoginInfo());
            loginInfoRepository.deleteById(oldId);
        }
        adminRepository.save(user);
        return adminRepository.findById(id);
    }

    @Operation(summary = "Delete a specific Admin")
    @DeleteMapping(path = "/admins/{id}")
    String deleteAdmin(@PathVariable int id) {
        Admin admin = adminRepository.findById(id);
        adminRepository.deleteById(id);
        return success;
    }

}
