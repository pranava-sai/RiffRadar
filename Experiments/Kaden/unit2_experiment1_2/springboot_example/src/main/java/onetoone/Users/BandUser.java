package onetoone.Users;

import jakarta.persistence.Entity;
@Entity
public class BandUser extends User{

    //Instance Variables
    private String genre;

    //Constructors

    public BandUser(String userName, String email, String password, String phoneNumber, String genre){
        super(userName, email, password, phoneNumber);
        this.genre = genre;
    }

    public BandUser(){
    }

    public String getGenre(){
        return this.genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }
}
