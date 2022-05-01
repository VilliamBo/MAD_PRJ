package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.model.User;

public class FriendsListViewModel extends AndroidViewModel {

    private static final String TAG = "FriendListViewModel";

    //private MutableLiveData<List<USer>> drinks;

    //Repository to communicate with USER Firebase database
    private Repository repository;

    public FriendsListViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        // defaultDrinks = new ArrayList<Drink__1>();
    }

    public MutableLiveData<ArrayList<User>> getFriends(String localUserId){ return repository.getAllFriends(localUserId); }
}
