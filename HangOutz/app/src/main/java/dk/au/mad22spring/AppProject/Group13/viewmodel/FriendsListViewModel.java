package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.model.User;

public class FriendsListViewModel extends AndroidViewModel {

    private static final String TAG = "FriendListViewModel";

    private MutableLiveData<ArrayList<User>> friendList;
    private MutableLiveData<String> searchFilter;

    //################### TEST VARIABLE ##################################
    private String localUserId = "1234";
    private MutableLiveData<ArrayList<String>> friendIdList;
    private MutableLiveData<ArrayList<User>> observedFriendList;
    //####################################################################


    //Repository to communicate with USER Firebase database
    private Repository repository;

    public FriendsListViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();

        if (searchFilter == null){
            searchFilter = new MutableLiveData<String>();
            searchFilter.setValue("");
        }
        //repository.getAllFriends(localUserId, getFriendList());
        linkFriendIdListToDatabase();
    }

    public void updateFriendList() {
        repository.getUsersFromId(getFriendList(), getFriendIdList().getValue());
    }

    public MutableLiveData<ArrayList<User>> getFriendList(){
        if(friendList == null) {
            friendList = new MutableLiveData<ArrayList<User>>();
            friendList.setValue(new ArrayList<>());
        }
        return friendList;
    }

    public MutableLiveData<ArrayList<String>> getFriendIdList(){
        if(friendIdList == null) {
            friendIdList = new MutableLiveData<ArrayList<String>>();
            friendIdList.setValue(new ArrayList<>());
        }
        return friendIdList;
    }

    public void search(String searchFilterStr) {
        searchFilter.setValue(searchFilterStr);
        repository.searchUsers(getApplication(), getFriendList(), searchFilterStr);
    }

    //Links Friend id list to friend.child(userid). if it updates 
    private void linkFriendIdListToDatabase(){
        repository.getFriendsId(localUserId, getFriendIdList());
    }

    public void removeFriend(User friendClicked) {
        repository.removeFriend(friendClicked);
    }
}
