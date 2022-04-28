package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button loginBtn, newUserBtn;

    //this should be deleted
    private Button goToHangBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupWidgets();

    }

    private void setupWidgets() {
        loginBtn = findViewById(R.id.loginBtn);
        newUserBtn = findViewById(R.id.newUserBtn);

        goToHangBtn = findViewById(R.id.goToHangOutzBtn);
        goToHangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HangOutzActivity.class);
                startActivity(intent);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOn();
            }
        });
    }

    private void signOn() {
        if(auth == null) {
            auth = FirebaseAuth.getInstance();
        }

    }
}