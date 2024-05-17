package onetoone.Users;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class BandMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String instrument;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "band_id")
    private Band band;

    public BandMember(){

    }

    public BandMember(String name, String instrument){
        this.name = name;
        this.instrument = instrument;
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getName(){return this.name;}

    public void setName(String name){this.name = name;}

    public String getInstrument(){return this.instrument;}

    public void setInstrument(String instrument){this.instrument = instrument;}

    public Band getBand(){return band;}

    public void setBand(Band band){this.band = band;}

}

