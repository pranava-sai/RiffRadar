package onetoone.OrderSystem;


import jakarta.persistence.*;
import onetoone.Payment.PaymentInfo;
import onetoone.Users.LoginInfo;


/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paymentInfo_id")
    private PaymentInfo paymentInfo;

    private String concertName;
    private String username;

    private String email;

    private String orderNumber;

    private String numberOfTickets;

    private String orderCost;

    private String orderDate;

    private String orderTime;


    public OrderInfo(String concertName, String username, String email, String orderNumber, String numberOfTickets, String orderCost, String orderDate, String orderTime) {
        this.concertName = concertName;
        this.username = username;
        this.email = email;
        this.orderNumber = orderNumber;
        this.numberOfTickets = numberOfTickets;
        this.orderCost = orderCost;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public OrderInfo() {
    }

    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(String numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = orderCost;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }
}
