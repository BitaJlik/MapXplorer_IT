package com.example.mapxplorer;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class MarketShowProducts extends AppCompatActivity {
    private TextView text ;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_show_products);

        text = findViewById(R.id.texted);

        Market market = DataBase.markets.get(DataBase.id);
        if(!market.getProducts().isEmpty())
            text.setText(market.getNameMarket());
        for(Product product : market.getProducts()){
            text.append("\n" + product.getNameProduct());
        }

    }
}