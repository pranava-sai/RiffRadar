package com.example.androidexample;

public class Element {
    //init vars
    String name;
    String image;
    String location;
    String description;
    String price;
    String time;
    String ages;
    String bands;
    String genre;

    //constructor w/ just name
    Element(String name){
        this.name = name;
    }

    public void addLocation(String location){
        this.location = location;
    }

    public void addTime(String time){
        this.time = time;
    }

    public void addBands(String bands){
        this.bands = bands;
    }

    public void addGenre(String genre){
        this.genre = genre;
    }
    public void addAges(String ages){
        this.ages = ages;
    }

    public void addDescription(String description){
        this.description = description;
    }
    public void addImage(String image){
        this.image = image;
    }
    public void addPrice(String price){
        this.price = price;
    }

    /* returns true if Element has an image */
    public boolean hasImage(){
        return (image != null);
    }

    /* returns true if Element has a price */
    public boolean hasPrice(){
        return (price != null);
    }

}
