package users;

import storage.UltraSimpleStorage;

import java.io.IOException;
import java.util.List;

public class UserStorage extends UltraSimpleStorage<User> {

    List<User> users;

     public UserStorage() throws IOException, ClassNotFoundException {
         super("users.dat");
         this.users = this.load();
    }

    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


}
