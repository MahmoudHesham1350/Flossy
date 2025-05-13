package models;
import java.io.IOException;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Represents a user in the Flossy Personal Finance Manager system.
 * Stores user credentials and personal information.
 */
public class User implements Serializable {
    /** The unique identifier for the user. */
    private final UUID id;
    /** The user's email address. */
    private String email;
    /** The user's username. */
    private String username;
    /** The user's password (hashed or plain, depending on implementation). */
    private String password;
    /** The user's phone number. */
    private String phoneNumber;

    /**
     * Constructs a new User with the given information.
     * @param email The user's email address
     * @param username The user's username
     * @param password The user's password
     * @param phoneNumber The user's phone number
     */
    public User (String email, String username, String password, String phoneNumber) throws ClassNotFoundException, IOException {
        this.id = UUID.randomUUID();
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Checks if the provided password matches the user's password.
     * @param password The password to check
     * @return True if the password matches, false otherwise
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Gets the user's unique identifier.
     * @return The UUID of the user
     */
    public UUID getID(){
        return this.id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets a dictionary of user information (username, phone number).
     * @return A dictionary with user info
     */
    public Dictionary<String, String> getUserInfo() {
        Dictionary<String, String> userInfo = new Hashtable<>();
        userInfo.put("email", this.email);
        userInfo.put("username", this.username);
        userInfo.put("phoneNumber", this.phoneNumber);
        return userInfo;
    }

    /**
     * Gets the user's email address.
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's username.
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's phone number.
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
