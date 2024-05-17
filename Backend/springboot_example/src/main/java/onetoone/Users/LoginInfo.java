package onetoone.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysql.cj.log.Log;
import jakarta.persistence.*;


/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
public class LoginInfo {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String emailId;
    private String password;
    private String userType;

    @OneToOne(mappedBy = "loginInfo")
    @JoinColumn(name = "loginInfo_id")
    @JsonIgnore
    private Attendee attendee;

    @OneToOne(mappedBy = "loginInfo")
    @JoinColumn(name = "loginInfo_id")
    @JsonIgnore
    private Venue venue;

    @OneToOne(mappedBy = "loginInfo")
    @JoinColumn(name = "loginInfo_id")
    @JsonIgnore
    private Band band;


    public LoginInfo(String emailId, String password, String userType) {
        this.emailId = emailId;
        this.password = password;
        this.userType = userType;

    }

    public LoginInfo() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }


    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId = emailId;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}

