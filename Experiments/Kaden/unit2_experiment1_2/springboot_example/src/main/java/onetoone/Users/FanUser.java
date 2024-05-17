package onetoone.Users;

import jakarta.persistence.Entity;

@Entity
public class FanUser extends User{

    //Instance Variables
    private String favoriteGenre;

    //Constructors
    public FanUser(String userName, String email, String password, String phoneNumber, String favoriteGenre){
        super(userName, email, password, phoneNumber);
        this.favoriteGenre = favoriteGenre;
    }

    public FanUser(){
    }

    public String getFavoriteGenre(){
        return this.favoriteGenre;
    }

    public void setFavoriteGenre(String favoriteGenre){
        this.favoriteGenre = favoriteGenre;
    }
}
