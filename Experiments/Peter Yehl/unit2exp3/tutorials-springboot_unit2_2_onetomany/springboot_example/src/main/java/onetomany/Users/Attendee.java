package onetomany.Users;

import onetomany.Phones.Favorites;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Attendee {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;

    private String password;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User), the cascade option tells springboot
     * to create the child entity if not present already (in this case it is laptop)
     * @JoinColumn specifies the ownership of the key i.e. The User table will contain a foreign key from the laptop table and the column name will be laptop_id
     */
    /*@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "favorites_id")
    private Laptop laptop;*/

     /*
     * @OneToMany tells springboot that one instance of User can map to multiple instances of Phone OR one user row can map to multiple rows of the phone table
     */
    @OneToMany
    private List<Favorites> favorites;

     // =============================== Constructors ================================== //


    public Attendee(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        favorites = new ArrayList<>();
    }

    public Attendee() {
        favorites = new ArrayList<>();
    }

    
    // =============================== Getters and Setters for each field ================================== //


    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){this.email = email;}

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public List<Favorites> getFavorites() {return favorites;}

    public void setFavorites(List<Favorites> favorites) {
        this.favorites = favorites;
    }

    public void addFavorites(Favorites favorites){this.favorites.add(favorites);}
    
}
