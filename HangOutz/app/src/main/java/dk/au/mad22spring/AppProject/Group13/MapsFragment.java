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

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            map = googleMap;

            /*
            if(friendLocations != null){
                Log.d(Constants.DEBUG, "onMapReady: Calling updateMap from MapFragment" );
                updateMap(friendLocations);
            }

            /*
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    viewModel.showDialogue(marker);
                    // TODO: Implement a dialog pop-up
                    return false;
                }
            });

             */
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

        friendLocations = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(mainViewModel.class);

        // Observer to update location data when changes happen
        /*
        viewModel.getFriendLocations().observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locationList) {
                friendLocations = locationList;
                updateMap(friendLocations);
            }
        });

         */
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
                map.addMarker(new MarkerOptions().position(friendLocation).title(user.name).snippet("Energy " + user.energy + "% " + user.activity));
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