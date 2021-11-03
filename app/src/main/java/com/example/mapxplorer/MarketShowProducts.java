package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class MarketShowProducts extends AppCompatActivity  {
    private Market market;
    SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> arrayList;
    private Button sort;
    private ListView listView;
    @SuppressLint("SetTextI18n")
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_show_products);

        TextView text = findViewById(R.id.texted);
        listView = findViewById(R.id.list);

        sort = findViewById(R.id.sort);
        market = DataBase.markets.get(DataBase.id);
        if(!market.getProducts().isEmpty())
            text.setText(market.getNameMarket());
        else text.setText(market.getNameMarket() + "\nПусто");
        arrayList = new ArrayList<>();
        init();
        refresh();
        listView.setOnItemClickListener((parent, view, position, id) -> System.out.println("+++" + arrayList.get(position)));



    }
    public void init(){
        arrayList.clear();
        for(Product product : market.getProducts()){
            HashMap<String, String> map = new HashMap<>();
            map.put("Product",product.getNameProduct() );
            map.put("Price",  product.getAmount()+"шт     " +product.getPrice() +" Грн");
            arrayList.add(map);
        }
    }

    public void edit(View view) {
           Intent intent = new Intent(this,Edit.class);
           startActivity(intent);

       finish();
    }
    private void refresh(){
        init();
        adapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Product", "Price"},
                new int[]{android.R.id.text1, android.R.id.text2});

        listView.setAdapter(adapter);
    }
    int typeSort = 0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(View view) {
        typeSort++;
        switch (typeSort){
            case 1:
                DataBase.markets.get(DataBase.id).getProducts().sort(Product::compareTo);
                sort.setText(R.string.sort_by_price);
                break;
            case 2:
                DataBase.markets.get(DataBase.id).getProducts().sort(Product::compareAmount);
                sort.setText(R.string.sort_by_amount);
                break;
            case 3:
                typeSort = 0;
                DataBase.markets.get(DataBase.id).getProducts().sort(Product::compareName);
                sort.setText(R.string.sort_by_name);
                break;
        }
        refresh();
        // TODO: 03.11.2021 Make refresh listview
    }
}
