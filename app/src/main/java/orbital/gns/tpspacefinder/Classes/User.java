package orbital.gns.tpspacefinder.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class User implements Serializable {

    private String username = "";
    private String email = "";
    private String password = "";
    private String gender = "";
    public ArrayList<String> favouriteLocations = new ArrayList<>();
    public String seatTaken = "";

    public User() {

    }

    public User(String username, String email, String password, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.favouriteLocations = new ArrayList<>();
        this.seatTaken = null;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
