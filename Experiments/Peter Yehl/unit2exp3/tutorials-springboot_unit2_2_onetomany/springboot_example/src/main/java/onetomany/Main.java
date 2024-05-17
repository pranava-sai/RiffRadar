package onetomany;

import java.util.Date;

import onetomany.Phones.Favorites;
import onetomany.Phones.FavoritesRepository;
import onetomany.Users.Attendee;
import onetomany.Users.AttendeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines and phones
    @Bean
    CommandLineRunner initUser(AttendeeRepository attendeeRepository, FavoritesRepository favoritesRepository) {
        return args -> {
            Attendee user1 = new Attendee("John", "john@somemail.com", "1234");
            Attendee user2 = new Attendee("Jane", "jane@somemail.com", "9898");
            Attendee user3 = new Attendee("Justin", "justin@somemail.com", "2222");

            //Laptop laptop1 = new Laptop( 2.5, 4, 8, "Lenovo", 300);
            //Laptop laptop2 = new Laptop( 4.1, 8, 16, "Hp", 800);
            //Laptop laptop3 = new Laptop( 3.5, 32, 32, "Dell", 2300);
            favoritesRepository.save(new Favorites("ACDC"));
            favoritesRepository.save(new Favorites("Beatles"));
            favoritesRepository.save(new Favorites("Cardi B"));
            //user1.setLaptop(laptop1);
            //user2.setLaptop(laptop2);
            //user3.setLaptop(laptop3);
            user1.addFavorites(favoritesRepository.findById(1));
            user2.addFavorites(favoritesRepository.findById(2));
            user3.addFavorites(favoritesRepository.findById(3));
            attendeeRepository.save(user1);
            attendeeRepository.save(user2);
            attendeeRepository.save(user3);

        };
    }

}