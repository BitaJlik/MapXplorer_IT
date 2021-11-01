package com.example.mapxplorer;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class Market {
    private String nameMarket;
    private final ArrayList<Product> products = new ArrayList<>();
    private double latitude;
    private double longitude;

    public Market(String nameMarket, double latitude, double longitude) {
        this.nameMarket = nameMarket;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNameMarket() { return nameMarket; }
    public void setNameMarket(String nameMarket) { this.nameMarket = nameMarket; }
    public ArrayList<Product> getProducts() { return products; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @NonNull
    @Override
    public String toString() {
        return "Market{" +
                "nameMarket='" + nameMarket + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
