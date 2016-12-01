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

    public User(String pUserID, String pUsername, String pEmail, String pImage) {
        this.userID = pUserID;
        this.username = pUsername;
        this.email = pEmail;
        this.image = pImage;
    }
}
