package com.example.mapxplorer;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapxplorer.Adatpers.ProductAllListAdapter;
import com.example.mapxplorer.Market.Category;
import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.Market.Product;

import java.util.ArrayList;

public class ProductList extends Fragment {
    ProductAllListAdapter adapter;
    ProductAllListAdapter.OnClickListener listener;
    ArrayList<Product> products;
    RecyclerView recyclerView;
    SearchView searchView;
    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Creating", "ProductList");
        View view = inflater.inflate(R.layout.activity_product_list, container, false);
        searchView = MapsActivity.search;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        recyclerView = view.findViewById(R.id.recyclerProductList);
        listener = (product, position) -> {
            ss:
            {
                for (Market market : DataBase.getAllMarkets()) {
                    for (Category category : market.getCategories()) {
                        for (Product p : category.getProducts()) {
                            Log.d("TEST", p.toString());
                            Log.d("TEST",product.toString());
                            if (p == product) {
                                DataBase.ActiveShowingMarket = market;
                                break ss;
                            }
                        }
                    }
                }
            }
            startActivity(new Intent(getContext(),CategoryList.class));
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.SimpleOnItemTouchListener simple = new RecyclerView.SimpleOnItemTouchListener();

        recyclerView.addOnItemTouchListener(simple);
        recyclerView.setLayoutManager(layoutManager);
        initialize();
        return view;
    }

    public void initialize(){
        products = new ArrayList<>();
        for(Market market : DataBase.getAllMarkets()){
            for(Category category : market.getCategories()){
                products.addAll(category.getProducts());
            }
        }
        adapter = new ProductAllListAdapter(products, listener);
        recyclerView.setAdapter(adapter);

    }
}