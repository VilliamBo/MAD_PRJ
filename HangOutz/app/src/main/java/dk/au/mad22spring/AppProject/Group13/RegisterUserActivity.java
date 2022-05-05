package dk.au.mad22spring.AppProject.Group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dk.au.mad22spring.AppProject.Group13.model.User;
import dk.au.mad22spring.AppProject.Group13.viewmodel.loginViewModel;
import dk.au.mad22spring.AppProject.Group13.viewmodel.registerUserViewModel;

public class RegisterUserActivity extends AppCompatActivity {

    private registerUserViewModel viewModel;

    private EditText regEmail, regUsername, regPassword, regConfirmPassword;
    private Button cancelBtn, registerBtn;

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
                            "");
                    viewModel.addUser(user);
                    Toast.makeText(RegisterUserActivity.this, "User is already logged in", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        setupUI();
    }

    private void setupUI() {
        regUsername = findViewById(R.id.registerNameTxt);
        regEmail = findViewById(R.id.registerEMailTxt);
        regPassword = findViewById(R.id.registerPasswordTxt);
        regConfirmPassword = findViewById(R.id.registerConfirmPassword);

        cancelBtn = findViewById(R.id.cancelRegistrationBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterUserActivity.this, "Registration canceled", Toast.LENGTH_SHORT).show();
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
            regPassword.setError("Username cannot be empty");
            regPassword.requestFocus();
        }else if(TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)){
            regConfirmPassword.setError("Passwords are not identical");
            regConfirmPassword.requestFocus();
        }else{
            viewModel.register(email, password);
        }
        }
    }
