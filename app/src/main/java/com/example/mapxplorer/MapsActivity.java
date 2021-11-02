package com.example.mapxplorer;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapxplorer.databinding.ActivityMapsBinding;

import java.lang.annotation.Target;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCircleClickListener {

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng vinnitsa = new LatLng(49.2344160049607, 28.411152669550056);
        mMap.addMarker(new MarkerOptions().position(vinnitsa).title("Тута ВНТУ"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vinnitsa,16.0f));
        g = googleMap;
        mMap.setOnCircleClickListener(this);
        if(!DataBase.markets.isEmpty()){
            for(Market market : DataBase.markets){

                googleMap.addCircle(
                        new CircleOptions().center(
                        new LatLng(
                                market.getLatitude(),
                                market.getLongitude())).
                        radius(10.0).
                        fillColor(Color.BLUE).
                        strokeColor(Color.RED).
                        strokeWidth(5).
                        clickable(true));


            }
        }
    }
    private Object g ;
    @Override
    public void onCircleClick(@NonNull Circle circle) {
        int i=0;
        for(Market market : DataBase.markets){

            if(market.getLatitude() == circle.getCenter().latitude &&
               market.getLongitude() == circle.getCenter().longitude){
                Toast.makeText(this,market.getNameMarket() + circle.getCenter().latitude,Toast.LENGTH_SHORT).show();
                DataBase.id = i;
                Intent intent = new Intent(this, MarketShowProducts.class);
                startActivity(intent);
            }
            i++;
        }

    }

    public void addMarket(View view) {
        GoogleMap googleMap = (GoogleMap) g;
        double latitude = googleMap.getCameraPosition().target.latitude;
        double longitude = googleMap.getCameraPosition().target.longitude;

        googleMap.addCircle(
                new CircleOptions().center(
                        new LatLng(latitude,longitude)).
                        radius(10.0).
                        fillColor(Color.BLUE).
                        strokeColor(Color.RED).
                        strokeWidth(5).
                        clickable(true));
        System.out.println("Added " + latitude +"\t"+ longitude);
        DataBase.markets.add(
                new Market("Market",latitude,longitude));
    }
    public void search(View view) {
        String finding = "";
        for(Market market : DataBase.markets ){
            if(finding.equals(market.getNameMarket())){
                ((GoogleMap) g).moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(market.getLatitude(),market.getLongitude()),16.0f));
            }
            System.out.println("\n"+market.toString());
        }
    }


}