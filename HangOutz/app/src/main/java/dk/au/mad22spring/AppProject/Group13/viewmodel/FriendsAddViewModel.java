package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.model.User;

public class FriendsAddViewModel extends AndroidViewModel {

    private static final String TAG = "FriendListViewModel";

    private MutableLiveData<ArrayList<User>> userList;
    private MutableLiveData<String> searchFilter;

    //Repository to communicate with USER Firebase database
    private Repository repository;

    public FriendsAddViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();

        if (searchFilter == null){
            searchFilter = new MutableLiveData<String>();
            searchFilter.setValue("");
        }
        repository.linkToUserDatabase(getUserList(), searchFilter);
    }

    public MutableLiveData<ArrayList<User>> getUserList(){
        if(userList == null) {
            userList = new MutableLiveData<ArrayList<User>>();
            userList.setValue(new ArrayList<>());
        }
        return userList;
    }

    public void search(String searchFilterStr) {
        searchFilter.setValue(searchFilterStr);
        repository.searchUsers(getApplication(), getUserList(), searchFilterStr);
    }

    public User getUser(String id) {
        for(User u: userList.getValue()) {
            if(u.id == id)
                return u;
        }
        return null;
    }

    public void addFriend(String localId, String friendId) {
        repository.addFriend(localId, friendId);
    }
    public void tis(String localId, String friendId) {
        repository.addUser(new User("di", "d"));
    }

}
