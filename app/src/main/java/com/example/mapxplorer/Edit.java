package com.example.mapxplorer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Edit extends AppCompatActivity {
    private Market market ;
    private EditText input;
    RadioGroup radioGroup;
    RadioButton radioProduct;
    RadioButton radioPrice;
    RadioButton radioAmount;
    RadioButton radioMarket;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        market = DataBase.markets.get(DataBase.id);
        ListView listView = findViewById(R.id.listEdit);
        radioGroup = findViewById(R.id.radioGroup);

        radioProduct = findViewById(R.id.radioProduct);
        radioPrice = findViewById(R.id.radioPrice);
        radioAmount = findViewById(R.id.radioAmount);
        radioMarket = findViewById(R.id.radioMarket);

        input = findViewById(R.id.editInput);

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        for(Product product : market.getProducts()){
            HashMap<String, String> map = new HashMap<>();
            map.put("Product",product.getNameProduct() );
            map.put("Price",  product.getAmount()+"шт     " +product.getPrice() +" Грн");
            arrayList.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Product", "Price"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("+++" + arrayList.get(position));
            pos = position;
            input.setText(market.getProducts().get(pos).getNameProduct());
            radioGroup.clearCheck();
            radioProduct.setChecked(true);
        });
        listView.setAdapter(adapter);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(radioProduct.isChecked()){
                input.setText(market.getProducts().get(pos).getNameProduct());
            }
            else if(radioPrice.isChecked()) {
                input.setText(String.valueOf(market.getProducts().get(pos).getPrice()));
            }
            else if(radioAmount.isChecked()) {
                input.setText(String.valueOf(market.getProducts().get(pos).getAmount()));
            }
            else if(radioMarket.isChecked()){
                input.setText(market.getNameMarket());
            }
        });

    }
    public void edit(View view){
            if(radioProduct.isChecked()){
                market.getProducts().get(pos).setNameProduct(input.getText().toString());
            }
            else if(radioPrice.isChecked()) {
                market.getProducts().get(pos).setPrice(Double.parseDouble(input.getText().toString()));
            }
            else if(radioAmount.isChecked()) {
                market.getProducts().get(pos).setAmount(Integer.parseInt(input.getText().toString()));
            }
            else if(radioMarket.isChecked()){
                market.setNameMarket(input.getText().toString());
            }
            finish();
        Intent intent = new Intent(this,MarketShowProducts.class);
        startActivity(intent);
    }
    public void add(View view){
        if(radioProduct.isChecked()){
            market.getProducts().add(new Product(input.getText().toString(),0,0));
        }
        else if(radioPrice.isChecked()) {
            market.getProducts().add(new Product("",Double.parseDouble(input.getText().toString()),0));
        }
        else if(radioAmount.isChecked()) {
            market.getProducts().add(new Product("",0,Integer.parseInt(input.getText().toString())));
        }
        finish();
        Intent intent = new Intent(this,MarketShowProducts.class);
        startActivity(intent);
    }
    public void delete(View view){
        Intent intent = new Intent(this,MarketShowProducts.class);
        if(radioMarket.isChecked()){
            DataBase.markets.remove(DataBase.id);
            intent = new Intent(this,MapsActivity.class);
        }
        else {
            market.getProducts().remove(pos);
        }
        finish();
        startActivity(intent);
    }
}