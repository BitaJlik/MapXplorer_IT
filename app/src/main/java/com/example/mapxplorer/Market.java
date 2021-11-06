package com.example.mapxplorer;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Market {

    private String nameMarket;
    private ArrayList<Product> products = new ArrayList<>();
    private double latitude;
    private double longitude;

    public Market(){
        products = new ArrayList<>();
    }

    public Market(String nameMarket, double latitude, double longitude) {
        this.nameMarket = nameMarket;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNameMarket() { return nameMarket; }
    public void setNameMarket(String nameMarket) { this.nameMarket = nameMarket; }
    public ArrayList<Product> getProducts() { return products; }
    public void setProducts(ArrayList<Product> products){ this.products = products;}
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getID(){return String.valueOf(latitude)+String.valueOf(longitude);}
        @Override
    public String toString() {
        return "Market{" +
                "nameMarket='" + nameMarket  +
                ", products=" + products +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                getID() +
                '}';
    }
}
