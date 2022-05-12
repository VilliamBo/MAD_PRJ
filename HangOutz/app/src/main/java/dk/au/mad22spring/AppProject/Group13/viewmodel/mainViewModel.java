package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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
import dk.au.mad22spring.AppProject.Group13.model.User;

public class mainViewModel extends AndroidViewModel {

    private GoogleMap map;
    private MutableLiveData<List<Location>> friendLocations;

    private Authentication authentication;
    private Repository repository;

    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<ArrayList<User>> friendList;
    private MutableLiveData<ArrayList<String>> friendIdList;

    private MutableLiveData<User> localUserLiveData;

    // Constructor
    public mainViewModel(@NonNull Application application) {
        super(application);

        if(friendLocations == null){
            friendLocations = new MutableLiveData<>();
            friendLocations.setValue(new ArrayList<>());
        }

        // Authentication stuff
        repository = Repository.getInstance();
        authentication = new Authentication();
        userLiveData = authentication.getUserLiveData();
        loggedOutLiveData = authentication.getLoggedOutLiveData();
        linkFriendIdListToDatabase();
    }

    public void setUserLocation(Location userLocation) {
        String userId = authentication.getUserLiveData().getValue().getUid();
        String latitude = String.valueOf(userLocation.getLatitude());
        String longitude = String.valueOf(userLocation.getLongitude());

        repository.setUserLocation(userId,latitude, longitude);
    }

    public void showDialogue(Marker marker) {

        // TODO: Implement this properly.
        /* Get data from user. Including picture, what they typed in the activity hint,
         * what they set the energy barometer to and how much time left (maybe?)
         */
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

    public MutableLiveData<ArrayList<User>> getFriendList(){
        if(friendList == null) {
            friendList = new MutableLiveData<ArrayList<User>>();
            friendList.setValue(new ArrayList<>());
        }
        return friendList;
    }

    public MutableLiveData<ArrayList<String>> getFriendIdList(){
        if(friendIdList == null) {
            friendIdList = new MutableLiveData<ArrayList<String>>();
            friendIdList.setValue(new ArrayList<>());
        }
        return friendIdList;
    }

    public void updateFriendList() {
        repository.getUsersFromId(getFriendList(), getFriendIdList().getValue());
    }

    // For location ON/OFF state
    public MutableLiveData<User> getLocalUserLiveData() {
        if(localUserLiveData == null){
            localUserLiveData = new MutableLiveData<>();
        }
        return localUserLiveData;
    }

    public void getLocalUser(){
        repository.getLocalUser(localUserLiveData);
    }

    public String getLoggedInUserId(){
        return repository.getLoggedInUSerID();
    }

    //Links Friend id list to friend.child(userid). if it updates
    private void linkFriendIdListToDatabase(){
        repository.getFriendsId(getFriendIdList());
    }
    //################## END Repository methods ######################//
}
