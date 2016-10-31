package ar.com.ivanfenoy.myroute.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ar.com.ivanfenoy.myroute.model.User;

/**
 * Created by ivanj on 29/10/2016.
 */

public class DataBaseConnection {
    private DatabaseReference mDatabase;

    private void checkInstance(){
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }

    public void saveNewUser(String userID, User user) {
        checkInstance();
        mDatabase.child("users").child(userID).setValue(user);
    }
}
