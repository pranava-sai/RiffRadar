package onetoone.Users;

import jakarta.persistence.Entity;

@Entity
public class VenueUser extends User{

    //Instance Variables
    private String address;

    //Constructors
    public VenueUser(String userName, String email, String password, String phoneNumber, String address){
        super(userName, email, password, phoneNumber);
        this.address = address;
    }

    public VenueUser(){
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }
}
