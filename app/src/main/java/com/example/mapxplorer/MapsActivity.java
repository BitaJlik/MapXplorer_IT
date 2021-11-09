package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.User.User;
import com.example.mapxplorer.databinding.ActivityMapsBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataBase.auth.signInWithEmailAndPassword("help@mgmail.com","qweqwe");
        if(!isOnline(this)){
            Toast.makeText(this,"No Internet Connection\nPlease restart App",Toast.LENGTH_LONG).show();
        }
        ValueEventListener listener = new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Maps.googleMap != null){
                    DataBase.users.clear();
                }
                for( DataSnapshot snapuser : snapshot.getChildren()){ // UiD User
                    User user = snapuser.getValue(User.class);
                    if(user == null) break;
                    DataBase.users.add(user); // adding in LOCAL DB
                    ArrayList<Market> markets = new ArrayList<>(); // Markets for user
                    // getting from ONLINE DB from user markets
                    for(DataSnapshot dataSnapshot : snapuser.child("markets").getChildren()){
                        Market market = dataSnapshot.getValue(Market.class);
                        markets.add(market);
                    }
                    user.setMarkets(markets);
                }
                for(int i = 0;i < DataBase.users.size();i++){
                    System.out.println(DataBase.users.get(i));
                }
                MyCustomMapFragment.initMarkets();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        };
        DataBase.reference.addValueEventListener(listener);
        super.onCreate(savedInstanceState);
        com.example.mapxplorer.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView view = findViewById(R.id.nav_view);
        view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        onNavigationItemSelected(view.getMenu().findItem(R.id.nav_Map));

    }
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MyCustomMapFragment fragment = new MyCustomMapFragment();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.commit();
        switch (item.getItemId()){
            case R.id.nav_Map:
                fragmentTransaction.replace(R.id.fragment, fragment);
                break;
            case R.id.nav_Login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new LoginFragment()).commit();
                break;
            case R.id.nav_List:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new SearchFragment()).commit();
                break;

        }
        return true;
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}