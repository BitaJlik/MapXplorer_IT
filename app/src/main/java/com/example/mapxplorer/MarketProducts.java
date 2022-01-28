package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapxplorer.Adatpers.MarketProductsAdapter;
import com.example.mapxplorer.Adatpers.ProductAdapter;
import com.example.mapxplorer.Introduction.OnceDialogIntroduction;
import com.example.mapxplorer.Market.Edit;
import com.example.mapxplorer.Market.Product;
import com.example.mapxplorer.databinding.ActivityMapsBinding;
import com.example.mapxplorer.placeholder.PlaceholderContent;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.jar.Attributes;
import java.util.zip.Inflater;

/**
 * A fragment representing a list of Items.
 */

public class MarketProducts extends AppCompatActivity {
    RecyclerView recyclerView;
    MaterialTextView category;
    MaterialButton sort;
    MaterialButton edit;
    MarketProductsAdapter adapter = new MarketProductsAdapter(DataBase.category.getProducts());
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_market_products_list);
        recyclerView = findViewById(R.id.list);
        category = findViewById(R.id.categoryName);
        sort = findViewById(R.id.sort);
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(v -> edit());
        sort.setOnClickListener(v -> sort());
        recyclerView.setAdapter(adapter);
        category.setText(DataBase.category.getNameCategory());
        if(!DataBase.ActiveSessionUser.getEmail().equals("NULL")){
            if(DataBase.ActiveSessionUser.getEmail().equals(DataBase.ActiveShowingMarket.getEmail())){
                edit.setVisibility(View.VISIBLE);
            }
        }
        else {
            edit.setVisibility(View.GONE);
        }

    }
    private void edit(){
        startActivity(new Intent(this, Edit.class));
    }
    int typeSort = 0;
    @SuppressLint("NotifyDataSetChanged") @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort() {
        typeSort++;
        switch (typeSort){
            case 1:
                DataBase.category.getProducts().sort(Product::compareTo);
                sort.setText(R.string.sort_by_price);
                break;
            case 2:
                DataBase.category.getProducts().sort(Product::compareAmount);
                sort.setText(R.string.sort_by_amount);
                break;
            case 3:
                typeSort = 0;
                DataBase.category.getProducts().sort(Product::compareName);
                sort.setText(R.string.sort_by_name);
                break;
        }
        adapter.notifyDataSetChanged();
    }
}