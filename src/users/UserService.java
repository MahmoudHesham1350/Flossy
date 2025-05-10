package users;
import service.IService;
import java.util.Dictionary;

public class UserService implements IService<User> {
    UserStorage userStorage;


    public UserService() throws Exception {
        this.userStorage = new UserStorage();
    }

    private boolean isValidEmail(String email) {
        if(this.userStorage.getUserByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        return email.contains("@");
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    @Override
    public User validate(Dictionary<String, String> data) throws IllegalArgumentException {
        String email = data.get("email");
        String username = data.get("username");
        String password = data.get("password");
        String phoneNumber = data.get("phoneNumber");

        if (!isValidEmail(email) || username == null || !isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid user data");
        }

        return new User(email, username, password, phoneNumber);
    }

    public User login(String email, String password) throws IllegalArgumentException {
        User user = userStorage.getUserByEmail(email);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        throw new IllegalArgumentException("Invalid email or password");
    }
}
