package com.example.mapxplorer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapxplorer.Market.Category;
import com.example.mapxplorer.Market.CategoryAdapter;
import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.Market.Product;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class CategoryList extends AppCompatActivity {
    private CategoryAdapter.OnClickListener listener;
    private RecyclerView recyclerView;
    private Button edit;
    private ArrayList<Category> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        TextView marketName = findViewById(R.id.nameMarket);
        Button edit = findViewById(R.id.edit); // checking for we have registered user
        edit.setOnClickListener(v-> edit());
            if(DataBase.ActiveSessionUser.getName().equals(DataBase.ActiveShowingMarket.getOwner())){
            edit.setAlpha(1);
            edit.setClickable(true);
        }
        else {
            edit.setAlpha(0);
            edit.setClickable(false);
        }
        categories = new ArrayList<>();
        listener = (category, position) -> {
            DataBase.category = category;
            startActivity(new Intent(this,ShowProductsInMarket.class));
        };
        recyclerView =  findViewById(R.id.rList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this    );
        RecyclerView.SimpleOnItemTouchListener simple = new RecyclerView.SimpleOnItemTouchListener();
        recyclerView.addOnItemTouchListener(simple);
        recyclerView.setLayoutManager(layoutManager);
        marketName.setText(DataBase.ActiveShowingMarket.getNameMarket());

        init();
        refresh();
    }
    private void init(){
        categories.clear();
        categories.addAll(DataBase.ActiveShowingMarket.getCategories());
    }
    private void refresh(){
        init();
        CategoryAdapter adapter = new CategoryAdapter(categories, listener);
        recyclerView.setAdapter(adapter);
    }


    public void edit() {
        finish();
        startActivity(new Intent(this,EditCategory.class));
    }
}