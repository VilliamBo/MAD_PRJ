package dk.au.mad22spring.AppProject.Group13;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.au.mad22spring.AppProject.Group13.model.BBCharacter;
import dk.au.mad22spring.AppProject.Group13.model.User;
import dk.au.mad22spring.AppProject.Group13.viewmodel.registerUserViewModel;

public class RegisterUserActivity extends AppCompatActivity {

    private static final String TAG = "RegisterUserActivity";

    private registerUserViewModel viewModel;

    private EditText regEmail, regUsername, regPassword, regConfirmPassword;
    private Button cancelBtn, registerBtn, getRandomImgButton;
    private ImageView regUserImage;

    private String userImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        viewModel = new ViewModelProvider(this).get(registerUserViewModel.class);
        viewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    User user = new User(
                            firebaseUser.getUid(),
                            regUsername.getText().toString(),
                            firebaseUser.getEmail(),
                            userImageURL);
                    viewModel.addUser(user);
                    viewModel.logOut();
                    finish();
                }
            }
        });

        viewModel.getImgURL().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String json) {
                if(json != null) {
                    Gson gson = new GsonBuilder().create();
                    BBCharacter[] characters = gson.fromJson(json, BBCharacter[].class);

                    if (characters[0] != null) {
                        Log.d(TAG, "onChanged: image URL is: " + characters[0].getImg());
                        Glide.with(regUserImage.getContext()).load(characters[0].getImg()).into(regUserImage);
                        userImageURL = characters[0].getImg(); //updating local variable used to register new user
                    }
                }
                else{
                    Log.d(TAG, "Couldn't get random under image");
                }
            }
        });

        setupUI();
    }

    private void setupUI() {
        regUserImage = findViewById(R.id.registerUserView);
        regUsername = findViewById(R.id.registerNameTxt);
        regEmail = findViewById(R.id.registerEMailTxt);
        regPassword = findViewById(R.id.registerPasswordTxt);
        regConfirmPassword = findViewById(R.id.registerConfirmPassword);

        cancelBtn = findViewById(R.id.cancelRegistrationBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterUserActivity.this, R.string.userRegistrationCanceledToast, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        registerBtn = findViewById(R.id.registerUserBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        getRandomImgButton = findViewById(R.id.genNewImgButton);
        getRandomImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.generateRandomImage();
            }
        });
        viewModel.generateRandomImage(); // get random user image from webAPI
    }

        private void registerUser() {
            String email = regEmail.getText().toString().trim(); // trim() removes whitespaces
            String userName = regUsername.getText().toString().trim();
            String password = regPassword.getText().toString().trim();
            String confirmPassword = regConfirmPassword.getText().toString().trim();

            if(TextUtils.isEmpty(userName)){
                regEmail.setError("Email cannot be empty");
                regEmail.requestFocus();
            }else if(TextUtils.isEmpty(email)){
                regUsername.setError("Username cannot be empty");
                regUsername.requestFocus();
            }else if(TextUtils.isEmpty(password)){
                regPassword.setError("Password cannot be empty");
                regPassword.requestFocus();
            }else if(TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)){
                regConfirmPassword.setError("Passwords are not identical");
                regConfirmPassword.requestFocus();
            }else{
                viewModel.register(email, password);
            }
        }
    }
