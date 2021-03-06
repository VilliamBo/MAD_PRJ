package dk.au.mad22spring.AppProject.Group13.model;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Repository {

    private static final String TAG = "Repository";

    private static Repository instance;
    private FirebaseDB db;
    private Authentication mAuth;
    private webAPI api;

    private String loggedInUSerID;

    private Repository() {
        db = new FirebaseDB();
        mAuth = new Authentication();
        api = new webAPI();
    }

    //Singleton pattern
    public static Repository getInstance(){
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void addUser(User user){
        if(user != null) {
            db.addUser(user);
        }
    }

    public void getUser(String userId, MutableLiveData<User> user){
        db.getUser(userId, user);
    }
    public void setUserLocation(String localUserId, String latitude, String longitude){
        if(loggedInUSerID != null) {
            db.setUserLocation(loggedInUSerID, latitude, longitude);
        }
        else{
            Log.d(TAG, "setUserLocation: Couldn't set userLocation - loggedInUserID = null");
        }
        
    }

    public void setUserImg(String localUserId, String imgUrl){
        if(loggedInUSerID != null) {
            db.setUserImg(loggedInUSerID, imgUrl);
        }
        else{
            Log.d(TAG, "setUserImg: Couldn't set userImg - loggedInUserID = null");
        }
    }

    public void setUserName(String localUserId, String name){
        if(loggedInUSerID != null) {
            db.setUserName(loggedInUSerID, name);
        }
        else{
            Log.d(TAG, "setUserName: Couldn't set userName - loggedInUserID = null");
        }
    }

    public void addFriend(String localUserID, String friendUserID){
        if(loggedInUSerID != null) {
            db.addFriend(loggedInUSerID, friendUserID);
        }
        else{
            Log.d(TAG, "addFriend: Couldn't add friend - loggedInUserID = null");
        }
    }

    public void searchUsers(Context context, MutableLiveData<ArrayList<User>> userList, String searchStr){
        db.searchUsers(context, userList, searchStr);
    }

    public void linkToUserDatabase(MutableLiveData<ArrayList<User>> userList, MutableLiveData<String> searchFilter){
        db.linkToUserDatabase(userList, searchFilter);
    }

    public void deleteAccount(String userId){
        if(loggedInUSerID != null) {
            db.deleteUser(loggedInUSerID);
            mAuth.mAuth.getCurrentUser().delete();
        }
        else{
            Log.d(TAG, "deleteUser: Couldn't delete user - loggedInUserID = null");
        }
    }

    public void getFriendsId(MutableLiveData<ArrayList<String>> friendsIdList){
        if(loggedInUSerID != null) {
            db.getFriendsId(loggedInUSerID, friendsIdList);
        }
        else{
            Log.d(TAG, "getFriendsId: Couldn't get friends - loggedInUserID = null");
        }
    }

    public void getUsersFromId(MutableLiveData<ArrayList<User>> userList, ArrayList<String> userIdList){
        db.getUsersFromId(userList, userIdList);
    }

    public void getLocalUser(MutableLiveData<User> localUser){
        db.getUserByID(localUser, loggedInUSerID);
    }

    // set and get methods for private "loggedInUserID" string
    public String getLoggedInUserID(){
        if(loggedInUSerID == null) {
            Log.d(TAG, "getLoggedInUserID: No user logged in. ID = null");
        }
        return loggedInUSerID;
    }

    public void setLoggedInUserID(String uid) {
        loggedInUSerID = uid;
    }

    public String getLoggedInUSerID(){
        return loggedInUSerID;
    }

    public void setActivity(String activity) {
        db.setActivity(loggedInUSerID, activity);
    }

    public void setEnergy(int energy) {
        db.setEnergy(loggedInUSerID, energy);
    }

    public void setActive(Boolean state) {
        db.setActive(loggedInUSerID, state);
    }

    //method for webAPI
    public void getRandomImage(MutableLiveData<String> imgURL, Context context){
        api.getRandomImage(imgURL, context);
    }

    public void removeFriend(User friendClicked) {
        db.removeFriend(loggedInUSerID, friendClicked);
    }

    public void updateUser(String name) {
        db.setUserName(loggedInUSerID, name);
    }
}
