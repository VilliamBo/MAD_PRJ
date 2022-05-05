package dk.au.mad22spring.AppProject.Group13.model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Repository {

    private static Repository instance;
    private FirebaseDB db;
    public Authentication auth;

    private Repository(){
        db = new FirebaseDB();
        auth = new Authentication();
    }

    //Singleton pattern
    public static Repository getInstance(){
        if (instance == null){
            instance = new Repository();
        }
        return instance;
    }

    public void addUser(User user){
        if(user != null){
            db.addUser(user);
        }
    }

    public void getUser(String userId, MutableLiveData<User> user){
        db.getUser(userId, user);
    }
    public void setUserLocation(String localUserId, String location1, String location2){
        db.setUserLocation(auth.getUserLiveData().getValue().getUid(), location1, location2);
    }

    public void setUserImg(String localUserId, String imgUrl){
        db.setUserImg(auth.getUserLiveData().getValue().getUid(), imgUrl);
    }

    public void setUserName(String localUserId, String name){
        db.setUserName(auth.getUserLiveData().getValue().getUid(), name);
    }

    public void addFriend(String localUserID, String friendUserID){
        db.addFriend(auth.getUserLiveData().getValue().getUid(), friendUserID);
    }

    public void searchUsers(Context context, MutableLiveData<ArrayList<User>> userList, String searchStr){
        db.searchUsers(context, userList, searchStr);
    }

    public void linkToUserDatabase(MutableLiveData<ArrayList<User>> userList, MutableLiveData<String> searchFilter){
        db.linkToUserDatabase(userList, searchFilter);
    }

    public void deleteUser(String userId){
        db.deleteUser(auth.getUserLiveData().getValue().getUid());
    }

    public void getFriendsId(String localUserID, MutableLiveData<ArrayList<String>> friendsIdList){
        db.getFriendsId(auth.getUserLiveData().getValue().getUid(), friendsIdList);
    }

    public void getUsersFromId(Context context, MutableLiveData<ArrayList<User>> userList, ArrayList<String> userIdList){
        db.getUsersFromId(context, userList, userIdList);
    }
}
