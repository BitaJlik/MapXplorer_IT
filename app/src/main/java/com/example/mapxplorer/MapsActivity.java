package com.example.mapxplorer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.User.User;
import com.example.mapxplorer.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean isViewMap = true;
    private DrawerLayout drawer;
    public static FragmentTransaction fragmentTransaction;
    @SuppressLint("StaticFieldLeak")
    public static MyCustomMapFragment fragment;
    public static NavigationView view;
    ActionBarDrawerToggle toggle;
    int sizeUsers = 0;
    int sizeMarkets = 0;
    static Geocoder geocoder;
    @SuppressLint("StaticFieldLeak")
    public static TextView name;
    @SuppressLint("StaticFieldLeak")
    public static TextView email;
    @SuppressLint("StaticFieldLeak")
    public static SearchView search;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static boolean isSatellite = false;
    private static List<Address>[] addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


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

        if (!isOnline(this)) {
            Snackbar.make(binding.getRoot(), getStr(R.string.no_internet), Snackbar.LENGTH_LONG).show();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        search = toolbar.findViewById(R.id.search);
        setSupportActionBar(toolbar);

        addresses = new List[1];
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
        SwitchCompat switchCompat = view.getHeaderView(0).findViewById(R.id.satellite);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSatellite = isChecked;
            if (switchCompat.isChecked()) {
                MyCustomMapFragment.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else MyCustomMapFragment.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        });
        geocoder = new Geocoder(this);
        setListener();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    static void setListener() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isViewMap) {
                    try {
                        addresses[0] = geocoder.getFromLocationName(query, 1);
                    } catch (IOException e) {
                        Log.i("GEO", "Not found");
                    }
                    if (addresses[0].size() > 0) {
                        Address address = addresses[0].get(0);
                        System.out.println("+++");
                        MyCustomMapFragment.googleMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 16.0f));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (search.hasFocus() && !newText.isEmpty()) DataBase.search = newText;
                return false;
            }
        });
    }

    static void setHeader() {
        if (!DataBase.ActiveSessionUser.getEmail().equals("NULL")) {
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
            case R.id.nav_Product:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ProductList()).commit();
                isViewMap = false;
                break;
            case R.id.add:
                addMarket();
                break;
            case R.id.findMe:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    askLocationPermission();
                }
                break;
            case R.id.nav_exit:
                DataBase.auth.signOut();
                DataBase.users.clear();
                MyCustomMapFragment.clear();
                finish();
                break;
        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            search.onActionViewCollapsed();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        drawer.closeDrawers();
        return true;
    }
    @SuppressLint("SetTextI18n")
    public void addMarket() {

        if (DataBase.ActiveSessionUser.getEmail().equals("NULL") ){
            Snackbar.make(this, view,getStr(R.string.please_login) , Snackbar.LENGTH_LONG).show();
            return;
        }
        if(!isViewMap ){
            Snackbar.make(this, view, getStr(R.string.switch_map), Snackbar.LENGTH_LONG).show();
            return;
        }
        if (DataBase.ActiveSessionUser.getMarkets().size() == DataBase.ActiveSessionUser.getSizeMaxMarkets()) {
            Snackbar.make(this, view, getStr(R.string.reached_max), Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!isViewMap) return;
        try {
            if (Objects.requireNonNull(DataBase.auth.getCurrentUser()).isAnonymous()) return;

        }catch (NullPointerException e){
            return;
        }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                s[0] = 0;
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
                                MyCustomMapFragment.googleMap.addGroundOverlay(new GroundOverlayOptions().position(
                                        new LatLng(market.getLatitude(),market.getLongitude()),120,120)
                                        .image(BitmapDescriptorFactory.fromResource(R.drawable.small)).clickable(true));
                            }
                            break;
                            case MEDIUM: {
                                MyCustomMapFragment.googleMap.addGroundOverlay(new GroundOverlayOptions().position(
                                        new LatLng(market.getLatitude(),market.getLongitude()),120,120)
                                        .image(BitmapDescriptorFactory.fromResource(R.drawable.medium)).clickable(true));
                            }
                            break;
                            case LARGE: {
                                MyCustomMapFragment.googleMap.addGroundOverlay(new GroundOverlayOptions().position(
                                        new LatLng(market.getLatitude(),market.getLongitude()),120,120)
                                        .image(BitmapDescriptorFactory.fromResource(R.drawable.large)).clickable(true));
                            }
                            break;
                            case BIG: {
                                MyCustomMapFragment.googleMap.addGroundOverlay(new GroundOverlayOptions().position(
                                        new LatLng(market.getLatitude(),market.getLongitude()),120,120)
                                        .image(BitmapDescriptorFactory.fromResource(R.drawable.big)).clickable(true));
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

    private static final String TAG = "Map";
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(location -> {
            if (location != null) {
                //We have a location
                Log.d(TAG, "onSuccess: " + location.toString());
                Log.d(TAG, "onSuccess: " + location.getLatitude());
                Log.d(TAG, "onSuccess: " + location.getLongitude());
                LatLng l = new LatLng(location.getLatitude(),location.getLongitude() );
                MyCustomMapFragment.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,16.0f));
            } else  {
                Log.d(TAG, "onSuccess: Location was null...");
                Snackbar.make(this, view,getStr(R.string.gps_error) , Snackbar.LENGTH_LONG).show();
            }
        });

        locationTask.addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getLocalizedMessage() ));
    }
    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 10001);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 10001);
            }
        }
    }
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getLastLocation();
            } else {
                //Permission not granted
            }
        }
    }
    public String getStr(int Id){
        return getApplicationContext().getString(Id);
    }
}