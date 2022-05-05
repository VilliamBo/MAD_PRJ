package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import dk.au.mad22spring.AppProject.Group13.model.Authentication;
import dk.au.mad22spring.AppProject.Group13.model.Repository;

public class mainViewModel extends AndroidViewModel {

    private Authentication authentication;
    private Repository repository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public mainViewModel(@NonNull Application application) {
        super(application);

        repository = Repository.getInstance();
        authentication = new Authentication();
        userLiveData = authentication.getUserLiveData();
        loggedOutLiveData = authentication.getLoggedOutLiveData();
    }

    public void logOut(){
        authentication.logOut();
    }

    public void setStatusAsLoggedOut(){
        repository.setLoggedInUserID(null);
    }

    //get methods
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
