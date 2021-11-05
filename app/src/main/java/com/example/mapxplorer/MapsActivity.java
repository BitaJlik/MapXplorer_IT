package com.example.mapxplorer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCircleClickListener {
    private Object g ;

    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.mapxplorer.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        constraintLayout = findViewById(R.id.constraintLayout);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // create point on VNTU
        LatLng vinnitsa = new LatLng(49.2344160049607, 28.411152669550056);
        // add marker on map by point
        googleMap.addMarker(new MarkerOptions().position(vinnitsa).title("Тута ВНТУ"));
        // focus on this point
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vinnitsa,16.0f));
        // add listener for opening shops
        googleMap.setOnCircleClickListener(this);
        // add circles from DB on map like markets
        System.out.println(DataBase.online_markets);

        if(!DataBase.online_markets.isEmpty()){
            for(Market market : DataBase.online_markets){
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
        g = googleMap;
    }

    @Override
    public void onCircleClick(@NonNull Circle circle) {
        for(Market market : DataBase.online_markets){
            if(market.getLatitude() == circle.getCenter().latitude &&
               market.getLongitude() == circle.getCenter().longitude){
                Toast.makeText(this,market.getNameMarket(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ShowProductsInMarket.class);
                startActivity(intent);
            }
        }
    }

    public void addMarket(View view) {
        GoogleMap googleMap = (GoogleMap) g;
        double latitude = googleMap.getCameraPosition().target.latitude;
        double longitude = googleMap.getCameraPosition().target.longitude;


        Market market = new Market(DataBase.user.getName(),latitude,longitude);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Adding new Market");
        dialog.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.add_market,null);
        dialog.setView(registerWindow);


//        String email = Objects.requireNonNull(DataBase.auth.getCurrentUser()).getEmail();
//        String password = DataBase.reference.child(Objects.requireNonNull(DataBase.auth.getUid())).child("password").getKey();
        MaterialEditText namemarket = registerWindow.findViewById(R.id.nameInput);

        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.setPositiveButton("Confirm", (dialogInterface, which) -> {

                    if (TextUtils.isEmpty(namemarket.toString())) {
                        Snackbar.make(constraintLayout, "Input name", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    // adding market with email user
                    Map<String,Object> data = new HashMap<>();
                    data.put(String.valueOf(DataBase.users.get(DataBase.users.size()-1).getMarkets().size()),market);
                    DataBase.reference.child(Objects.requireNonNull(DataBase.auth.getUid())).child("markets")
                            .updateChildren(data).addOnSuccessListener(unused -> {
                        DataBase.online_markets.add(market);
                        {
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
                        } // adding circle on map
                    });

                });

        dialog.show();
    }


    public void search(View view) {
        Intent intent = new Intent(this,Search.class);
        startActivity(intent);
    }


}