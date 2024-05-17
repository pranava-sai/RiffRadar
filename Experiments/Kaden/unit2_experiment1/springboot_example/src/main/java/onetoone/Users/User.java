package onetoone.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@Entity
public class User {

     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userName;

    private String userType;

    private String venueLocation;

    private String favoriteGenre;

    private String bandGenre;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "laptop_id")
//    private Laptop laptop;

    public User(String userName, String userType, String venueLocation, String favoriteGenre, String bandGenre) {
        this.userName = userName;
        this.userType = userType;
        this.venueLocation = venueLocation;
        this.favoriteGenre = favoriteGenre;
        this.bandGenre = bandGenre;
    }

    public User() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getVenueLocation() {
        return this.venueLocation;
    }

    public void setVenueLocation(String venueLocation) {
        this.venueLocation = venueLocation;
    }

    public String getBandGenre() {
        return this.bandGenre;
    }

    public void setBandGenre(String bandGenre) { this.bandGenre = bandGenre; }

    public String getFavoriteGenre() {
        return this.favoriteGenre;
    }

    public void setFavoriteGenre(String favouriteGenre) {
        this.favoriteGenre = favouriteGenre;
    }

    @Override
    public String toString() {
        //Returns specific info based on user type, null if userType is not specified on user creation
        if(userType.contains("Venue")) {
            return "Name: " + userName + "\n" +
                    "User Type: " + userType + "\n" +
                    "Venue Location: "        + venueLocation + " ";
        } else if(userType.contains("Band")) {
            return "Name: " + userName + "\n" +
                    "User Type: "       + userType + "\n" +
                    "Band Genre: "       + bandGenre + " ";
        } else if(userType.contains("Fan")){
            return "Name: " + userName + "\n" +
                    "User Type: "       + userType + "\n" +
                    "Favorite Genre: "      + favoriteGenre + " ";
        } else {
            return null;
        }
    }
    
}
