package onetoone.Carts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import onetoone.OrderSystem.OrderInfo;
import onetoone.Payment.PaymentInfo;
import onetoone.Users.Attendee;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String concertName;

    private String numberOfTickets;


    private String pricePerTicket;

    @OneToOne(mappedBy = "cart")
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Attendee attendee;


    public Cart(String concertName, String numberOfTickets, String pricePerTicket) {
        this.concertName = concertName;
        this.numberOfTickets = numberOfTickets;
        this.pricePerTicket = pricePerTicket;
    }

    public Cart() {
    }

    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }


    public String getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(String numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public String getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(String pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }
}
