package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class MarketShowProducts extends AppCompatActivity  {
    private Market market;
    private ArrayList<HashMap<String, String>> arrayList;
    @SuppressLint("SetTextI18n")
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_show_products);

        TextView text = findViewById(R.id.texted);
        ListView listView = findViewById(R.id.list);

        market = DataBase.markets.get(DataBase.id);
        if(!market.getProducts().isEmpty())
            text.setText(market.getNameMarket());
        else text.setText(market.getNameMarket() + "   -> Пусто");
        arrayList = new ArrayList<>();
        init();

        SimpleAdapter adapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Product", "Price"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setOnItemClickListener((parent, view, position, id) -> System.out.println("+++" + arrayList.get(position)));
        listView.setAdapter(adapter);

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
       if(!market.getProducts().isEmpty()) {
           Intent intent = new Intent(this,Edit.class);
           startActivity(intent);
       }
       finish();
    }
}
