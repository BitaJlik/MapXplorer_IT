package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MarketShowProducts extends AppCompatActivity  {
    private TextView text ;
    private ListView listView;
    private SimpleAdapter adapter;
    private Button edit;
    private  Market market;
   @SuppressLint("SetTextI18n")
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_show_products);

        text = findViewById(R.id.texted);
        listView = findViewById(R.id.list);
        edit = findViewById(R.id.button);

        market = DataBase.markets.get(DataBase.id);
        if(!market.getProducts().isEmpty())
            text.setText(market.getNameMarket());
        else text.setText(market.getNameMarket() + "   -> Пусто");
       ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
       HashMap<String, String> map;
        for(Product product : market.getProducts()){
            map = new HashMap<>();
            map.put("Product",product.getNameProduct() );
            map.put("Price",  product.getAmount()+"шт     " +product.getPrice() +" Грн");
            arrayList.add(map);
        }

        adapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
               new String[]{"Product", "Price"},
               new int[]{android.R.id.text1, android.R.id.text2});
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("+++" + arrayList.get(position));
            }
        });
        listView.setAdapter(adapter);

    }

    public void edit(View view) {
       if(!market.getProducts().isEmpty()) {
           market.getProducts().get(0).setAmount(market.getProducts().get(0).getAmount() + 1);
       }
       finish();
    }
}
