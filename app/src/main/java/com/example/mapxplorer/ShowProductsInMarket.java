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
    private Button edit;
    private int pos = 0;
    ProductAdapter.OnClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_markets);
        TextView marketName = findViewById(R.id.nameMarket);
        edit = findViewById(R.id.edit);

        if(DataBase.user.getName() == null && DataBase.user.getPassword() == null){
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
        for(int i =0;i < DataBase.online_markets.size();i++){
            if(DataBase.online_markets.get(i).getID().equals(DataBase.id)){
                pos = i;
            }
        }
        marketName.setText(DataBase.online_markets.get(pos).getNameMarket());
        init();
        refresh();
    }

    public void edit(View view) {
        int i =0;
        for(Market market : DataBase.user.getMarkets()){
            if(market.getID().equals(DataBase.user.getMarkets().get(i).getID())){
                Intent intent = new Intent(this,Edit.class);
                startActivity(intent);
                finish();
                i = 0;
            }
            else edit.setAlpha(0);
            i++;
        }

    }
    private void init(){
        arrayList.clear();
        arrayList.addAll(DataBase.online_markets.get(pos).getProducts());
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

                DataBase.online_markets.get(pos).getProducts().sort(Product::compareTo);
                sort.setText(R.string.sort_by_price);
                break;
            case 2:
                DataBase.online_markets.get(pos).getProducts().sort(Product::compareAmount);
                sort.setText(R.string.sort_by_amount);
                break;
            case 3:
                typeSort = 0;
                DataBase.online_markets.get(pos).getProducts().sort(Product::compareName);
                sort.setText(R.string.sort_by_name);
                break;
        }
        refresh();
    }
}