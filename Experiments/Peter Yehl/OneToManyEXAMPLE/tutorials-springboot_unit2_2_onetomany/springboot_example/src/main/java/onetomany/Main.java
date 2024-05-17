package onetomany;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetomany.Laptops.Laptop;
import onetomany.Laptops.LaptopRepository;
import onetomany.Phones.Phone;
import onetomany.Phones.PhoneRepository;
import onetomany.Users.User;
import onetomany.Users.UserRepository;

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines and phones
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, LaptopRepository laptopRepository, PhoneRepository phoneRepository) {
        return args -> {
            User user1 = new User("Ahole", "@gmail", "http//", "rock", "fun", "500");
            User user2 = new User("ames", "@gmail", "http//", "rock", "fun", "500");
            User user3 = new User("mshop", "@gmail", "http//", "rock", "fun", "500");
            Laptop laptop1 = new Laptop( 2.5, 4, 8, "Lenovo", 300);
            Laptop laptop2 = new Laptop( 4.1, 8, 16, "Hp", 800);
            Laptop laptop3 = new Laptop( 3.5, 32, 32, "Dell", 2300); 
            for(int i=6; i<13; i++)
                phoneRepository.save(new Phone("lolapalooza", "02/09/2008", "http", "$500", "indie", "2 days", "16+", "ACDC, DEF LEPP"));
            user1.setLaptop(laptop1);
            user2.setLaptop(laptop2);
            user3.setLaptop(laptop3);
            user1.addPhones(phoneRepository.findById(1));
            user1.addPhones(phoneRepository.findById(2));            
            user1.addPhones(phoneRepository.findById(6));
            user2.addPhones(phoneRepository.findById(3));
            user2.addPhones(phoneRepository.findById(4)); 
            user3.addPhones(phoneRepository.findById(5));
            user3.addPhones(phoneRepository.findById(7));
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        };
    }

}