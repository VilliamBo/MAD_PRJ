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

public class RegisterUserActivity extends AppCompatActivity {

    private TextView regEmail, regUsername, regPassword, regConfirmPassword;
    private Button cancelBtn, registerBtn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        if(mAuth == null){
            mAuth = FirebaseAuth.getInstance();
        }
        setupUI();
    }

    private void setupUI() {
        regEmail = findViewById(R.id.regEmailTxt);
        regUsername = findViewById(R.id.regUserNameTxt);
        regPassword = findViewById(R.id.regPasswordTxt);
        regConfirmPassword = findViewById(R.id.regConPasswordTxt);

        cancelBtn = findViewById(R.id.regCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        registerBtn = findViewById(R.id.regUserBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = regEmail.getText().toString();
        String userName = regUsername.getText().toString();
        String password = regPassword.getText().toString();
        String confirmPassword = regConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            regEmail.setError("Email cannot be empty");
            regEmail.requestFocus();
        }else if(TextUtils.isEmpty(userName)){
            regUsername.setError("Username cannot be empty");
            regUsername.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            regPassword.setError("Username cannot be empty");
            regPassword.requestFocus();
        }else if(TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)){
            regConfirmPassword.setError("Passwords are not identical");
            regConfirmPassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email.toLowerCase(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterUserActivity.this, "New user registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterUserActivity.this, "Registration error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        }
    }
