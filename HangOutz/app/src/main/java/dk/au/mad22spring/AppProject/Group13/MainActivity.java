package dk.au.mad22spring.AppProject.Group13;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.AppProject.Group13.model.BBCharacter;
import dk.au.mad22spring.AppProject.Group13.service.FriendActivityNotificationService;
import dk.au.mad22spring.AppProject.Group13.viewmodel.mainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //viewModel
    private mainViewModel viewModel;

    //UI widgets
    private Button logOutBtn, friendsBtn;
    private EditText edtActivity;
    private Switch swtActive;
    private SeekBar skBarEnergy;

    // Location Variables
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation = null;
    private Location currentLocation = null;
    private Boolean noLocation = false;
    private MapsFragment mapsFragment;


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

        setupMap(savedInstanceState);
        setupUI();
        startNotificationService();
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

        logOutBtn = findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.logOut();
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

        friendsBtn = findViewById(R.id.btnGoToFriendsList);
        friendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, FriendsListActivity.class);
                startActivity(i);
                Toast.makeText(MainActivity.this, "Friend button clicked", Toast.LENGTH_SHORT).show();
            }
        });



        // TODO: Implement below comment
        // VILLIAM has to make his magic on this one so the map plots is added when database is updated

        /*btnFindAFriend = findViewById(R.id.btnFindAFriend);
        btnFindAFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*do{
                    currentLocation = getCurrentLocation();
                } while (currentLocation == null);*//*
                currentLocation = getCurrentLocation();
                //getCurrentLocationCallback(); // Purely for testing

                if(currentLocation != null) {
                    List<Location> locations = new ArrayList<>();
                    locations.add(currentLocation);
                    mapsFragment.updateMap(locations);
                    *//*viewModel.setUserLocation(currentLocation);
                    viewModel.updateMap(viewModel.getMap());*//*
                }
            }
        });*/

        //setup location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    // https://developer.android.com/training/location/request-updates#java
    // nice to have - saved for future work
    private Location getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_CODE);;
        }

        //functions even though it is red marked
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

        //functions even though it is red marked
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
    private void startNotificationService() {
        Intent NotificationServiceIntent = new Intent(MainActivity.this, FriendActivityNotificationService.class);
        startService(NotificationServiceIntent);
    }
}