package com.example.mapxplorer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowProductsInMarket extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Product> arrayList;
    private Button sort;
    ProductAdapter.OnClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_markets);
        TextView marketName = findViewById(R.id.nameMarket);

        Button edit = findViewById(R.id.edit); // checking for we have registered user
        if(DataBase.ActiveSessionUser.getEmail().equals("NULL")){
            edit.setAlpha(0);
            edit.setClickable(false);
        }
        else {
            edit.setAlpha(1);
            edit.setClickable(true);
        }

        arrayList = new ArrayList<>();
        listener = (product, position) ->
                Toast.makeText(getApplicationContext(), "Вибрано " + product.getNameProduct(),
                Toast.LENGTH_SHORT).show();

        sort = findViewById(R.id.sort);
        recyclerView = findViewById(R.id.rList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this    );
        RecyclerView.SimpleOnItemTouchListener simple = new RecyclerView.SimpleOnItemTouchListener();

        recyclerView.addOnItemTouchListener(simple);
        recyclerView.setLayoutManager(layoutManager);

        marketName.setText(DataBase.ActiveShowingMarket.getNameMarket());
        init();
        refresh();

        if(!DataBase.ActiveSessionUser.getName().equals("NULL")){
            for(Market market : DataBase.ActiveSessionUser.getMarkets()){
                if(DataBase.ActiveShowingMarket.getID().equals(market.getID())){
                    edit.setAlpha(1);
                    edit.setClickable(true);
                    break;
                }
                else {
                    edit.setAlpha(0);
                    edit.setClickable(false);
                }
            }
        }
        else {
            edit.setAlpha(0);
            edit.setClickable(false);
        }
    }

    public void edit(View view) {
        finish();
        Intent intent = new Intent(this,Edit.class);
        startActivity(intent);
    }
    private void init(){
        arrayList.clear();
        arrayList.addAll(DataBase.ActiveShowingMarket.getProducts());
    }
    private void refresh(){
        init();
        ProductAdapter adapter = new ProductAdapter(arrayList, listener);
        recyclerView.setAdapter(adapter);
    }
    int typeSort = 0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(View view) {
        typeSort++;

        switch (typeSort){
            case 1:

                DataBase.ActiveShowingMarket.getProducts().sort(Product::compareTo);
                sort.setText(R.string.sort_by_price);
                break;
            case 2:
                DataBase.ActiveShowingMarket.getProducts().sort(Product::compareAmount);
                sort.setText(R.string.sort_by_amount);
                break;
            case 3:
                typeSort = 0;
                DataBase.ActiveShowingMarket.getProducts().sort(Product::compareName);
                sort.setText(R.string.sort_by_name);
                break;
        }
        refresh();
    }
}