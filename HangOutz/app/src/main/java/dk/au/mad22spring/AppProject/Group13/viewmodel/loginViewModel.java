package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import dk.au.mad22spring.AppProject.Group13.model.Authentication;
import dk.au.mad22spring.AppProject.Group13.model.Repository;

public class loginViewModel extends AndroidViewModel {

    private Authentication authentication;
    private Repository repository;

    private MutableLiveData<FirebaseUser> userLiveData;

    //constructor
    public loginViewModel(@NonNull Application application) {
        super(application);

        repository = Repository.getInstance();
        authentication = new Authentication();
        userLiveData = authentication.getUserLiveData();
    }

    public void login(String email, String password){
        authentication.login(email, password, getApplication());
    }

    //get method
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public void notifyLogin(){
        repository.auth = new Authentication();
    }
}
