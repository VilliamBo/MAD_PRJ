package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import dk.au.mad22spring.AppProject.Group13.Constants;
import dk.au.mad22spring.AppProject.Group13.R;
import dk.au.mad22spring.AppProject.Group13.model.Authentication;
import dk.au.mad22spring.AppProject.Group13.model.Repository;

public class mainViewModel extends AndroidViewModel {

    private GoogleMap map;
    private MutableLiveData<List<Location>> friendLocations;

    private Authentication authentication;
    private Repository repository;

    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    // Constructor
    public mainViewModel(@NonNull Application application) {
        super(application);

        // Purely for testing... createTestLocations() causes me to not be able to create the viewModel
        if(friendLocations == null){
            List<Location> locations = new ArrayList<>();

            friendLocations = new MutableLiveData<>();
            friendLocations.setValue(locations);
            //createTestLocations(); // for developing
        }

        // Authentication stuff
        repository = Repository.getInstance();
        authentication = new Authentication();
        userLiveData = authentication.getUserLiveData();
        loggedOutLiveData = authentication.getLoggedOutLiveData();

        //friendLocations = getFriendLocations();
    }

    public void setUserLocation(Location userLocation) {
        String userId = authentication.getUserLiveData().getValue().getUid();
        String latitude = String.valueOf(userLocation.getLatitude());
        String longitude = String.valueOf(userLocation.getLongitude());

        repository.setUserLocation(userId,latitude, longitude);
    }

    public MutableLiveData<List<Location>> getFriendLocations() {
        return friendLocations;
        //return repo.getFriendLocations(); // Doesn't exist yet.
    }

    private void addMarker(Location location, GoogleMap googleMap) {
        if(location != null){
            LatLng friendLocation = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d(Constants.DEBUG, "Hangoutz addMarker: " + friendLocation);

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

    public void showDialogue(Marker marker) {

        // TODO: Implement this properly.
        /* Get data from user. Including picture, what they typed in the activity hint,
         * what they set the energy barometer to and how much time left (maybe?)
         */

        //int iconId = auth.getUserLiveData().getValue()
        //repo.getUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplication().getApplicationContext())
                //.setIcon()
                //.setMessage(auth.getUserLiveData().getValue().getActivity() + "\n Energy Level: " + userLiveData.getValue().getEnergyLevel() + "%")
                .setTitle("Hangout with " + authentication.getUserLiveData().getValue().getDisplayName()) // TODO: Change to get friends display name
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(R.string.joinHangout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyFriend();
                    }
                });
        builder.create().show();
    }

    //TODO: Implement this. It has to notify your friend that you joined the hangout.
    private void notifyFriend() {
        Toast.makeText(getApplication().getApplicationContext(), "You joined the hangout!", Toast.LENGTH_SHORT).show();
    }

    // This method is purely for testing -> nice to have ;)
    public void createTestLocations() {
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

    // get and set methods for maps

    public void setMap(GoogleMap googleMap) {
        map = googleMap;
    }

    public GoogleMap getMap(){
        return map;
    }

    //################## START Authentication methods ##################//
    public void logOut(){
        authentication.logOut();
    }

    public void setStatusAsLoggedOut(){
        repository.setLoggedInUserID(null);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData(){
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }
    //################# END Authentication methods ###################//

    //################# START Repository methods #####################//

    public void setActivity(String activity){
        repository.setActivity(activity);
    }

    public void setEnergy(int energy){
        repository.setEnergy(energy);
    }

    public void setActive(Boolean state){
        repository.setActive(state);
    }
    //################## END Repository methods ######################//
}
