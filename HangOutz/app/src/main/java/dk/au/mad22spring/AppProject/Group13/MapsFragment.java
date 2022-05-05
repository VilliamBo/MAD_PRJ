package dk.au.mad22spring.AppProject.Group13;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.location.Location;
import android.os.Bundle;
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

import java.util.List;

import dk.au.mad22spring.AppProject.Group13.viewmodel.hangOutzViewModel;
import dk.au.mad22spring.AppProject.Group13.viewmodel.mainViewModel;

public class MapsFragment extends Fragment {

    private hangOutzViewModel viewModel;
    private GoogleMap map;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            map = googleMap;
            viewModel.updateMap(map);
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
        viewModel = new ViewModelProvider(this).get(hangOutzViewModel.class);

        // Observer to update location data when changes happen
        viewModel.getFriendLocations().observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locationList) {
                viewModel.updateMap(map);
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                         @Override
                                         public boolean onMarkerClick(@NonNull Marker marker) {
                                             viewModel.showDialogue(marker);
                                             // TODO: Implement a dialog pop-up
                                             return false;
                                         }
                                     });

        // TODO: Fix map boundaries.
    }
}