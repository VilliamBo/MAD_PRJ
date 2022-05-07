package dk.au.mad22spring.AppProject.Group13;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.ArrayList;

import dk.au.mad22spring.AppProject.Group13.model.User;
import dk.au.mad22spring.AppProject.Group13.service.FriendActivityNotificationService;
import dk.au.mad22spring.AppProject.Group13.viewmodel.mainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //viewModel
    private mainViewModel viewModel;

    //UI widgets
    private Button logOutBtn, friendsBtn, btnManageAccount;
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
                    Toast.makeText(MainActivity.this, R.string.userLoggedOutToast, Toast.LENGTH_SHORT).show();
                    viewModel.setStatusAsLoggedOut(); // loggedInUserID = null
                    goToLoginScreen();
                }
            }
        });

        viewModel.getLocalUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                swtActive.setChecked(user.active);
                skBarEnergy.setProgress(user.energy);
                edtActivity.setText(user.activity);
            }
        });

        setupMap(savedInstanceState);
        setupUI();
        setupLocationHandler();
        startNotificationService();
        viewModel.getLocalUser(); // Request current user
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateMap(viewModel.getFriendList().getValue()); // added, but didn't work
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
            }
        });

        btnManageAccount = findViewById(R.id.btnGoToManageAccount);
        btnManageAccount.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, ManageAccountActivity.class);
            startActivity(i);
        });

        viewModel.getFriendIdList().observe(this, users -> {viewModel.updateFriendList();});
        viewModel.getFriendList().observe(this, users -> {updateMap(users);});

        // Setup location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    // Location Handler to get user location every 5 seconds
    // Inspired by: https://developer.android.com/reference/android/os/Handler
    private void setupLocationHandler() {
        final Handler handler = new Handler();
        final int delay = 5000; // in milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {

                if(swtActive.isChecked()){

                    currentLocation = getCurrentLocation();
                    Log.d(TAG, "run: Current Location:" + currentLocation);

                    if(currentLocation == null){
                        lastKnownLocation = getLastLocation();
                        Log.d(TAG, "run: Last Location:" + lastKnownLocation);

                        if(lastKnownLocation != null){
                            viewModel.setUserLocation(lastKnownLocation);
                        }

                    } else {
                        viewModel.setUserLocation(currentLocation);
                    }

                    Log.d(TAG, "run: Handler Run: " + currentLocation);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void updateMap(ArrayList<User> friends) {
        ArrayList<User> activeFriends = new ArrayList<>();
        int i = 0;
        for(User u : friends){
            if(u.active) {
                activeFriends.add(u);
                i++;
            }

        }

        mapsFragment.updateMap(activeFriends);
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
                            Toast.makeText(fusedLocationClient.getApplicationContext(), R.string.noLocationFoundToast, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(fusedLocationClient.getApplicationContext(), R.string.noLocationFoundToast, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, R.string.locationPermissionToast, Toast.LENGTH_LONG).show();
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