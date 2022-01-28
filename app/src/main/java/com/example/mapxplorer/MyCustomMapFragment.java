package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.mapxplorer.Market.Bottom_Dialog;
import com.example.mapxplorer.Market.Market;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyCustomMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnGroundOverlayClickListener {
    @SuppressLint("StaticFieldLeak") private static Context thisContext;
    public static GoogleMap googleMap;
    public static boolean onMarket = false;
    @Override public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setUpMapIfNeeded();
        MainActivity.setListener();
    }
    private void setUpMapIfNeeded() {

        thisContext = this.getContext();

        getMapAsync(this);

    }
    @Override public void onMapReady(@NonNull GoogleMap googleMap) {
        MyCustomMapFragment.googleMap = googleMap;

        if(MainActivity.isSatellite){
            MyCustomMapFragment.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else MyCustomMapFragment.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        assert thisContext != null;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(thisContext,R.raw.style_json));

        // add listener for opening shops
        googleMap.setOnGroundOverlayClickListener(this);
        // add circles from DB on map like markets
        drawMarkets();
        // focus on this point
        if(onMarket){
            LatLng market = new LatLng(DataBase.ActiveShowingMarket.getLatitude()+0.00034, DataBase.ActiveShowingMarket.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions().position(market);
            googleMap.setOnMarkerClickListener(marker -> true);
            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(market,16.0f));
            onMarket = false;
        }
        else googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.23522374140626, 28.4118144775948),16.0f));

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
    public static void clear(){ googleMap.clear(); }
    @Override public void onGroundOverlayClick (@NonNull GroundOverlay groundOverlay) {
        for(Market market : DataBase.getAllMarkets()){
            if(market.getLatitude() == groundOverlay.getPosition().latitude &&
                    market.getLongitude() == groundOverlay.getPosition().longitude){
                DataBase.ActiveShowingMarket = market;

                // Expand block
                Bottom_Dialog dialog = Bottom_Dialog.newInstance();
                dialog.show(getChildFragmentManager(),"bottom");

            }
        }

    }
    public static double getLatitude(){ return googleMap.getCameraPosition().target.latitude; }
    public static double getLongitude(){ return googleMap.getCameraPosition().target.longitude; }


}