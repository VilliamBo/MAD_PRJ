package dk.au.mad22spring.AppProject.Group13.model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Repository {

    private static Repository instance;
    private FirebaseDB db;

    private Repository(){
        db = new FirebaseDB();
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

    public void getUSer(String userId, MutableLiveData<User> user){
        db.getUser(userId, user);
    }
    public void setUserLocation(String localUserId, String location1, String location2){
        db.setUserLocation(localUserId, location1, location2);
    }

    public void setUserImg(String localUserId, String imgUrl){
        db.setUserImg(localUserId, imgUrl);
    }

    public void setUserName(String localUserId, String name){
        db.setUserName(localUserId, name);
    }

    public void addFriend(String localUserID, String friendUserID){
        db.addFriend(localUserID, friendUserID);
    }

    public void searchUsers(Context context, MutableLiveData<ArrayList<User>> userList, String searchStr){
        db.searchUsers(context, userList, searchStr);
    }

    public void linkToUserDatabase(MutableLiveData<ArrayList<User>> userList, MutableLiveData<String> searchFilter){
        db.linkToUserDatabase(userList, searchFilter);
    }

    public void deleteUser(String userId){
        db.deleteUser(userId);
    }

    public void getFriendsId(String localUserID, MutableLiveData<ArrayList<String>> friendsIdList){
        db.getFriendsId(localUserID, friendsIdList);
    }

    public void getUsersFromId(Context context, MutableLiveData<ArrayList<User>> userList, ArrayList<String> userIdList){
        db.getUsersFromId(context, userList, userIdList);
    }
}
