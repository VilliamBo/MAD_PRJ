package dk.au.mad22spring.AppProject.Group13.model;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Repository {

    private static Repository instance;
    private FirebaseDB database;

    private Repository(){
        database = new FirebaseDB();
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
            database.addUser(user);
        }
    }
    public void setLocation(String userId, String location1, String location2){
        database.setLocation(userId, location1, location2);
    }

    public void addFriend(String localUserID, String friendUserID){
        database.addFriend(localUserID, friendUserID);
    }


    public void deleteUser(String userId){

    }

    public MutableLiveData<ArrayList<User>> getAllFriends(String userId) {
        return database.getFriends(userId);
    }
}
