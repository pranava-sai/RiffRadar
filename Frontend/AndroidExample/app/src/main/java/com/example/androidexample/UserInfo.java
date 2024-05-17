package com.example.androidexample;

/**
 * The UserInfo class represents a singleton object managing user information in the application.
 * It holds the information of the currently logged-in user.
 */
public class UserInfo {
    /**
     * The single instance of UserInfo.
     */
    private static UserInfo instance = null;
    /**
     * The currently logged-in user.
     */
    public User loggedInUser;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the loggedInUser to null.
     */
    private UserInfo() {
        loggedInUser = null;
    }

    /**
     * Returns the single instance of UserInfo.
     * @return The singleton instance of UserInfo.
     */
    public static synchronized UserInfo getInstance() {
        if(instance == null) {
            instance = new UserInfo();
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
}
