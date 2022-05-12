// Inspired from SWMAD lecture 9 (Sensors, Location and Maps)

package dk.au.mad22spring.AppProject.Group13;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.AppProject.Group13.model.User;
import dk.au.mad22spring.AppProject.Group13.viewmodel.mainViewModel;

public class MapsFragment extends Fragment {

    private mainViewModel viewModel;
    private GoogleMap map;
    private List<Location> friendLocations;
    private LatLng Denmark;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            map = googleMap;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(Denmark,7F));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        Denmark = new LatLng(56.2637, 9.5118);
        friendLocations = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(mainViewModel.class);
    }

    public void updateMap(List<User> friendList) {

        // Clear all markers on map. To avoid doubles
        deleteMapMarkers();

        // Add all the updated markers.
        if(friendList != null){
            addFriendMarkers(friendList);
        }
    }

    private void addMarker(User user) {
        if(user != null){
            Location location = new Location("");
            location.setLongitude(Double.parseDouble(user.longitude));
            location.setLatitude(Double.parseDouble(user.latitude));
            LatLng friendLocation = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d(Constants.DEBUG, "MapsFragment addMarker: " + friendLocation);

            if(map != null){
                Log.d(Constants.DEBUG, "addMarker: map != null");
                map.addMarker(new MarkerOptions().position(friendLocation).title("Hangout with " + user.name).snippet(user.activity + " | Energy " + user.energy + "%"));
            }
        }
    }

    private void addFriendMarkers(List<User> friends) {
        int i = 0;
        for(User u : friends){
            if(u != null) {
                // TODO: remove fixed latitude
                u.latitude=String.valueOf(Double.parseDouble(u.latitude+i));
                u.longitude=String.valueOf(Double.parseDouble(u.longitude+i));
                addMarker(u);
            }
        }
    }

    private void deleteMapMarkers() {
        // Deletes all map markers, etc.
        if(map != null)
        {
            map.clear();
        }
    }
}