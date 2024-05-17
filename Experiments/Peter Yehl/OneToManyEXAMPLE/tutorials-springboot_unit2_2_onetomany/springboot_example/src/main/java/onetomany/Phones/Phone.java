package onetomany.Phones;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetomany.Users.User;

@Entity
public class Phone {

    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String date;
    private String image;
    private String price;
    //private String location;
    private String genre;
    private String description;
    private String ages;

    //TODO change to bandsattending class (*-*)
    private String bands;

    /*
     * @ManyToOne tells springboot that multiple instances of Phone can map to one instance of OR multiple rows of the phone table can map to one user row
     * @JoinColumn specifies the ownership of the key i.e. The Phone table will contain a foreign key from the User table and the column name will be user_id
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/phone objects (phone->user->[phones]->...)
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

     // =============================== Constructors ================================== //

    public Phone(){

    }
    
    public Phone( String name, String date, String image, String price, String genre
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


    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBands(){return bands;}

    public void setBands(String bands){this.bands = bands;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
