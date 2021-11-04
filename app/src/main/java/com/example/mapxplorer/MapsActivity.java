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

import com.example.mapxplorer.User.GetCurrentUserFromDB;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCircleClickListener {
    private Object g ;
    public static String idUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Markets");
        constraintLayout = findViewById(R.id.constraintLayout);
        GetCurrentUserFromDB.getUser();

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
        g = googleMap;
    }

    @Override
    public void onCircleClick(@NonNull Circle circle) {
        int i=0; // reference on other activity
        for(Market market : DataBase.markets){
            if(market.getLatitude() == circle.getCenter().latitude &&
               market.getLongitude() == circle.getCenter().longitude){
                Toast.makeText(this,market.getNameMarket() + circle.getCenter().latitude,Toast.LENGTH_SHORT).show();
                DataBase.id = i;
                Intent intent = new Intent(this, ShowProductsInMarket.class);
                startActivity(intent);
            }
            i++;
        }

    }

    public void addMarket(View view) {
        FirebaseAuth firebaseAuthuser;
        FirebaseDatabase firebaseDatabaseuser;
        DatabaseReference databaseReferenceuser;

        GoogleMap googleMap = (GoogleMap) g;
        double latitude = googleMap.getCameraPosition().target.latitude;
        double longitude = googleMap.getCameraPosition().target.longitude;


        Market market = new Market("Market",latitude,longitude);
        DataBase.markets.add(market);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Adding new Market");
        dialog.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.add_market,null);
        dialog.setView(registerWindow);

        MaterialEditText email = registerWindow.findViewById(R.id.emailInput);
        MaterialEditText password = registerWindow.findViewById(R.id.passwordInput);
        MaterialEditText namemarket = registerWindow.findViewById(R.id.nameInput);

        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.setPositiveButton("Confirm", (dialogInterface, which) -> {
            if(TextUtils.isEmpty(email.toString())){
                Snackbar.make(constraintLayout,"Input email",Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(namemarket.toString())){
                Snackbar.make(constraintLayout,"Input name",Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(Objects.requireNonNull(password.getText()).toString().length() < 5){
                Snackbar.make(constraintLayout,"Input password more than 5 symbols",Snackbar.LENGTH_SHORT).show();
                return;
            }

            // adding market with email user

            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        googleMap.addCircle(
                                new CircleOptions().center(
                                        new LatLng(latitude,longitude)).
                                        radius(12.0).
                                        fillColor(Color.BLUE).
                                        strokeColor(Color.RED).
                                        strokeWidth(5).
                                        clickable(true));
                        databaseReference.child(Objects.requireNonNull(
                                FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(market)
                                .addOnSuccessListener(unused -> Snackbar.make(constraintLayout,"Success",Snackbar.LENGTH_SHORT).show());
                    });


        });

        dialog.show();
    }


    public void search(View view) {
        Intent intent = new Intent(this,Search.class);
        startActivity(intent);
    }


}