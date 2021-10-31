package com.example.mapxplorer;

import androidx.fragment.app.FragmentActivity;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapxplorer.databinding.ActivityMapsBinding;

import java.lang.annotation.Target;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng vinnitsa = new LatLng(49.2344160049607, 28.411152669550056);
        mMap.addMarker(new MarkerOptions().position(vinnitsa).title("Тута ВНТУ"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vinnitsa,16.0f));
        g = googleMap;
    }
    private Object g ;
    public void addMarket(View view) {
        GoogleMap googleMap = (GoogleMap) g;
        googleMap.addMarker(new MarkerOptions().position(googleMap.getCameraPosition().target));
        System.out.println("Added" + googleMap.getCameraPosition().target.latitude +"\t"+ googleMap.getCameraPosition().target.longitude);
    }
    public void search(View view) {
        GoogleMap googleMap = (GoogleMap) g;
    }
}