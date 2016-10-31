package ar.com.ivanfenoy.myroute.model;

/**
 * Created by ivanj on 29/10/2016.
 */

public class User {
    public String userID;
    public String username;
    public String email;
    public String image;

    public User() {
    }

    public User(String username, String email, String image) {
        this.username = username;
        this.email = email;
        this.image = image;
    }
}
