package com.example.mapxplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.example.mapxplorer.databinding.MapsActivityBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Maps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCircleClickListener {
    public static GoogleMap googleMap;
    private MapsActivityBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MapsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);
//        Button addMarket = findViewById(R.id.addMarket);
//        Button listMarkets = findViewById(R.id.list);
//        if (DataBase.ActiveSessionUser.getEmail().equals("NULL")) {
//            addMarket.setClickable(false);
//            addMarket.setAlpha(0);
//        } else addMarket.setOnClickListener(v -> addMarket());
//        listMarkets.setOnClickListener(v -> list());
//        constraintLayout = findViewById(R.id.constraintLayout);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Maps.googleMap = googleMap;

    }


    @Override
    public void onCircleClick(@NonNull Circle circle) {

    }
}