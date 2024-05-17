package onetoone.Addresses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import onetoone.Users.Venue;
import onetoone.Users.*;

/**
 * @author pmyehl
 */
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String streetAddress;
    private String city;
    private String state;
    private int zipcode;

    @OneToOne
    @JsonIgnore
    private Venue venue;

    @OneToOne
    @JsonIgnore
    private Band band;

    public Address(String streetAddress, String city, String state, int zipcode){
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public Address(){
    }

    //GETTERS AND SETTERS


    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getStreetAddress(){
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress){
        this.streetAddress = streetAddress;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public int getZipcode(){
        return zipcode;
    }

    public void setZipcode(int zipcode){
        this.zipcode = zipcode;
    }

    public Venue getVenue(){
        return venue;
    }

    public void setVenue(Venue venue){
        this.venue = venue;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}
