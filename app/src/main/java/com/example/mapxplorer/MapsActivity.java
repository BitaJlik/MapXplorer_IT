package com.example.mapxplorer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.mapxplorer.Market.Market;
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
import com.rengwuxian.materialedittext.MaterialEditText;

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                LayoutInflater inflater = LayoutInflater.from(this);
                View info = inflater.inflate(R.layout.activity_info_market,null);
                dialog.setView(info);
                Button button = info.findViewById(R.id.toProducts);
                TextView nameMarket = info.findViewById(R.id.nameMarket);
                TextView infoMarketOwner = info.findViewById(R.id.contactInfoOwner);
                TextView infoMarketEmail = info.findViewById(R.id.contactInfoEmail);
                TextView openTime = info.findViewById(R.id.timeMarket);
                nameMarket.setText(DataBase.ActiveShowingMarket.getNameMarket());
                openTime.setText(DataBase.ActiveShowingMarket.getOpenTime());
                infoMarketOwner.setText(DataBase.ActiveShowingMarket.getOwner());
                infoMarketEmail.setText(DataBase.ActiveShowingMarket.getEmail());
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(this, ShowProductsInMarket.class);
                    startActivity(intent);
                });
                dialog.show();
            }
        }

    }

    public void addMarket() {
        if(DataBase.ActiveSessionUser.getMarkets().size() == DataBase.ActiveSessionUser.getSizeMaxMarkets()){
            Snackbar.make(constraintLayout,"Reached maximum limit markets \n Pay for more :) ",Snackbar.LENGTH_LONG).show();
            return;
        }
        double latitude = googleMap.getCameraPosition().target.latitude;
        double longitude = googleMap.getCameraPosition().target.longitude;



        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        AlertDialog alertDialog = dialog.create();
        dialog.setTitle("Adding new Market");
        dialog.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.add_market,null);

        EditText openHours = registerWindow.findViewById(R.id.openHours);
        EditText openMinutes = registerWindow.findViewById(R.id.openMinutes);
        EditText closingHours = registerWindow.findViewById(R.id.closingHours);
        EditText closingMinutes = registerWindow.findViewById(R.id.closingMinutes);
        MaterialEditText namemarket = registerWindow.findViewById(R.id.nameInput);
        Button addMarketR = registerWindow.findViewById(R.id.addMarketR);

        openHours.setText("00");
        openMinutes.setText("00");
        closingHours.setText("23");
        closingMinutes.setText("59");

        openHours.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(Integer.parseInt(openHours.getText().toString()) > 23){
                        openHours.setText(String.valueOf(23));
                    }
                }
                catch (NumberFormatException e){
                    openHours.setText(String.valueOf(0));
                }

            }

            @Override public void afterTextChanged(Editable s) { }
        });
        closingHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(Integer.parseInt(closingHours.getText().toString()) > 23){
                        closingHours.setText(String.valueOf(23));
                    }
                }
                catch (NumberFormatException e){
                    closingHours.setText(String.valueOf(0));
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override public void afterTextChanged(Editable s) {

            }
        });
        openMinutes.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(Integer.parseInt(openMinutes.getText().toString()) > 59){
                        openMinutes.setText(String.valueOf(59));
                    }
                }
                catch (NumberFormatException e){
                    openMinutes.setText(String.valueOf(0));
                }
            }

            @Override public void afterTextChanged(Editable s) { }
        });
        closingMinutes.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(Integer.parseInt(closingMinutes.getText().toString()) > 59){
                        closingMinutes.setText(String.valueOf(59));
                    }
                }
                catch (NumberFormatException e){
                    closingMinutes.setText(String.valueOf(0));
                }
            }

            @Override public void afterTextChanged(Editable s) { }
        });
        addMarketR.setOnClickListener(v -> {

            Market market = new Market(Objects.requireNonNull(namemarket.getText()).toString(),latitude,longitude);
            market.setOwner(DataBase.ActiveSessionUser.getName());
            market.setEmail(DataBase.ActiveSessionUser.getEmail());
            market.setOpenTime(openHours.getText().toString() +":"+openMinutes.getText().toString() + " — " +
                    closingHours.getText().toString() + ":" + closingMinutes.getText().toString());
            if (namemarket.getText().toString().length() > 2) {
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
            }
            finish();
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        });

        dialog.setView(registerWindow);


        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss(); });

        dialog.show();
    }


    public void list() {
        Intent intent = new Intent(this,Search.class);
        startActivity(intent);
    }


}