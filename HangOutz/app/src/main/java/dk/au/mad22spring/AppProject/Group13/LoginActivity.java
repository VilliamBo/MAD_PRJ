package dk.au.mad22spring.AppProject.Group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.invoke.ConstantCallSite;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button loginBtn, createUserBtn;
    private TextView userNameTxt, passwordTxt;

    //this should be deleted
    private Button goToHangBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(mAuth == null){
            mAuth = FirebaseAuth.getInstance();
        }

        setupWidgets();
    }

    private void setupWidgets() {

        userNameTxt = findViewById(R.id.eMailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);

        loginBtn = findViewById(R.id.loginBtn);
        createUserBtn = findViewById(R.id.createUserBtn);
        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        goToHangBtn = findViewById(R.id.goToHangOutzBtn);

        // this is only for working on project
        goToHangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HangOutzActivity.class);
                startActivity(intent);
                finish();
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
        else{ // inputs OK - check with fireBase Authentication
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    }else{
                        Toast.makeText(LoginActivity.this, "Login error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        userNameTxt.setText("");
                        passwordTxt.setText("");
                    }
                }
            });
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); // start new activity -> MainActivity
        finish(); // destroy login screen when logged in
    }
}