package storage;
import java.io.IOException;

import models.User;

/**
 * Storage class for User objects in the Flossy Personal Finance Manager system.
 * Stores all users in 'users.dat'.
 */
public class UserStorage extends UltraSimpleStorage<User> {

    /**
     * Constructs a UserStorage and loads users from 'users.dat'.
     * @throws IOException if file access fails
     * @throws ClassNotFoundException if deserialization fails
     */
    public UserStorage() throws IOException, ClassNotFoundException {
        super("users.dat");
    }

    /**
     * Gets a user by their email address.
     * @param email The email to search for
     * @return The User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        for (User user : this.storage) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}
