package onetoone.Images;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import onetoone.Concerts.Concert;
import onetoone.Users.Band;
import onetoone.Users.Venue;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // GenerationType.IDENTITY
    private int id;
    private String filePath;

    @OneToOne
    @JsonIgnore
    private Concert concert;

    @OneToOne
    @JsonIgnore
    private Band band;

    @OneToOne
    @JsonIgnore
    private Venue venue;

    public Image() {}

    public Image(String filePath){
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setConcert(Concert concert){this.concert = concert;}

    public Concert getConcert(){return concert;}

    public void removeConcert(){this.concert = null;}

    public void setBand(Band band){this.band = band;}

    public Band getBand(){return band;}

    public void removeBand(){this.band = null;}

    public void setVenue(Venue venue){this.venue = venue;}

    public Venue getVenue(){return venue;}

    public void removeVenue(){this.venue = null;}
}

