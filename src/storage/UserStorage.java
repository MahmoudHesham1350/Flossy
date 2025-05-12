package storage;
import java.io.IOException;

import users.User;

public class UserStorage extends UltraSimpleStorage<User> {

     public UserStorage() throws IOException, ClassNotFoundException {
         super("users.dat");
    }

    public User getUserByEmail(String email) {
        for (User user : this.storage) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


}
