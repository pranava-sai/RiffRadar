package onetoone.Users;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bandName;

    private String genre;

    private String location;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bandMembers_id")
    private BandMember bandMembers;

    public Band(){

    }

    public Band(String bandName, String genre, String location, String bandMembers){
        this.bandName = bandName;
        this.genre = genre;
        this.location = location;
    }

    public String getBandName(){return this.bandName;}

    public void setBandName(String bandName){this.bandName = bandName;}

    public String getGenre(){return this.genre;}

    public void setGenre(String genre){this.genre = genre;}

    public String getLocation(){return this.location;}

    public void setLocation(String location){this.location = location;}

    public BandMember getBandMembers(){return this.bandMembers;}

    public void setBandMembers(BandMember bandMembers){this.bandMembers = bandMembers;}

    @Override
    public String toString(){
        return bandName + " "
                + genre + " "
                + location + " "
                + bandMembers;
    }
}

