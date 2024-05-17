package com.example.androidexample;

import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a user with basic information.
 * This class serves as an abstract representation of a user, containing fields such as ID, name, login information, and geographic coordinates.
 * Subclasses can extend this class to provide additional functionality or specialization.
 */
public abstract class User {
    /** Unique User ID */
    public int id;
    /** User Name */
    public String name;
    /** User Login Information */
    public LoginInfo loginInfo;
}
