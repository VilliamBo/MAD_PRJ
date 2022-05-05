package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import dk.au.mad22spring.AppProject.Group13.Constants;
import dk.au.mad22spring.AppProject.Group13.model.Authentication;
import dk.au.mad22spring.AppProject.Group13.model.Repository;

public class mainViewModel extends AndroidViewModel {

    private Authentication auth;
    private Repository repo;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<List<Location>> friendLocations;


    public mainViewModel(@NonNull Application application) {
        super(application);

        repo = Repository.getInstance();
        auth = new Authentication();
        userLiveData = auth.getUserLiveData();
        loggedOutLiveData = auth.getLoggedOutLiveData();
        //friendLocations = getFriendLocations();
    }

    public void logOut(){
        auth.logOut();
    }

    //get methods
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public void setUserLocation(Location userLocation) {
        // TODO: Talk with the Boooois. About repository = Authentication & userId. Skal der være en fragment pr. activity.
        String userId = auth.getUserLiveData().getValue().getUid();
        String latitude = String.valueOf(userLocation.getLatitude());
        String longitude = String.valueOf(userLocation.getLongitude());

        repo.setUserLocation(userId,latitude, longitude);
    }

    public MutableLiveData<List<Location>> getFriendLocations() {
        return null;
        //return repo.getFriendLocations(); // Doesn't exist yet.
    }

    public void updateMap(GoogleMap googleMap) {

        // TODO: Update when repo-function is made
        // Get all activity locations of the active users friends
        friendLocations = getFriendLocations();

        // Delete all markers = No dupes & old markers.
        deleteMapMarkers(googleMap);
        // Add all the updated markers.
        addFriendMarkers(friendLocations.getValue(),googleMap);
    }

    private void addMarker(Location location, GoogleMap googleMap) {
        if(location != null){
            LatLng friendLocation = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d(Constants.DEBUG, "addMarker: " + friendLocation);

            if(googleMap != null){
                Log.d(Constants.DEBUG, "addMarker: mMap != null");
                googleMap.addMarker(new MarkerOptions().position(friendLocation).title("Some information about who put it up").snippet("Information about this activity here"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    private void addFriendMarkers(List<Location> locations, GoogleMap googleMap) {
        for(Location location : locations){
            if(location != null) {
                addMarker(location, googleMap);
            }
        }
    }

    private void deleteMapMarkers(GoogleMap googleMap) {
        // Deletes all map markers, etc.
        googleMap.clear();
    }

    // This method is purely for testing
    private void createTestLocations() {
        Location Randers = new Location("Randers");
        Randers.setLatitude(56.4586);
        Randers.setLongitude(10.0402);

        Location Aarhus = new Location("Aarhus");
        Aarhus.setLatitude(56.1569);
        Aarhus.setLongitude(10.2108);

        Location Miami = new Location("Miami");
        Miami.setLatitude(25.7617);
        Miami.setLongitude(-80.1918);

        Location London = new Location("London");
        London.setLatitude(51.4346);
        London.setLongitude(-0.4578);

        friendLocations.getValue().add(Randers);
        friendLocations.getValue().add(Aarhus);
        friendLocations.getValue().add(Miami);
        friendLocations.getValue().add(London);
    }
}
