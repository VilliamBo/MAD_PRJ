package dk.au.mad22spring.AppProject.Group13.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDB {
    public FirebaseDatabase database;
    private DatabaseReference userCloudEndPoint;

    public FirebaseDB(){
        database = FirebaseDatabase.getInstance();
        userCloudEndPoint = database.getReference("Users");
    }

    public void addUser(User user){
        if(user != null){
            user.id = userCloudEndPoint.push().getKey();
            userCloudEndPoint.child(user.id).setValue(user);
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
}
