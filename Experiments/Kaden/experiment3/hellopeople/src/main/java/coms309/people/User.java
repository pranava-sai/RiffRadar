package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class User {

    private String userName;

    private String userType;

    private String venueLocation;

    private String favoriteGenre;

    private String bandGenre;
    public User(){

    }

    /**
     * Creates a new User object
     * @param userName
     * @param userType
     * @param venueLocation
     * @param favoriteGenre
     * @param bandGenre
     */
    public User(String userName, String userType, String venueLocation, String favoriteGenre, String bandGenre){
        this.userName = userName;
        this.userType = userType;
        this.venueLocation = venueLocation;
        this.favoriteGenre = favoriteGenre;
        this.bandGenre = bandGenre;
    }

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
