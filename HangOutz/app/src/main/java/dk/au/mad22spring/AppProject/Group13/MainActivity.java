package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import dk.au.mad22spring.AppProject.Group13.viewmodel.mainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private mainViewModel viewModel;

    //UI widgets
    private Button logOutBtn, fetchButton;

    private Button goToHangOutzBtn; // for developing

    private TextView loggedInUserText;
    private ImageView BBCharImg;


    //API variables
    private RequestQueue myRequestQueue;
    private StringRequest myStringRequest;
    private String url = "https://www.breakingbadapi.com/api/character/random";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(mainViewModel.class);
        viewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    loggedInUserText.setText("Logged in user: " + firebaseUser.getEmail());
                    logOutBtn.setEnabled(true);
                }
                else{
                    logOutBtn.setEnabled(false);
                }
            }
        });

        viewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if(loggedOut){
                    Toast.makeText(MainActivity.this, "User logged out", Toast.LENGTH_SHORT).show();
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

        BBCharImg = findViewById(R.id.BBimageView);
        fetchButton = findViewById(R.id.fetchAPIBtn);
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchAPI();
            }
        });

        loggedInUserText = findViewById(R.id.loginStatusTxt);
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
    }

    private void fetchAPI() {

        //RequestQueue initiating
        if(myRequestQueue == null){
            myRequestQueue = Volley.newRequestQueue(this);
        }

        //String Request initiating
        myStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        myRequestQueue.add(myStringRequest);
    }

    private void parseJson(String json){
        Log.d(TAG, "json: " + json);

        Gson gson = new GsonBuilder().create();
        BBCharacter[] characters = gson.fromJson(json, BBCharacter[].class);

        if(characters[0] != null){
            Glide.with(BBCharImg.getContext()).load(characters[0].getImg()).into(BBCharImg);
        }

    }
}