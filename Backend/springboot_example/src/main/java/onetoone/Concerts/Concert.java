package onetoone.Concerts;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import onetoone.Addresses.Address;
import onetoone.Images.Image;
import onetoone.Users.Attendee;
import onetoone.Users.Band;
import onetoone.Users.Venue;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Concert {
    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String date;
    private String image;
    private String price;
    //private String location;
    private String genre;
    private String description;
    private String ages;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    public Image actImage;

    //TODO change to bandsattending class (*-*)
    private String bands;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonIgnoreProperties({"concerts", "bands"})
    private Venue venue;

    @ManyToMany(mappedBy = "concerts")
    //@JsonIgnoreProperties("concerts")
    @JsonIgnore
    private Set<Attendee> attendees = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "concert_band_list", joinColumns = @JoinColumn(name = "concert_id"), inverseJoinColumns = @JoinColumn(name = "band_id"))
    //@JsonIgnoreProperties("venues")
    private Set<Band> bandsList = new HashSet<>();

    public Concert(String name, String date, String image, String price, String genre
                 , String description, String ages, String bands) {
        this.name = name;
        this.date = date;
        this.image = image;
        this.price = price;
       // this.location = location;
        this.genre = genre;
        this.description = description;
        this.ages = ages;
        this.bands = bands;

    }

    public Concert() {
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

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getPrice(){return price;}

    public void setPrice(String price){this.price = price;}

    /*public String getLocation(){return location;}

    public void setLocation(String location){this.location = location;}
*/
    public String getGenre(){return genre;}

    public void setGenre(String genre){this.genre = genre;}
    public String getDescription(){return description;}

    public void setDescription(String description){this.description = description;}

    public String getAges(){return ages;}

    public void setAges(String ages){this.ages = ages;}

    public String getBands(){
        return bands;}

    public void setBands(String bands){this.bands = bands;}

    public Venue getVenue(){return venue;}

    public void setVenue(Venue venue){this.venue = venue;}

    //@JsonIgnore
    public Set<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<Attendee> attendees){
        this.attendees = attendees;
    }

    public void addAttendee(Attendee attendee){
        this.attendees.add(attendee);
    }

    public void removeAttendee(Attendee attendee){this.attendees.remove(attendee);}

    @JsonIgnoreProperties({"concertsList", "venues"})
    public Set<Band> getBandsList() {
        return bandsList;
    }

    public void setBandsList(Set<Band> bandsList){
        this.bandsList = bandsList;
    }

    public void addBand(Band band){
        this.bandsList.add(band);
    }

    public void removeBand(Band band){this.bandsList.remove(band);}

    public void setActImage(Image image){this.actImage = image;}

    public Image getActImage(){return actImage;}

    public void removeImage(){actImage = null;}

}