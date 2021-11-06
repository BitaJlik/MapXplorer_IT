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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edit extends AppCompatActivity {
    private Market market ;
    private EditText input;
    RadioGroup radioGroup;
    RadioButton radioProduct;
    RadioButton radioPrice;
    RadioButton radioAmount;
    RadioButton radioMarket;

    private int posItem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ListView listView = findViewById(R.id.listEdit);
        radioGroup = findViewById(R.id.radioGroup);
        radioProduct = findViewById(R.id.radioProduct);
        radioPrice = findViewById(R.id.radioPrice);
        radioAmount = findViewById(R.id.radioAmount);
        radioMarket = findViewById(R.id.radioMarket);
        input = findViewById(R.id.editInput);
        market = DataBase.ActiveShowingMarket;

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

        String Uid = FirebaseAuth.getInstance().getUid();
        if(Uid == null){
            return;
        }
        Map<String,Object> data = new HashMap<>();
        ArrayList<Market> markets = new ArrayList<>(DataBase.ActiveSessionUser.getMarkets());
        data.put("markets",markets);
        DataBase.reference.child(Uid).child("markets").removeValue();
        DataBase.reference.child(Uid).updateChildren(data);
            finish();
        Intent intent = new Intent(this,ShowProductsInMarket.class);
        startActivity(intent);
    }
    public void add(View view){
        if(input.getText().toString().equals("")){
            return;
        }
        {
            String Uid = FirebaseAuth.getInstance().getUid();
            if(Uid == null){
                return;
            }
            Map<String,Object> data = new HashMap<>();
            Market market = DataBase.ActiveShowingMarket;
            market.getProducts().add(new Product(input.getText().toString(),0,0));
            ArrayList<Market> markets = new ArrayList<>(DataBase.ActiveSessionUser.getMarkets());
            data.put("markets",markets);
            DataBase.reference.child(Uid).child("markets").removeValue();
            DataBase.reference.child(Uid).updateChildren(data);
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
            intent = new Intent(this,MapsActivity.class);
        }
        else {
            market.getProducts().remove(posItem);
        }
        String Uid = FirebaseAuth.getInstance().getUid();
        if(Uid == null){
            return;
        }
        Map<String,Object> data = new HashMap<>();
        ArrayList<Market> markets = new ArrayList<>(DataBase.ActiveSessionUser.getMarkets());
        data.put("markets",markets);
        DataBase.reference.child(Uid).child("markets").removeValue();
        DataBase.reference.child(Uid).updateChildren(data);
        finish();
        startActivity(intent);
    }
}