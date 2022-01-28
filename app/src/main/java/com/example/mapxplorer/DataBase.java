package com.example.mapxplorer;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.mapxplorer.Market.Category;
import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.User.Comment;
import com.example.mapxplorer.User.User;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataBase {
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference reference = DataBase.database.getReference("Users");
    public static FragmentManager fragmentManager;
    static int sizeUsers = 0;
    static int sizeMarkets = 0;
    static {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataBase.users.clear();
                if(MyCustomMapFragment.googleMap != null){
                    MyCustomMapFragment.clear();
                }

                for (DataSnapshot snapuser : snapshot.getChildren()) { // UiD User
                    User user = snapuser.getValue(User.class);
                    System.out.println(user);
                    if (user == null) break;
                    DataBase.users.add(user); // adding in LOCAL DB

                    ArrayList<Market> markets = new ArrayList<>(); // Markets for user
                    // getting from ONLINE DB from user markets
                    for (DataSnapshot dataSnapshot : snapuser.child("markets").getChildren()) {
                        Market market = dataSnapshot.getValue(Market.class);
                        markets.add(market);
                    }
                    user.setMarkets(markets);

                    if(ActiveSessionUser.getEmail().equals(user.getEmail())){
                        ActiveSessionUser.setId(user.getId());
                        auth.signInWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnSuccessListener(authResult -> {
                            ActiveSessionUser.setSizeMaxMarkets(user.getSizeMaxMarkets());
                            ActiveSessionUser.setMarkets(user.getMarkets());
                            for(int i = 0;i < user.getMarkets().size();i++){
                                if(user.getMarkets().get(i).getEmail().equals(ActiveShowingMarket.getEmail())){
                                    ActiveShowingMarket.setComments(user.getMarkets().get(i).getComments());
                                    ActiveShowingMarket.setCategories(user.getMarkets().get(i).getCategories());
                                }
                            }
                        });
                    }
                }

                if (sizeMarkets <= DataBase.getAllMarkets().size() | sizeUsers <= DataBase.users.size()) {
                    MyCustomMapFragment.drawMarkets();
                    sizeMarkets = DataBase.getAllMarkets().size();
                    sizeUsers = DataBase.users.size();
                }

            }

            @Override public void onCancelled(@NonNull DatabaseError error) { }
        };
        DataBase.reference.addValueEventListener(listener);
    }
    public static String search;
    // public static User ActiveSessionUser = new User("BitaJlik","qweqwe","vtkach4488@gmail.com");
    public static User ActiveSessionUser = new User("NULL","NULL","NULL");
    public static Market ActiveShowingMarket = new Market("NULL",0,0);
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Circle> circles = new ArrayList<>();
    public static Category category = new Category();
    public static int posCategory = 0;
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
