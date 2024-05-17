package onetomany.Phones;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onetomany.Users.Attendee;

import javax.persistence.*;

@Entity
public class Favorites {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String concert; //UPDATE WITH ACTUAL CONCERT TABLE

    /*
     * @ManyToOne tells springboot that multiple instances of Phone can map to one instance of OR multiple rows of the phone table can map to one user row
     * @JoinColumn specifies the ownership of the key i.e. The Phone table will contain a foreign key from the User table and the column name will be user_id
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/phone objects (phone->user->[phones]->...)
     */
    @ManyToOne
    @JoinColumn(name = "attendee_id")
    @JsonIgnore
    private Attendee attendee;

     // =============================== Constructors ================================== //

    public Favorites(){

    }

    public Favorites(String concert) {
        this.concert = concert;
    }


    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConcert() {
        return concert;
    }

    public void setConcert(String concert) {this.concert = concert;}

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }


}
