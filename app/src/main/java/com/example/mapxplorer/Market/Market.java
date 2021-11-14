package com.example.mapxplorer.Market;

import android.media.Image;
import android.widget.ImageView;

import com.example.mapxplorer.R;
import com.example.mapxplorer.User.User;

import java.util.ArrayList;
public class Market {
    public enum SizeMarket {SMALL,MEDIUM,LARGE,BIG}

    private String nameMarket;
    private ArrayList<Category> categories;
    private double latitude;
    private double longitude;
    private String owner;
    private String email;
    private String openTime;
    private Image image;
    private SizeMarket sizeMarket;

    public Market(){
        categories = new ArrayList<>();
    }

    public Market(String nameMarket, double latitude, double longitude) {
        this.nameMarket = nameMarket;
        this.latitude = latitude;
        this.longitude = longitude;
        categories = new ArrayList<>();
    }

    public String getNameMarket() { return nameMarket; }
    public void setNameMarket(String nameMarket) { this.nameMarket = nameMarket; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getID(){return String.valueOf(latitude)+String.valueOf(longitude);}
    public void setOwner(String owner) { this.owner = owner; }
    public String getOwner(){ return owner; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOpenTime() { return openTime; }
    public void setOpenTime(String openTime) { this.openTime = openTime; }
    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image;}
    public SizeMarket getSizeMarket() { return sizeMarket; }
    public void setSizeMarket(SizeMarket sizeMarket) { this.sizeMarket = sizeMarket; }
    public ArrayList<Category> getCategories() { return categories; }
    public void setCategories(ArrayList<Category> categories) { this.categories = categories; }

    @Override public String toString() {
        return "Market{" +
                "\nnameMarket='" + nameMarket  +
                ", latitude=" + latitude +
                categories.toString() +
                ", longitude=" + longitude +
                getID() +
                '}';
    }
}
