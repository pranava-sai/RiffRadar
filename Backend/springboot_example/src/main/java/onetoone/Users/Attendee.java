package onetoone.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import onetoone.Carts.Cart;
import onetoone.Groups.UserGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import onetoone.Concerts.Concert;
import onetoone.Payment.PaymentInfo;

import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
public class Attendee {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loginInfo_id")
    private LoginInfo loginInfo;


    @ManyToMany(mappedBy = "groupAttendees")
    Set<UserGroup> userGroups = new HashSet<>();


    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "favorites", joinColumns = @JoinColumn(name = "attendee_id"), inverseJoinColumns = @JoinColumn(name = "concert_id"))
    //@JsonIgnore
    private Set<Concert> concerts = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public Attendee(String name) {
        this.name = name;
    }

    public Attendee() {
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

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }


    public void setUserGroup(UserGroup userGroup){
        this.userGroups.add(userGroup);
    }

    public void removeFromUserGroup(UserGroup userGroup){
        this.userGroups.remove(userGroup);
    }


    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }



    //@JsonIgnore
    public Set<Concert> getFavorites() {
        return concerts;
    }

    public void setFavorites(Set<Concert> favorites){
        this.concerts = favorites;
    }
    public void addFavorite(Concert favorite){
        this.concerts.add(favorite);
    }

    public void removeFavorite(Concert favorite){this.concerts.remove(favorite);}

    public Set<Concert> getConcerts() {
        return concerts;
    }

    public void setConcerts(Set<Concert> concerts) {
        this.concerts = concerts;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}

