package dk.au.mad22spring.AppProject.Group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dk.au.mad22spring.AppProject.Group13.model.BBCharacter;
import dk.au.mad22spring.AppProject.Group13.viewmodel.hangOutzViewModel;
import dk.au.mad22spring.AppProject.Group13.viewmodel.mainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private mainViewModel viewModel;
    //private hangOutzViewModel vm; // Purely for testing

    //UI widgets
    private Button logOutBtn, fetchButton;

    private Button goToHangOutzBtn; // for developing
    private Button btnFindAFriend; // FOR DEBUGGING ONLY

    private TextView loggedInUserText;
    private ImageView BBCharImg;

    // Location Variables
    private FusedLocationProviderClient fusedLocationClient;
    private com.google.android.gms.location.LocationRequest googleLocationRequest;
    private LocationRequest androidLocationRequest;
    private LocationCallback locationCallback;
    private Location lastKnownLocation = null;
    private Location currentLocation = null;
    private Boolean noLocation = false;
    private MapsFragment mapsFragment;

    //API variables
    private RequestQueue myRequestQueue;
    private StringRequest myStringRequest;
    private String url = "https://www.breakingbadapi.com/api/character/random";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vm = new ViewModelProvider(this).get(hangOutzViewModel.class); // FOR TESTING


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

        setupMap(savedInstanceState);
        setupUI();
    }

    private void setupMap(Bundle savedInstanceState) {
        mapsFragment = new MapsFragment();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mapsContainer, mapsFragment)
                    .commitNow();
        }
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

        // TODO: Implement below comment
        // Purely for testing
        btnFindAFriend = findViewById(R.id.btnFindAFriend);
        btnFindAFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*do{
                    currentLocation = getCurrentLocation();
                } while (currentLocation == null);*/
                currentLocation = getCurrentLocation();
                //getCurrentLocationCallback(); // Purely for testing

                if(currentLocation != null) {
                    List<Location> locations = new ArrayList<>();
                    locations.add(currentLocation);
                    mapsFragment.updateMap(locations);
                    /*viewModel.setUserLocation(currentLocation);
                    viewModel.updateMap(viewModel.getMap());*/
                }
            }
        });

        setupLocationFramework();
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

    private void setupLocationFramework() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if(locationResult == null){
                    Log.d(TAG, "onLocationResult: NULL");
                    return;
                } else {
                    for (Location location : locationResult.getLocations()){
                        Log.d(TAG, "onLocationResult: " + location.toString());
                        currentLocation = location;

                        if(fusedLocationClient != null){
                            fusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
    }

    // https://developer.android.com/training/location/request-updates#java
    private Location getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_CODE);;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        lastKnownLocation = location;
                        if (location == null) {
                            Toast.makeText(fusedLocationClient.getApplicationContext(), "No location was found", Toast.LENGTH_SHORT).show();
                            noLocation = true;
                        }
                    }
                });

        if (noLocation) {
            noLocation = false;
            return null;
        } else {
            return lastKnownLocation;
        }
    }

    //BELOW IS FOR TESTING
    private void getCurrentLocationCallback() {

        // Check to see if permissions have been granted. If not request the permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_CODE);;
        }

        fusedLocationClient.requestLocationUpdates(googleLocationRequest,locationCallback, Looper.getMainLooper());
    }

    // https://developer.android.com/training/location/retrieve-current
    private Location getCurrentLocation() {

        CancellationToken cancellationToken = new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        };

        // Check to see if permissions have been granted. If not request the permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_CODE);;
        }

        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, cancellationToken)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        currentLocation = location;
                        if (location == null) {
                            Toast.makeText(fusedLocationClient.getApplicationContext(), "No location was found", Toast.LENGTH_SHORT).show();
                            noLocation = true;
                        }
                    }
                });

        if(noLocation){
            noLocation = false;
            return null;
        } else {
            return currentLocation;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.LOCATION_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(Constants.DEBUG, "checkPermissions: PERMISSION_GRANTED");
                } else {
                    Toast.makeText(this, "Location permissions need to be enabled to use this feature", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }
}