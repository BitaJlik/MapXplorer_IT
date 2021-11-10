package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.User.User;
import com.example.mapxplorer.databinding.ActivityMapsBinding;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;

public class MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean isViewMap = true;
    private DrawerLayout drawer;
    public static NavigationView view;
    ActionBarDrawerToggle toggle;
    int sizeUsers = 0;
    int sizeMarkets = 0;
    public static TextView name ;
    public static TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isOnline(this)) {
            Toast.makeText(this, "No Internet Connection\nPlease restart App", Toast.LENGTH_LONG).show();
        }
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataBase.users.clear();
                MyCustomMapFragment.clear();

                for (DataSnapshot snapuser : snapshot.getChildren()) { // UiD User
                    User user = snapuser.getValue(User.class);
                    if (user == null) break;
                    DataBase.users.add(user); // adding in LOCAL DB
                    ArrayList<Market> markets = new ArrayList<>(); // Markets for user
                    // getting from ONLINE DB from user markets
                    for (DataSnapshot dataSnapshot : snapuser.child("markets").getChildren()) {
                        Market market = dataSnapshot.getValue(Market.class);
                        markets.add(market);
                    }
                    user.setMarkets(markets);
                }
                for (int i = 0; i < DataBase.users.size(); i++) {
                    System.out.println(DataBase.users.get(i));
                }
                if (sizeMarkets <= DataBase.getAllMarkets().size() | sizeUsers <= DataBase.users.size()) {

                    MyCustomMapFragment.drawMarkets();
                    sizeMarkets = DataBase.getAllMarkets().size();
                    sizeUsers = DataBase.users.size();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        DataBase.reference.addValueEventListener(listener);
        super.onCreate(savedInstanceState);
        com.example.mapxplorer.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        SearchView search = toolbar.findViewById(R.id.search);
        setSupportActionBar(toolbar);




        Geocoder geocoder = new Geocoder(this);
        final List<Address>[] addresses = new List[1];
        search.setOnSearchClickListener(v -> {
            try {
                addresses[0] = geocoder.getFromLocationName(search.toString(),1);
            } catch (IOException e) {
                Log.i("GEO","Not found");
            }
            if(addresses[0].size() > 0){
                Address address = addresses[0].get(0);
                MyCustomMapFragment.googleMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(new LatLng(address.getLatitude(),address.getLongitude()),16.0f));
            }
        });

        drawer = findViewById(R.id.drawer_layout);

        NavigationView view = findViewById(R.id.nav_view);
        MapsActivity.view = view;
        view.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        view.setCheckedItem(R.id.nav_Map);
        toggle.syncState();
        onNavigationItemSelected(view.getMenu().findItem(R.id.nav_Map));

        name = view.getHeaderView(0).findViewById(R.id.nameLoginUser);
        email = view.getHeaderView(0).findViewById(R.id.nameLoginEmail);
    }

    static void setHeader(){
        if(!DataBase.ActiveSessionUser.getEmail().equals("NULL")){
            name.setText(DataBase.ActiveSessionUser.getName());
            email.setText(DataBase.ActiveSessionUser.getEmail());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static FragmentTransaction fragmentTransaction;
    @SuppressLint("StaticFieldLeak")
    public static MyCustomMapFragment fragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragment = new MyCustomMapFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.commit();
        switch (item.getItemId()) {
            case R.id.nav_Map:
                fragmentTransaction.replace(R.id.fragment, fragment);
                isViewMap = true;
                break;
            case R.id.nav_Login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new LoginFragment()).commit();
                isViewMap = false;
                break;
            case R.id.nav_List:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SearchFragment()).commit();
                isViewMap = false;
                break;
            case R.id.add:
                addMarket();
                break;
            case R.id.nav_exit:
                DataBase.auth.signOut();
                DataBase.users.clear();
                MyCustomMapFragment.clear();
                finish();
                break;
        }
        drawer.closeDrawers();
        return true;
    }

    @SuppressLint("SetTextI18n")
    public void addMarket() {
        if (DataBase.ActiveSessionUser.getEmail().equals("NULL")) return;
        if (DataBase.ActiveSessionUser.getMarkets().size() == DataBase.ActiveSessionUser.getSizeMaxMarkets()) {
            Snackbar.make(this, view, "Reached maximum limit markets \nPay for more :) ", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!isViewMap) return;
        if (Objects.requireNonNull(DataBase.auth.getCurrentUser()).isAnonymous()) return;
        double latitude = MyCustomMapFragment.getLatitude();
        double longitude = MyCustomMapFragment.getLongitude();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Adding new Market");
        builder.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.add_market, null, false);

        EditText openHours = registerWindow.findViewById(R.id.openHours);
        EditText openMinutes = registerWindow.findViewById(R.id.openMinutes);
        EditText closingHours = registerWindow.findViewById(R.id.closingHours);
        EditText closingMinutes = registerWindow.findViewById(R.id.closingMinutes);
        MaterialEditText namemarket = registerWindow.findViewById(R.id.nameInput);
        Spinner spinner = registerWindow.findViewById(R.id.spinner);
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("Small", "Medium", "Large", "BIG"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strings);
        final int[] s = {0};
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s[0] = position;
                System.out.println(s[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        openHours.setText("00");
        openMinutes.setText("00");
        closingHours.setText("23");
        closingMinutes.setText("59");
        builder.create();
        {
            openHours.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (Integer.parseInt(openHours.getText().toString()) > 23) {
                            openHours.setText(String.valueOf(23));
                        }
                    } catch (NumberFormatException e) {
                        openHours.setText(String.valueOf(0));
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            closingHours.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (Integer.parseInt(closingHours.getText().toString()) > 23) {
                            closingHours.setText(String.valueOf(23));
                        }
                    } catch (NumberFormatException e) {
                        closingHours.setText(String.valueOf(0));
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            openMinutes.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (Integer.parseInt(openMinutes.getText().toString()) > 59) {
                            openMinutes.setText(String.valueOf(59));
                        }
                    } catch (NumberFormatException e) {
                        openMinutes.setText(String.valueOf(0));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            closingMinutes.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (Integer.parseInt(closingMinutes.getText().toString()) > 59) {
                            closingMinutes.setText(String.valueOf(59));
                        }
                    } catch (NumberFormatException e) {
                        closingMinutes.setText(String.valueOf(0));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        builder.setView(registerWindow);
        builder.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());
        builder.setPositiveButton("Add", (dialog, which) -> {
            Market market = new Market(Objects.requireNonNull(namemarket.getText()).toString(), latitude, longitude);
            market.setOwner(DataBase.ActiveSessionUser.getName());
            market.setEmail(DataBase.ActiveSessionUser.getEmail());
            market.setOpenTime(openHours.getText().toString() + ":" + openMinutes.getText().toString() + " — " +
                    closingHours.getText().toString() + ":" + closingMinutes.getText().toString());
            Market.SizeMarket sizeMarket;
            switch (s[0]){
                case 1:
                    sizeMarket = Market.SizeMarket.MEDIUM;
                    break;
                case 2:
                    sizeMarket = Market.SizeMarket.LARGE;
                    break;
                case 3:
                    sizeMarket = Market.SizeMarket.BIG;
                    break;
                default:
                    sizeMarket = Market.SizeMarket.SMALL;
                    break;
            }
            market.setSizeMarket(sizeMarket);
            if (namemarket.getText().toString().length() > 2) {
                // adding market with email user
                String Uid = FirebaseAuth.getInstance().getUid();
                if (Uid == null) {
                    return;
                }
                Map<String, Object> data = new HashMap<>();
                DataBase.ActiveSessionUser.getMarkets().add(market);
                DataBase.reference.child(Uid).removeValue();
                data.put(Uid, DataBase.ActiveSessionUser);
                DataBase.reference.updateChildren(data).addOnSuccessListener(unused -> {
                    {
                        switch (market.getSizeMarket()){
                            case SMALL: {
                                MyCustomMapFragment.googleMap.addCircle(
                                        new CircleOptions().center(
                                                new LatLng(
                                                        market.getLatitude(),
                                                        market.getLongitude())).
                                                radius(10.0).
                                                fillColor(Color.GREEN).
                                                strokeColor(Color.RED).
                                                strokeWidth(4).
                                                clickable(true));
                            }
                            break;
                            case MEDIUM: {
                                MyCustomMapFragment.googleMap.addCircle(
                                        new CircleOptions().center(
                                                new LatLng(
                                                        market.getLatitude(),
                                                        market.getLongitude())).
                                                radius(10.0).
                                                fillColor(Color.GREEN).
                                                strokeColor(Color.RED).
                                                strokeWidth(4).
                                                clickable(true));
                            }
                            break;
                            case LARGE: {
                                MyCustomMapFragment.googleMap.addCircle(
                                        new CircleOptions().center(
                                                new LatLng(
                                                        market.getLatitude(),
                                                        market.getLongitude())).
                                                radius(10.0).
                                                fillColor(Color.GREEN).
                                                strokeColor(Color.RED).
                                                strokeWidth(4).
                                                clickable(true));
                            }
                            break;
                            case BIG: {
                                MyCustomMapFragment.googleMap.addCircle(
                                        new CircleOptions().center(
                                                new LatLng(
                                                        market.getLatitude(),
                                                        market.getLongitude())).
                                                radius(10.0).
                                                fillColor(Color.GREEN).
                                                strokeColor(Color.RED).
                                                strokeWidth(4).
                                                clickable(true));
                            }
                            break;
                        }
                    } // adding circle on map
                });
            }
            dialog.dismiss();
        });
        builder.show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}