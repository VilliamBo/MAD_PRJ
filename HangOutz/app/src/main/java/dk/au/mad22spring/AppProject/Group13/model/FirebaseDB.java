package dk.au.mad22spring.AppProject.Group13.model;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseDB {
    private static final String TAG = "FireBaseDB TAG";

    public FirebaseDatabase db;
    private DatabaseReference userCloudEndPoint;
    private DatabaseReference friendsCloudEndPoint;

    public FirebaseDB(){
        db = FirebaseDatabase.getInstance();
        userCloudEndPoint = db.getReference("Users");
        friendsCloudEndPoint = db.getReference("Friends");
    }

    public void addUser(User user){
        if(user != null){
            userCloudEndPoint.child(user.id).setValue(user);
            friendsCloudEndPoint.child(user.id).setValue(user.id);
        }
    }

    public void getUser(String userID, MutableLiveData<User> user){
        userCloudEndPoint.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user.setValue(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addFriend(String localUserID, String friendUserID) {
        userCloudEndPoint.orderByChild("id").equalTo(friendUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //Add Friend
                    friendsCloudEndPoint.child(localUserID).child(friendUserID).setValue("");
                    //friendsCloudEndPoint.child(localUserID).child(friendUserID);
                    Log.d(TAG, "addFriend: User: " + friendUserID + " added to User: " + localUserID + "'s friendsList" );
                }
                else {
                    //Friend do not exist in DB
                    Log.d(TAG, "addFriend: User: " + friendUserID + " does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUsersFromId(MutableLiveData<ArrayList<User>> userList, ArrayList<String> userIdList){
        userCloudEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> newFriendList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    for(String id: userIdList){
                        if(user.id.equals(id)) {
                            newFriendList.add(user);
                        }
                    }
                    userList.setValue(newFriendList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }

    public void getFriendsId(String localUserID, MutableLiveData<ArrayList<String>> friendsIdList){
        friendsCloudEndPoint.child(localUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> newFriendsIdList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    newFriendsIdList.add(key);
                }
                friendsIdList.setValue(newFriendsIdList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void linkToUserDatabase(MutableLiveData<ArrayList<User>> userList, MutableLiveData<String> searchFilter){
        userCloudEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> myList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if(searchFilter.getValue().equals("") || user.id.contains(searchFilter.getValue())){
                        myList.add(user);
                       userList.setValue(myList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void searchUsers(Context context, MutableLiveData<ArrayList<User>> userList, String searchStr){
            userCloudEndPoint.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<User> myList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.id.contains(searchStr)) {
                            myList.add(user);
                        }
                        userList.setValue(myList);
                    }
                    if(myList.isEmpty()) {
                        Toast toast = Toast.makeText(context, "No Users Found", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error){}
            });
    }

    public void setUserLocation(String userID, String latitude, String longitude){
        userCloudEndPoint.child(userID).child("latitude").setValue(latitude);
        userCloudEndPoint.child(userID).child("longitude").setValue(longitude);
    }

    public void setUserImg(String userID, String imgUrl){
        userCloudEndPoint.child(userID).child("imgUrl").setValue(imgUrl);
    }

    public void setUserName(String userID, String name){
        userCloudEndPoint.child(userID).child("name").setValue(name);
    }

    public void setActivity(String userID, String activity) {
        userCloudEndPoint.child(userID).child("activity").setValue(activity);
    }

    public void setEnergy(String userID, int energy) {
        userCloudEndPoint.child(userID).child("energy").setValue(energy);
    }

    public void setActive(String userID, Boolean state) {
        userCloudEndPoint.child(userID).child("active").setValue(state);
    }

    public void setUserEmail(String userID, String email) {
        userCloudEndPoint.child(userID).child("email").setValue(email);
    }

    public void deleteUser(String userID){
        userCloudEndPoint.child(userID).removeValue();
        friendsCloudEndPoint.child(userID).removeValue();
    }


    public void removeFriend(String userID, User friendClicked) {
        friendsCloudEndPoint.child(userID).child(friendClicked.id).removeValue();
    }


}



