package onetoone.Users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import onetoone.Addresses.Address;
import onetoone.Concerts.Concert;
import onetoone.Images.Image;
//import onetoone.Messages.MessengersKey;
//import onetoone.Messages.MessengersKey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
@Table(name = "Band")
public class Band {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    private String genre;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(mappedBy = "bandChats")
    //@JsonIgnoreProperties("concerts")
    private Set<Venue> venueChats = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    public Image actImage;

    @ManyToMany(mappedBy = "bandsList")
    //@JsonIgnoreProperties("concerts")
    private Set<Concert> concertsList = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loginInfo_id")
    private LoginInfo loginInfo;

    public Band(String name, String image, String genre, String description) {
        this.name = name;
        this.image = image;
        this.genre = genre;
        this.description = description;
        //this.venueChats = new ArrayList<>();
    }

    public Band() {
        //this.venueChats = new ArrayList<>();

    }

    // =============================== Getters and Setters for each field ================================== //

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImageURL(String image){
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getGenre(){
        return genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    //@JsonIgnore
    @JsonIgnoreProperties({"concerts", "bands"})
    public Set<Venue> getVenues() {
        return venueChats;
    }

    public void setVenues(Set<Venue> venueChats){
        this.venueChats = venueChats;
    }

    public void addVenue(Venue venue){
        this.venueChats.add(venue);
    }

    public void removeVenue(Venue venue){this.venueChats.remove(venue);}

    @JsonIgnoreProperties({"concerts", "bandsList"})
    public Set<Concert> getConcertsList() {
        return concertsList;
    }

    public void setConcertsList(Set<Concert> concertsList){
        this.concertsList = concertsList;
    }

    public void addConcertsList(Concert concert){
        this.concertsList.add(concert);
    }

    public void removeConcert(Concert concert){this.concertsList.remove(concert);}

    public void setActImage(Image image){this.actImage = actImage;}

    public Image getActImage(){return actImage;}

    public void removeImage(){actImage = null;}
}
