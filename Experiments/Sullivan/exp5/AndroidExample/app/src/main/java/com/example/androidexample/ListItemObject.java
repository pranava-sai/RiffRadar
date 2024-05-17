package com.example.androidexample;

public class ListItemObject {
    private String name;
    private String price;
    private String time;

    public ListItemObject(String name, String price, String time) {
        this.name = name;
        this.price = price;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
    public String getTime() {
        return time;
    }
}
