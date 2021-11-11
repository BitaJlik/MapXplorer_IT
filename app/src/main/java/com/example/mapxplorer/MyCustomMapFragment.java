package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.mapxplorer.Market.Market;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyCustomMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnGroundOverlayClickListener {
    @SuppressLint("StaticFieldLeak")
    private static Context thisContext;
    public static GoogleMap googleMap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        thisContext = this.getContext();

        getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MyCustomMapFragment.googleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        assert thisContext != null;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(thisContext,R.raw.style_json));
        // create point on VNTU
        LatLng vinnitsa = new LatLng(49.2344160049607, 28.411152669550056);
        MarkerOptions markerOptions = new MarkerOptions().position(vinnitsa).title("Тута ВНТУ");

        // add marker on map by point
        googleMap.addMarker(markerOptions);
        // focus on this point
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vinnitsa,16.0f));
        // add listener for opening shops
        googleMap.setOnGroundOverlayClickListener(this);
        // add circles from DB on map like markets
        drawMarkets();
    }
    public static void drawMarkets(){
        if(!DataBase.circles.isEmpty()){
            for(Circle circle : DataBase.circles){
                circle.remove();
            }
        }
        if(googleMap != null){
            for(Market market : DataBase.getAllMarkets()){
                if(market.getSizeMarket() == Market.SizeMarket.SMALL){
                    MyCustomMapFragment.googleMap.addGroundOverlay(new GroundOverlayOptions().position(
                            new LatLng(market.getLatitude(),market.getLongitude()),60,60)
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.small)).clickable(true));
                }
                if(market.getSizeMarket() == Market.SizeMarket.MEDIUM){
                    MyCustomMapFragment.googleMap.addGroundOverlay(new GroundOverlayOptions().position(
                            new LatLng(market.getLatitude(),market.getLongitude()),75,75)
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.medium)).clickable(true));
                }
                if(market.getSizeMarket() == Market.SizeMarket.LARGE){
                    MyCustomMapFragment.googleMap.addGroundOverlay(new GroundOverlayOptions().position(
                            new LatLng(market.getLatitude(),market.getLongitude()),100,100)
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.large)).clickable(true));
                }
                if(market.getSizeMarket() == Market.SizeMarket.BIG){
                    MyCustomMapFragment.googleMap.addGroundOverlay(new GroundOverlayOptions().position(
                            new LatLng(market.getLatitude(),market.getLongitude()),120,120)
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.big)).clickable(true));
                }
            }
        }
    }
    public static void clear(){
        googleMap.clear();
    }
    @Override public void onGroundOverlayClick (@NonNull GroundOverlay groundOverlay) {
        for(Market market : DataBase.getAllMarkets()){
            if(market.getLatitude() == groundOverlay.getPosition().latitude &&
                    market.getLongitude() == groundOverlay.getPosition().longitude){
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
    public static double getLatitude(){
        return googleMap.getCameraPosition().target.latitude;
    }
    public static double getLongitude(){
        return googleMap.getCameraPosition().target.longitude;
    }


}