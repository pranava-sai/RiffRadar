package onetoone;

import onetoone.Users.Attendee;
import onetoone.Users.AttendeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import onetoone.Users.User;
import onetoone.Users.UserRepository;
import onetoone.Users.Band;
import onetoone.Users.BandRespository;
/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@SpringBootApplication
@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     * 
     * @param userRepository repository for the User entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, AttendeeRepository attendeeRepository, BandRespository bandRespository) {
        return args -> {
            User user1 = new User("John", "john@somemail.com");
            User user2 = new User("Jane", "jane@somemail.com");
            User user3 = new User("Justin", "justin@somemail.com");

            Attendee user4 = new Attendee("kadenb", "password", "kadenb@iastate.edu");

            Band user8 = new Band("Soundgarden", "http", "password2", "soundgarden@gmail.com", "82", "-30","Grunge", "from seattle" );

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            bandRespository.save(user8);

        };
    }

}
