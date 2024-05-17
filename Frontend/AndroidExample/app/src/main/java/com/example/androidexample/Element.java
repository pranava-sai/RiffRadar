package com.example.androidexample;

/**
 * A class that contains all the information for a an element (band, concert, or venue).
 */
public class Element {
    public int id;
    public String name;
    public String image;
    public String description;
    public String streetAddress;
    public String city;
    public String state;
    public String price;
    public String time;
    public String date;
    public String ages;
    public String bands;
    public String genre;
    public String capacity;


    /**
     * Constructor that creates an element w/ an name
     * @param name the name of the element to be created
     */
    Element(String name){
        this.name = name;
    }

    /**
     * Constructor that creates an element w/ an id
     * @param id the id of the element to be created
     */
    Element(int id){
        this.id = id;
    }

    /**
     * method to add a name to the element
     * @param name name to be added
     */
    public void addName(String name){ this.name = name; }

    /**
     * method to add a streetAddress to the element
     * @param streetAddress streetAddress to be added
     */
    public void addStreetAddress(String streetAddress){ this.streetAddress = streetAddress;}

    /**
     * method to add a city to the element
     * @param city city to be added
     */
    public void addCity(String city){ this.city = city;}

    /**
     * method to add a state to the element
     * @param state state to be added
     */
    public void addState(String state){ this.state = state;}

    /**
     * method to add a time to the element
     * @param time time to be added
     */
    public void addTime(String time){ this.time = time;}

    /**
     * method to add a date to the element
     * @param date date to be added
     */
    public void addDate(String date){ this.date = date;}

    /**
     * method to add a bands to the element
     * @param bands bands to be added
     */
    public void addBands(String bands){ this.bands = bands;}

    /**
     * method to add a capacity to the element
     * @param capacity capacity to be added
     */
    public void addCapacity(String capacity) {this.capacity = capacity;}

    /**
     * method to add a genre to the element
     * @param genre genre to be added
     */
    public void addGenre(String genre){
        this.genre = genre;
    }

    /**
     * method to add a ages to the element
     * @param ages ages to be added
     */
    public void addAges(String ages){
        this.ages = ages;
    }

    /**
     * method to add a description to the element
     * @param description description to be added
     */
    public void addDescription(String description){
        this.description = description;
    }

    /**
     * method to add an image to the element
     * @param image image to be added
     */
    public void addImage(String image){
        this.image = image;
    }

    /**
     * method to price a price to the element
     * @param price price to be added
     */
    public void addPrice(String price){
        this.price = price;
    }

    /**
     * method to check if an element has an image
     */
    public boolean hasImage(){
        return (image != null);
    }

    /**
     * method to check if the element has a price
     */
    public boolean hasPrice(){
        return (price != null);
    }

}
