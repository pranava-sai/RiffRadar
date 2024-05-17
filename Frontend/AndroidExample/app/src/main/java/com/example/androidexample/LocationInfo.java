package com.example.androidexample;

import com.google.android.gms.maps.model.LatLng;

/**
 * The UserInfo class represents a singleton object managing user information in the application.
 * It holds the information of the currently logged-in user.
 */
public class LocationInfo {
    /**
     * The single instance of UserInfo.
     */
    private static LocationInfo instance = null;
    /**
     * The currently position.
     */
    public Position position;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the loggedInUser to null.
     */
    private LocationInfo() {
        position = new Position();
    }

    /**
     * Returns the single instance of UserInfo.
     * @return The singleton instance of UserInfo.
     */
    public static synchronized LocationInfo getInstance() {
        if(instance == null) {
            instance = new LocationInfo();
        }
        return instance;
    }

    /**
     * Resets the singleton instance of UserInfo to null.
     * This is useful for resetting the user information, typically during logout.
     */
    public static synchronized void resetInstance() {
        instance = null;
    }

    /**
     * Checks if an instance of UserInfo exists.
     * @return true if an instance of UserInfo exists, otherwise false.
     */
    public static boolean hasInstance() {
        return instance != null;
    }

    public class Position {
        /** Geographic Coordinates - latitudes and longitudes */
        public LatLng latLng;
        /** Area to search - radius in km */
        public int radius;
    }
}
