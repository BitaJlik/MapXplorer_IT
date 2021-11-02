package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;


public class MarketShowProducts extends AppCompatActivity  {
    private TextView text ;
    private ListView listView;
    private ArrayAdapter<CharSequence> adapter;
   @SuppressLint("SetTextI18n")
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_show_products);

        text = findViewById(R.id.texted);
        listView = findViewById(R.id.list);
        Market market = DataBase.markets.get(DataBase.id);
        if(!market.getProducts().isEmpty())
            text.setText(market.getNameMarket());
        else text.setText(market.getNameMarket() + "   -> Пусто");
       ArrayList<String> arrayList = new ArrayList<>();
        for(Product product : market.getProducts()){
            arrayList.add(product.getNameProduct() + "   "+product.getAmount()+"шт     " +product.getPrice() +" Грн");
        }
        String[] strings = new String[arrayList.size()];
        for(int i = 0;i< arrayList.size();i++){
            strings[i] = arrayList.get(i);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("+++" + arrayList.get(position));
                print(arrayList,position);
            }
        });
        listView.setAdapter(adapter);

    }
    private void print(ArrayList<String> a, int pos){
       Toast.makeText(this,a.get(pos),Toast.LENGTH_SHORT).show();
    }
}