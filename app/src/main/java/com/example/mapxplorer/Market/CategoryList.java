package com.example.mapxplorer.Market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Adatpers.MarketProductsAdapter;
import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.Adatpers.CategoryAdapter;
import com.example.mapxplorer.MarketProducts;
import com.example.mapxplorer.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CategoryList extends AppCompatActivity {
    private CategoryAdapter.OnClickListener listener;
    private RecyclerView recyclerView;
    private ArrayList<Category> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        TextView marketName = findViewById(R.id.nameMarket);
        MaterialButton edit = findViewById(R.id.edit); // checking for we have registered user
        edit.setOnClickListener(v-> edit());

        if(DataBase.ActiveShowingMarket.getEmail().equals(DataBase.ActiveSessionUser.getEmail())){
            edit.setVisibility(View.VISIBLE);
        }
        else {
            edit.setVisibility(View.GONE);
            marketName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        init();

        listener = (category, position) -> {
            DataBase.category = category;
            DataBase.posCategory = position;

            startActivity(new Intent(this, MarketProducts.class));
        };
        recyclerView =  findViewById(R.id.rList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.SimpleOnItemTouchListener simple = new RecyclerView.SimpleOnItemTouchListener();
        recyclerView.addOnItemTouchListener(simple);
        recyclerView.setLayoutManager(layoutManager);
        marketName.setText(DataBase.ActiveShowingMarket.getNameMarket());

        refresh();
    }
    private void init(){
        categories = DataBase.ActiveShowingMarket.getCategories();
    }
    private void refresh(){
        init();
        CategoryAdapter adapter = new CategoryAdapter(categories, listener);
        recyclerView.setAdapter(adapter);
    }


    public void edit() {
        finish();
        startActivity(new Intent(this, EditCategory.class));
    }
}