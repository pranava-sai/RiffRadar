package onetoone.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import onetoone.Addresses.Address;
import onetoone.Concerts.Concert;
import onetoone.Images.Image;
//import onetoone.Messages.MessengersKey;
//import onetoone.Messages.MessengersKey;

import java.util.*;


/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
@Table(name = "Venue")
public class Venue {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String image;
    private String genre;
    private String description;
    private String capacity;

    private boolean hasSeating;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "venue_band_chats", joinColumns = @JoinColumn(name = "venue_id"), inverseJoinColumns = @JoinColumn(name = "band_id"))
    //@JsonIgnoreProperties("venues")
    private Set<Band> bandChats = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    public Image actImage;

    @OneToMany//(mappedBy = "venue")
    //@JsonIgnore
    @JsonIgnoreProperties("venue")
    private List<Concert> concerts;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loginInfo_id")
    private LoginInfo loginInfo;

    public Venue(String name, String capacity, String description,
                 String image, String genre, boolean hasSeating) {
        this.name = name;
        this.capacity = capacity;
        this.description = description;
        this.genre = genre;
        this.image = image;
        this.hasSeating = hasSeating;
        concerts = new ArrayList<>();
        //bandChats = new Set<>();
    }

    public Venue() {
        concerts = new ArrayList<>();

    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImage(){return image;}

    public void setImage(String image){this.image = image;}

    public String getGenre(){return genre;}

    public void setGenre(String genre){this.genre = genre;}

    public String getDescription(){return description;}

    public void setDescription(String description){this.description = description;}

    public String getCapacity(){return capacity;}

    public void setCapacity(String capacity){this.capacity = capacity;}

    public Address getAddress(){
        return address;
    }

    public void setAddress(Address address){
        this.address = address;
    }

    public List<Concert> getConcerts() {
        return concerts;
    }

    public void setConcerts(List<Concert> concerts) {
        this.concerts = concerts;
    }

    public void addConcerts(Concert concert){
        this.concerts.add(concert);
    }

    public void removeConcerts(Concert concert){this.concerts.remove(concert);}

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public boolean isHasSeating() {
        return hasSeating;
    }

    public void setHasSeating(boolean hasSeating) {
        this.hasSeating = hasSeating;
    }

    @JsonIgnoreProperties({"concerts", "venues"})
    public Set<Band> getBands() {
        return bandChats;
    }

    public void setBands(Set<Band> bandChats){
        this.bandChats = bandChats;
    }
    public void addBand(Band band){
        this.bandChats.add(band);
    }

    public void removeBand(Band band){this.bandChats.remove(band);}

    public void setActImage(Image image){this.actImage = actImage;}

    public Image getActImage(){return actImage;}

    public void removeImage(){actImage = null;}

}
