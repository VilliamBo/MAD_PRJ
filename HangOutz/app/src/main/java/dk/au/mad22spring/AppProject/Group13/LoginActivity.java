package dk.au.mad22spring.AppProject.Group13;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Repo;

import dk.au.mad22spring.AppProject.Group13.model.FirebaseDB;
import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.viewmodel.loginViewModel;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //viewModel instance
    private loginViewModel viewModel;

    //UI widgets
    private Button loginBtn, registerNewBtn;
    private TextView userNameTxt, passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(loginViewModel.class);
        viewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    Log.d("TAG", "LoggedIn ID: " + firebaseUser.getUid());
                    viewModel.setLoggedInUserID(firebaseUser.getUid());
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        setupUI();
    }

    private void setupUI() {

        userNameTxt = findViewById(R.id.loginEmailInput);
        passwordTxt = findViewById(R.id.loginPassInput);

        loginBtn = findViewById(R.id.loginButton);
        registerNewBtn = findViewById(R.id.loginCreateUserBtn);
        registerNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String email = userNameTxt.getText().toString().trim(); //trim removes whitespaces in both ends
        String password = passwordTxt.getText().toString().trim();

        // fail first -> checking if input fields is filled
        if(TextUtils.isEmpty(email)){
            userNameTxt.setError("Email cannot be empty");
            userNameTxt.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            passwordTxt.setError("Password cannot be empty");
            passwordTxt.requestFocus();
        }
        else{ // inputs OK - call ViewModel function
            viewModel.login(email, password);
        }
    }
}