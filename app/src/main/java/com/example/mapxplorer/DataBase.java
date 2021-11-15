package com.example.mapxplorer;

import com.example.mapxplorer.Market.Category;
import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.User.User;
import com.google.android.gms.maps.model.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DataBase {
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference  reference = DataBase.database.getReference("Users");

    static User ActiveSessionUser = new User("NULL","NULL","NULL");
    static Market ActiveShowingMarket = new Market("NULL",0,0);
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Circle> circles = new ArrayList<>();
    static Category category = new Category();

    public static ArrayList<Market> getAllMarkets(){
        ArrayList<Market> markets = new ArrayList<>();
        for(int i = 0; i < users.size();i++){
            if(users.get(i).getMarkets().size() > 0){
                markets.addAll(users.get(i).getMarkets());
            }
        }
        return markets;
    }
}
