package dk.au.mad22spring.AppProject.Group13.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDB {
    private static final String TAG = "FireBaseDB TAG";
    public FirebaseDatabase database;
    private DatabaseReference userCloudEndPoint;
    private DatabaseReference friendsCloudEndPoint;

    public FirebaseDB(){
        database = FirebaseDatabase.getInstance();
        userCloudEndPoint = database.getReference("Users");
        friendsCloudEndPoint = database.getReference("Friends");
    }

    public void addUser(User user){
        if(user != null){
            userCloudEndPoint.child(user.id).setValue(user);
            friendsCloudEndPoint.child(user.id).setValue(user.id);

        }
    }

    public void setLocation(String userId, String location1, String location2){
        userCloudEndPoint.child(userId).child("location1").setValue(location1);
        userCloudEndPoint.child(userId).child("location1").setValue(location2);
    }

    public void addFirend(String localUserID, String friendUserID) {
        userCloudEndPoint.orderByChild("id").equalTo(friendUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //Add Friend
                    friendsCloudEndPoint.child(localUserID).child(friendUserID).setValue(true);
                    Log.d(TAG, "addFirend: User " + friendUserID + " added to User " + localUserID + "friendsList" );
                }
                else {
                    //Firend do not exist in DB
                    Log.d(TAG, "addFirend: User " + friendUserID + "dont exsist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

    /*
    public void getUser(String userId){
        if(userId != null){
            userCloudEndPoint.child().setValue();
        }
    }

    public void delete(String userId){
        if(userId != null){
            userCloudEndPoint.child().setValue();
        }
    }

    myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                //txtCount.setText("Count: "+value);
                //counter=Integer.valueOf(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

         userCloudEndPoint.setValue("Hello user").addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
        });
     */

