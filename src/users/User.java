package users;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public class User implements Serializable {
    private String email;
    private String username;
    private String password;
    private String phoneNumber;

    public User (String email, String username, String password, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return email;
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

    public Dictionary<String, String> getUserInfo() {
        Dictionary<String, String> userInfo = new Hashtable<>();
        userInfo.put("email", this.email);
        userInfo.put("username", this.username);
        userInfo.put("phoneNumber", this.phoneNumber);
        return userInfo;
    }


}
