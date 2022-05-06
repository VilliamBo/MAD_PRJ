package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

import dk.au.mad22spring.AppProject.Group13.model.BBCharacter;
import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.viewmodel.mainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private mainViewModel viewModel;

    //UI widgets
    private Button logOutBtn, goToHangOutzBtn, btnGoToFriendsList;
    private EditText edtActivity;
    private Switch swtActive;
    private SeekBar skBarEnergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(mainViewModel.class);

        viewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if(loggedOut){
                    Toast.makeText(MainActivity.this, "User logged out", Toast.LENGTH_SHORT).show();
                    viewModel.setStatusAsLoggedOut(); // loggedInUserID = null
                    goToLoginScreen();
                }
            }
        });

        setupUI();
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void setupUI() {

        logOutBtn = findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.logOut();
            }
        });

        goToHangOutzBtn = findViewById(R.id.goToHOBtn);
        goToHangOutzBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HangOutzActivity.class);
                startActivity(intent);
            }
        });

        edtActivity = findViewById(R.id.edtActivity);

        skBarEnergy = findViewById(R.id.skBarEnergy);

        swtActive = findViewById(R.id.swtActive);
        swtActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setActive(swtActive.isChecked());
                if(swtActive.isChecked()){
                    viewModel.setActivity(edtActivity.getText().toString());
                    viewModel.setEnergy(skBarEnergy.getProgress());
                }
            }
        });

        btnGoToFriendsList = findViewById(R.id.btnGoToFriendsList);
        btnGoToFriendsList.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, FriendsListActivity.class);
            startActivity(i);
        });
    }
}