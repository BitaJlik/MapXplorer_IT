package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.mapxplorer.Market.Market;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyCustomMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnCircleClickListener {
    private static Context thisContext;
    private static GoogleMap googleMap;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        System.out.println("+++");
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        thisContext = this.getContext();

        getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MyCustomMapFragment.googleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        assert thisContext != null;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(thisContext,R.raw.style_json));
        // create point on VNTU
        LatLng vinnitsa = new LatLng(49.2344160049607, 28.411152669550056);
        // add marker on map by point
        googleMap.addMarker(new MarkerOptions().position(vinnitsa).title("Тута ВНТУ"));
        // focus on this point
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vinnitsa,16.0f));
        // add listener for opening shops
        googleMap.setOnCircleClickListener(this);
        // add circles from DB on map like markets
        initMarkets();
    }
    public static void initMarkets(){
        if(googleMap != null){
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
    }
    @Override
    public void onCircleClick(@NonNull Circle circle) {
        for(Market market : DataBase.getAllMarkets()){
            if(market.getLatitude() == circle.getCenter().latitude &&
                    market.getLongitude() == circle.getCenter().longitude){
                DataBase.ActiveShowingMarket = market;
                AlertDialog.Builder dialog = new AlertDialog.Builder(thisContext);
                LayoutInflater inflater = LayoutInflater.from(thisContext);
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
                    Intent intent = new Intent(thisContext, ShowProductsInMarket.class);
                    startActivity(intent);
                });
                dialog.show();
            }
        }

    }
    @SuppressLint("SetTextI18n")
    public void addMarket() {
        if(DataBase.ActiveSessionUser.getMarkets().size() == DataBase.ActiveSessionUser.getSizeMaxMarkets()){
            //Snackbar.make("Reached maximum limit markets \n Pay for more :) ",Snackbar.LENGTH_LONG).show();
            return;
        }
        double latitude = googleMap.getCameraPosition().target.latitude;
        double longitude = googleMap.getCameraPosition().target.longitude;



        AlertDialog.Builder dialog = new AlertDialog.Builder(thisContext);
        dialog.setTitle("Adding new Market");
        dialog.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(thisContext);
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

            Intent intent = new Intent(thisContext,MapsActivity.class);
            startActivity(intent);
        });

        dialog.setView(registerWindow);


        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());

        dialog.show();
    }
    public void list() {
        Intent intent = new Intent(thisContext,Search.class);
        startActivity(intent);
    }

}