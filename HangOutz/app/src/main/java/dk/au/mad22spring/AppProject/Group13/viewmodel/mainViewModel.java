package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import dk.au.mad22spring.AppProject.Group13.model.Authentication;

public class mainViewModel extends AndroidViewModel {

    private Authentication repository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public mainViewModel(@NonNull Application application) {
        super(application);

        repository = new Authentication();
        userLiveData = repository.getUserLiveData();
        loggedOutLiveData = repository.getLoggedOutLiveData();
    }

    public void logOut(){
        repository.logOut();
    }

    //get methods
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
