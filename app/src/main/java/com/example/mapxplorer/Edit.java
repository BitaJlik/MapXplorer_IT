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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Edit extends AppCompatActivity {
    private Market market ;
    private EditText input;
    RadioGroup radioGroup;
    RadioButton radioProduct;
    RadioButton radioPrice;
    RadioButton radioAmount;
    RadioButton radioMarket;

    private int posM = 0;
    private int posItem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        for(int i =0;i < DataBase.online_markets.size();i++){
            if(DataBase.online_markets.get(i).getID().equals(DataBase.id)){
                posM = i;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        market = DataBase.online_markets.get(posM);
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
            posItem = position;
            input.setText(market.getProducts().get(posItem).getNameProduct());
            radioGroup.clearCheck();
            radioProduct.setChecked(true);
        });
        listView.setAdapter(adapter);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(radioMarket.isChecked()){
                input.setText(market.getNameMarket());
            }
            else input.setText("");
            if (!market.getProducts().isEmpty()) {
                if(radioProduct.isChecked()){
                    input.setText(market.getProducts().get(posItem).getNameProduct());
                }
                else if(radioPrice.isChecked()) {
                    input.setText(String.valueOf(market.getProducts().get(posItem).getPrice()));
                }
                else if(radioAmount.isChecked()) {
                    input.setText(String.valueOf(market.getProducts().get(posItem).getAmount()));
                }
            }

        });

    }
    public void edit(View view){
            if(market.getProducts().isEmpty()){
                return;
            }
            if(radioProduct.isChecked()){
                market.getProducts().get(posItem).setNameProduct(input.getText().toString());
            }
            else if(radioPrice.isChecked()) {
                market.getProducts().get(posItem).setPrice(Double.parseDouble(input.getText().toString()));
            }
            else if(radioAmount.isChecked()) {
                market.getProducts().get(posItem).setAmount(Integer.parseInt(input.getText().toString()));
            }
            else if(radioMarket.isChecked()){
                market.setNameMarket(input.getText().toString());
            }
            finish();
        Intent intent = new Intent(this,ShowProductsInMarket.class);
        startActivity(intent);
    }
    public void add(View view){
        if(input.getText().toString().equals("")){
            return;
        }
        if(radioProduct.isChecked()){

            DataBase.reference.child(
                    Objects.requireNonNull(
                            DataBase.auth.getCurrentUser())
                            .getUid())
                    .child("markets")
                    .child(String.valueOf(posM))
                    .child("products")
            .setValue(new Product(input.getText().toString(),0,0));

            Map<String ,Object> data = new HashMap<>();
            data.put(String.valueOf(market.getProducts().size()),new Product(input.getText().toString(),0,0));
            DataBase.reference.updateChildren(data);
            market.getProducts().add(new Product(input.getText().toString(),0,0));
        }
        else if(radioPrice.isChecked()) {
            market.getProducts().add(new Product("",Double.parseDouble(input.getText().toString()),0));
        }
        else if(radioAmount.isChecked()) {
            market.getProducts().add(new Product("",0,Integer.parseInt(input.getText().toString())));
        }
        finish();
        Intent intent = new Intent(this,ShowProductsInMarket.class);
        startActivity(intent);
    }
    public void delete(View view){
        if(market.getProducts().isEmpty()){
            return;
        }
        Intent intent = new Intent(this,ShowProductsInMarket.class);
        if(radioMarket.isChecked()){
            DataBase.online_markets.remove(posM);
            intent = new Intent(this,MapsActivity.class);
        }
        else {
            market.getProducts().remove(posItem);
        }
        finish();
        startActivity(intent);
    }
}