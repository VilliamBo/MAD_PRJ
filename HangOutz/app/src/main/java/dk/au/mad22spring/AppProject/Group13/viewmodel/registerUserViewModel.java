package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import dk.au.mad22spring.AppProject.Group13.model.Authentication;
import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.model.User;

public class registerUserViewModel extends AndroidViewModel {

    private Authentication authentication;
    private MutableLiveData<FirebaseUser> userLiveData;
    private Repository repository;

    //constructor
    public registerUserViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        authentication = new Authentication();
        userLiveData = authentication.getUserLiveData();
    }

    public void register(String email, String password){
        authentication.register(email, password, getApplication());
    }

    //get method
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public void addUser(User user) {
        repository.addUser(user);
    }

    public void logOut() {
        authentication.logOut();
    }
}
