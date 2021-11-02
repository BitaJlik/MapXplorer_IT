package com.example.mapxplorer;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private int pos;

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
        });
        listView.setAdapter(adapter);

    }
    public void edit(View view){
        System.out.println("+++" + input.getText().toString());
       
            if(radioProduct.isChecked()){
                market.getProducts().get(pos).setNameProduct(input.getText().toString());
            }
            else if(radioPrice.isChecked()) {
                market.getProducts().get(pos).setPrice(Double.parseDouble(input.getText().toString()));
            }
            else if(radioAmount.isChecked()) {
                market.getProducts().get(pos).setAmount(Integer.parseInt(input.getText().toString()));
            }
            finish();
    }
}