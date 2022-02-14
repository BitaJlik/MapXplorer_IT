package com.example.mapxplorer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.util.Util;
import com.example.mapxplorer.Auth.Auth_Fragment;
import com.example.mapxplorer.Introduction.OnceDialogIntroduction;
import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.Market.ProductList;
import com.example.mapxplorer.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static DrawerLayout drawer;
    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;
    public static NavigationView view;
    public static Window window;
    public static boolean isSatellite = false;
    public static boolean isDarkTheme = false;
    public static boolean isViewMap = false ;
    public static Geocoder geocoder;
    private static Address address;
    private static final String TAG = "Map";
    ActionBarDrawerToggle toggle;
    FusedLocationProviderClient fusedLocationProviderClient;
    private int sizeMarket = 0;
    @SuppressLint("StaticFieldLeak") public static TextView name;
    @SuppressLint("StaticFieldLeak") public static TextView email;
    @SuppressLint("StaticFieldLeak") public static SearchView search;
    @SuppressLint("StaticFieldLeak") public static Toolbar toolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.main_color));
        super.onCreate(savedInstanceState);
        com.example.mapxplorer.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!Utils.isOnline(this)) {
            Snackbar.make(binding.getRoot(), getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show();
        }
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("++");
            }
        });
        search = toolbar.findViewById(R.id.search);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        view = findViewById(R.id.nav_view);

        view.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(@NonNull View drawerView, float slideOffset) { }
            @Override public void onDrawerOpened(@NonNull View drawerView) { }
            @Override public void onDrawerClosed(@NonNull View drawerView) { }
            @Override public void onDrawerStateChanged(int newState) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                search.onActionViewCollapsed();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
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
        fragmentManager = getSupportFragmentManager();
        DataBase.fragmentManager = fragmentManager;
        MainActivity.setHeader();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean("one", false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            startActivity(new Intent(this, OnceDialogIntroduction.class));
            editor.putBoolean("one", Boolean.TRUE);
            editor.apply();
        }
        if(Settings.isSwitch){
            fragmentManager.beginTransaction().replace(R.id.fragment,new Settings()).commit();
            Settings.isSwitch = false;
        }
    }
    @SuppressLint("NonConstantResourceId") @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawers();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.commit();
        switch (item.getItemId()) {
            case R.id.nav_Map:
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.fragment, new MyCustomMapFragment());
                isViewMap = true;
                break;
            case R.id.nav_Login:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.fragment, new Auth_Fragment()).commit();
                isViewMap = false;
                break;
            case R.id.nav_List:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.fragment, new SearchFragment()).commit();
                isViewMap = false;
                break;
            case R.id.nav_Product:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.fragment, new ProductList()).commit();
                isViewMap = false;
                break;
            case R.id.add:
                addMarket();
                break;
            case R.id.findMe:
                if (!isViewMap) {
                    Snackbar.make(view, getResources().getText(R.string.switch_map), Snackbar.LENGTH_LONG).show();
                }
                else {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        getLastLocation();
                    } else {
                        askLocationPermission();
                    }
                }
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.fragment, new Settings()).commit();
                isViewMap = false;
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

        return true;
    }

    static void setListener() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isViewMap) {
                    try {
                        address = geocoder.getFromLocationName(query, 1).get(0);
                    } catch (IOException e) {
                        Log.i("GEO", "Not found");
                    }
                    if (address != null) {
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
    public static void setHeader() {
        if (!DataBase.ActiveSessionUser.getEmail().equals("NULL")) {
            name.setText(DataBase.ActiveSessionUser.getName());
            email.setText(DataBase.ActiveSessionUser.getEmail());
        }
    }
    @Override public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressLint("SetTextI18n") public void addMarket() {
        sizeMarket = 0;
        if (DataBase.ActiveSessionUser.getEmail().equals("NULL")) {
            Snackbar.make(this, view, getResources().getString(R.string.please_login), Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show();
            return;
        }
        if (!isViewMap) {
            Snackbar.make(this, view, getResources().getString(R.string.switch_map), Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show();
            return;
        }
        if (DataBase.ActiveSessionUser.getMarkets().size() == DataBase.ActiveSessionUser.getSizeMaxMarkets()) {
            Snackbar.make(this, view, getResources().getString(R.string.reached_max), Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show();
            return;
        }
        if (!isViewMap) return;
        try {
            if (Objects.requireNonNull(DataBase.auth.getCurrentUser()).isAnonymous()) return;

        } catch (NullPointerException e) {
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
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sizeMarket = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sizeMarket = 0;
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
                public void afterTextChanged(Editable s) {}
            });
        }
        builder.setView(registerWindow);
        builder.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());
        builder.setPositiveButton("Add", (dialog, which) -> {
            Market market = new Market(Objects.requireNonNull(namemarket.getText()).toString(), latitude, longitude);
            market.setUserUid(DataBase.auth.getUid());
            market.setOwner(DataBase.ActiveSessionUser.getName());
            market.setEmail(DataBase.ActiveSessionUser.getEmail());
            market.setOpenTime(openHours.getText().toString() + ":" + openMinutes.getText().toString() + " — " +
                    closingHours.getText().toString() + ":" + closingMinutes.getText().toString());
            Market.SizeMarket ESizeMarket;
            switch (sizeMarket) {
                case 1:
                    ESizeMarket = Market.SizeMarket.MEDIUM;
                    break;
                case 2:
                    ESizeMarket = Market.SizeMarket.LARGE;
                    break;
                case 3:
                    ESizeMarket = Market.SizeMarket.BIG;
                    break;
                default:
                    ESizeMarket = Market.SizeMarket.SMALL;
                    break;
            }
            market.setSizeMarket(ESizeMarket);
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
                });
            }
            dialog.dismiss();
        });
        builder.show();
    }
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(location -> {
            // sometimes location is NULL !!!
            if(location != null){
                //We have a location
                Log.d(TAG, "onSuccess: " + location.toString());
                Log.d(TAG, "onSuccess: " + location.getLatitude());
                Log.d(TAG, "onSuccess: " + location.getLongitude());
                LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
                MyCustomMapFragment.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 16.0f));
            }
            else {
                Snackbar.make(view,getResources().getString(R.string.gps_error_wait),Snackbar.LENGTH_LONG).show();
            }
        });
        locationTask.addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getLocalizedMessage()));
    }
    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                Snackbar.make(view, getResources().getText(R.string.gps_error), Snackbar.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10001);
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
                Snackbar.make(view, getResources().getText(R.string.gps_error), Snackbar.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: Permission not granted ");
            }
        }
    }
}