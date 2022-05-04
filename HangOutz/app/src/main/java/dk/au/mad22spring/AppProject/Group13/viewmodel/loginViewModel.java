package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import dk.au.mad22spring.AppProject.Group13.model.AuthenticationRepository;


public class loginViewModel extends AndroidViewModel {

    private AuthenticationRepository repository;
    private MutableLiveData<FirebaseUser> userLiveData;

    //constructor
    public loginViewModel(@NonNull Application application) {
        super(application);

        repository = new AuthenticationRepository(application);
        userLiveData = repository.getUserLiveData();
    }

    public void login(String email, String password){
        repository.login(email, password);
    }

    //get method
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}
