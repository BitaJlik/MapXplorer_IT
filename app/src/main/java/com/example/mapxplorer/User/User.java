package com.example.mapxplorer.User;

import androidx.annotation.NonNull;

import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.Market.Market;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User  {
    private String name,password,email,Id;
    private ArrayList<Market> markets;
    private int sizeMaxMarkets = 0;
    public User(){
         markets = new ArrayList<>();
    }

    public User(String name, String pass, String email) {
        this.name = name;
        this.password = pass;
        this.email = email;
        markets = new ArrayList<>();
    }

    public User(String name, String pass, String email, ArrayList<Market> markets) {
        this.name = name;
        this.password = pass;
        this.email = email;
        this.markets = markets;
    }

    // TODO: 22.12.2021 Make methods Update from DB and Getting info

    public void updateUserInDB(){
        DataBase.auth.signInWithEmailAndPassword(DataBase.ActiveSessionUser.getEmail(),
                DataBase.ActiveSessionUser.getPassword())
                .addOnSuccessListener(authResult -> {
                    String Uid = FirebaseAuth.getInstance().getUid();
                    if(Uid == null){
                        return;
                    }
                    Map<String,Object> data = new HashMap<>();

                    ArrayList<Market> markets = new ArrayList<>(DataBase.ActiveSessionUser.getMarkets());
                    data.put("markets",markets);
                    DataBase.reference.child(Uid).child("markets").removeValue();
                    DataBase.reference.child(Uid).updateChildren(data);
                });
    }
    public void updateUserFromDB(){

    }

    public ArrayList<Market> getMarkets() { return markets; }
    public void setMarkets(ArrayList<Market> markets) { this.markets = markets; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String pass) { this.password = pass; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getSizeMaxMarkets() { return sizeMaxMarkets; }
    public void setSizeMaxMarkets(int sizeMaxMarkets) { this.sizeMaxMarkets = sizeMaxMarkets; }
    public String getId() { return Id; }
    public void setId(String UId) { this.Id = UId; }

    @NonNull
    @Override
    public String toString() {
        return "\nUser{" +
                "\nname='" + name +
                ", pass='" + password +
                ", email='" + email +
                ", market=" + markets + '\n' +
                getId() +
                sizeMaxMarkets +
                '}';
    }
}
