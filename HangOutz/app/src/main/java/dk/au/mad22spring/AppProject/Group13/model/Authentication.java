// Authentication inspired from: https://learntodroid.com/how-to-use-firebase-authentication-in-an-android-app-using-mvvm/

package dk.au.mad22spring.AppProject.Group13.model;


import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {

    public FirebaseAuth mAuth;

    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;


    //Constructor
    public Authentication(){
        this.mAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        if(mAuth.getCurrentUser() != null) //if user is logged in
        {
            userLiveData.postValue(mAuth.getCurrentUser()); //update livedata with user instance
            loggedOutLiveData.postValue(false); //set livedata to logged in
        }
    }

    public void register(String email, String password, Context context){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userLiveData.postValue(mAuth.getCurrentUser());

                    mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        }
                    });
                }else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void login(String email, String password, Context context){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userLiveData.postValue(mAuth.getCurrentUser());
                }else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void logOut(){
        mAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    //get methods
    public FirebaseUser getCurrentUser(){return mAuth.getCurrentUser();}

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
