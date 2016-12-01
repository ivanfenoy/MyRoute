package ar.com.ivanfenoy.myroute.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.ivanfenoy.myroute.App;
import ar.com.ivanfenoy.myroute.interfaces.ListCallback;
import ar.com.ivanfenoy.myroute.model.Trip;
import ar.com.ivanfenoy.myroute.model.User;

/**
 * Created by ivanj on 29/10/2016.
 */

public class DataBaseConnection {
    private DatabaseReference mDatabase;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_TRIPS = "trips";

    private void checkInstance(){
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        if(App.mUser == null){
            App.sentToLogin();
            return;
        }
    }

    public void saveNewUser(String userID, User user) {
        checkInstance();
        mDatabase.child(TABLE_USERS).child(userID).setValue(user);
    }

    public void saveNewTrip(Trip pTrip){
        checkInstance();
        mDatabase.child(TABLE_TRIPS).child(App.mUser.userID).push().setValue(pTrip);
    }

    public void updateTrip(Trip pTrip){
        checkInstance();
        mDatabase.child(TABLE_TRIPS).child(App.mUser.userID).child(pTrip.id).setValue(pTrip);
    }

    public void deleteTrip(Trip pTrip){
        checkInstance();
        mDatabase.child(TABLE_TRIPS).child(App.mUser.userID).child(pTrip.id).removeValue();
    }

    public void getTrips(final ListCallback callback){
        checkInstance();
        mDatabase.child(TABLE_TRIPS).child(App.mUser.userID).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Trip> trips = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Trip wTrip = data.getValue(Trip.class);
                            wTrip.id = data.getKey();
                            trips.add(wTrip);
                        }
                        callback.done(null, trips);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.done(databaseError.toException(), null);
                    }
                });
    }
}
