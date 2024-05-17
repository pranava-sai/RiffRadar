package com.example.androidexample;

/**
 * Represents an address with its properties such as street address, city, state, and zipcode.
 */
public class Address {
//    "id": 9,
//            "streetAddress": "40881 something St",
//            "city": "Chicago",
//            "state": "Illinois",
//            "zipcode": 50014
    /** The ID of the address. */
    public int id;
    /** The street address. */
    public String streetAddress;
    /** The city of the address. */
    public String city;
    /** The state of the address. */
    public String state;
    /** The zipcode of the address. */
    public int zipcode;

    /**
     * Returns a string representation of the address.
     *
     * @return A string representation of the address including street address, city, state, and zipcode.
     */
    public String toString() {
        return streetAddress + "\n" + city + ", " + state + "\t" + zipcode;
    }
}
