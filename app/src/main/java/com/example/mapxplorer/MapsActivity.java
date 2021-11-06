package com.example.mapxplorer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.mapxplorer.User.User;
import com.example.mapxplorer.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCircleClickListener {
    private GoogleMap googleMap;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.mapxplorer.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);


        Button addMarket = findViewById(R.id.addMarket);
        Button listMarkets = findViewById(R.id.list);
        if(DataBase.ActiveSessionUser.getEmail().equals("NULL")){
            addMarket.setClickable(false);
            addMarket.setAlpha(0);
        }
        else addMarket.setOnClickListener(v -> addMarket());
        listMarkets.setOnClickListener(v -> list());
        constraintLayout = findViewById(R.id.constraintLayout);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json));
        // create point on VNTU
        LatLng vinnitsa = new LatLng(49.2344160049607, 28.411152669550056);
        // add marker on map by point
        googleMap.addMarker(new MarkerOptions().position(vinnitsa).title("Тута ВНТУ"));
        // focus on this point
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vinnitsa,16.0f));
        // add listener for opening shops
        googleMap.setOnCircleClickListener(this);
        // add circles from DB on map like markets
        for(Market market : DataBase.getAllMarkets()){
            googleMap.addCircle(
                    new CircleOptions().center(
                            new LatLng(
                                    market.getLatitude(),
                                    market.getLongitude())).
                            radius(10.0).
                            fillColor(Color.GREEN).
                            strokeColor(Color.RED).
                            strokeWidth(4).
                            clickable(true));
        }


    }

    @Override
    public void onCircleClick(@NonNull Circle circle) {
        for(Market market : DataBase.getAllMarkets()){
            if(market.getLatitude() == circle.getCenter().latitude &&
               market.getLongitude() == circle.getCenter().longitude){
                DataBase.ActiveShowingMarket = market;
                Intent intent = new Intent(this,ShowProductsInMarket.class);
                startActivity(intent);
                break;
            }
        }

    }

    public void addMarket() {
        double latitude = googleMap.getCameraPosition().target.latitude;
        double longitude = googleMap.getCameraPosition().target.longitude;



        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Adding new Market");
        dialog.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.add_market,null);
        dialog.setView(registerWindow);

        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.setPositiveButton("Confirm", (dialogInterface, which) -> {
            MaterialEditText namemarket = registerWindow.findViewById(R.id.nameInput);
            Market market = new Market(Objects.requireNonNull(namemarket.getText()).toString(),latitude,longitude);
                    if (TextUtils.isEmpty(namemarket.toString())) {
                        Snackbar.make(constraintLayout, "Input name", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    // adding market with email user
                    String Uid = FirebaseAuth.getInstance().getUid();
                    if(Uid == null){
                        return;
                    }
                    Map<String,Object> data = new HashMap<>();
                    DataBase.ActiveSessionUser.getMarkets().add(market);
                    DataBase.reference.child(Uid).removeValue();
                    data.put(Uid,DataBase.ActiveSessionUser);
                    DataBase.reference.updateChildren(data).addOnSuccessListener(unused -> {
                        DataBase.users.get(DataBase.users.size()-1).getMarkets().add(market);
                        {
                            googleMap.addCircle(
                                    new CircleOptions().center(
                                            new LatLng(
                                                    market.getLatitude(),
                                                    market.getLongitude())).
                                            radius(10.0).
                                            fillColor(Color.GREEN).
                                            strokeColor(Color.RED).
                                            strokeWidth(4).
                                            clickable(true));
                        } // adding circle on map
                    });
                });
        dialog.show();
    }


    public void list() {
        Intent intent = new Intent(this,Search.class);
        startActivity(intent);
    }


}