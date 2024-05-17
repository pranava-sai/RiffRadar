package onetoone;

import onetoone.Addresses.Address;
import onetoone.Concerts.Concert;
import onetoone.Concerts.ConcertRepository;
//import onetoone.Messages.MessengersKey;
import onetoone.Users.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.List;


/**
 * 
 * @author Vivek Bengre
 * 
 */

@OpenAPIDefinition(info = @Info(title = "My API", version = "1.0", description = "API for My Application"))
@SpringBootApplication
@EnableJpaRepositories
@EnableAsync(proxyTargetClass = true)
class Main {

    public static void main(String[] args) {SpringApplication.run(Main.class, args);}

    // Create 3 users with their machines
    /**
     *
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser( AttendeeRepository attendeeRepository, VenueRepository venueRepository, BandRespository bandRespository, ConcertRepository concertRepository) {
        return args -> {
            //User user1 = new User("John", "john@somemail.com");
            //User user2 = new User("Jane", "jane@somemail.com");
            //User user3 = new User("Justin", "justin@somemail.com");

            //Attendee user4 = new Attendee("logintest4", "password4", "logintest4@iastate.edu");

            /*Venue venue3 = new Venue("MShop", "500", "Memorial Union Concert Event!", "password", "http://image", false);
            Venue venue4 = new Venue("AHOLE", "500",  "Memorial Union Concert Event!", "password", "http://image", true);
            Venue venue5 = new Venue("MShop", "500", "Memorial Union Concert Event!", "password", "http://image", true);
            Band band1 = new Band("soundgarden", "https:", "rock", "test From Seattle");
            Band band2 = new Band("acdc", "https:",  "Grunge", "test From Seattle");
            Band band3 = new Band("pierce the veil", "https:", "Pop Punk", "test From Seattle");

           // List<Venue> venues = Arrays.asList(venue3, venue4, venue5);
            //List<Band> bands = Arrays.asList(band1, band2, band3);

            venueRepository.save(venue3);
            venueRepository.save(venue4);
            venueRepository.save(venue5);
            bandRespository.save(band1);
            bandRespository.save(band2);
            bandRespository.save(band3);

            venue3.addBand(band2);
            venue4.addBand(band1);
            venue5.addBand(band3);

            venueRepository.save(venue3);
            venueRepository.save(venue4);
            venueRepository.save(venue5);*/



            //Venue user7 = new Venue("CEFCU ARENA", "100", "cefcu@gmail.com", "CEFCU CONCERT EVENT!",
             //        "1234","http://image", "Metal");
            //Address address1 = new Address("2660 College AVE", "Normal", "Illinois", 61761);

            //Venue user8 = new Venue("Hilton Coliseum", "14000", "hilton@gmail.com", "Hilton CONCERT EVENT!",
            //        "1234","http://image", "Metal");

            //Concert concert1 = new Concert("Lolapalooza", "02/28/2024", "http://image", "$20","BIG SHOW!!!","18+", "ACDC, CREAM");

            //user7.setAddress(address1);
            //venueRepository.save(user7);

            //concert1.setVenue(user7);
            // concertRepository.save(concert1);

            //userRepository.save(user1);
            //userRepository.save(user2);
            //userRepository.save(user3);
            //attendeeRepository.save(user4);
            //venueRepository.save(user5);
            //bandRespository.save(user6);

            //attendeeRepository.save(user4);


            //MessengersKey chat = new MessengersKey(1);
            //Band band = new Band("name", "image", "genre","description", "chat");
            //bandRespository.save(band);



        };
    }

}
