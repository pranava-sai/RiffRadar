package com.example.androidexample;

/**
 * A class that contains all the information for a chat preview.
 */
public class Chat {
    public int id;
    public String name;

    /**
     * Constructor that creates a chat w/ an id
     * @param id the id of the element to be created
     */
    Chat(int id){
        this.id = id;
    }

    /**
     * method to add a name to the element
     * @param name name to be added
     */
    public void addName(String name){ this.name = name; }
}
