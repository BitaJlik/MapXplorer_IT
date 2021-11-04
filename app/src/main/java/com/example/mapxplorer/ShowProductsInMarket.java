package com.example.mapxplorer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

public class ShowProductsInMarket extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> arrayList;
    private Button sort;
    ProductAdapter.OnClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_markets);
        arrayList = new ArrayList<>();
        listener = (product, position) ->
                Toast.makeText(getApplicationContext(), "Вибрано " + product.getNameProduct(),
                Toast.LENGTH_SHORT).show();

        sort = findViewById(R.id.sort);
        recyclerView = findViewById(R.id.rList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this    );
        RecyclerView.SimpleOnItemTouchListener simple = new RecyclerView.SimpleOnItemTouchListener();
        init();
        refresh();
        recyclerView.addOnItemTouchListener(simple);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void edit(View view) {
        Intent intent = new Intent(this,Edit.class);
        startActivity(intent);

        finish();
    }
    private void init(){
        arrayList.clear();
        arrayList.addAll(DataBase.markets.get(DataBase.id).getProducts());
    }
    private void refresh(){
        init();
        adapter = new ProductAdapter(arrayList,listener);


        recyclerView.setAdapter(adapter);
    }
    int typeSort = 0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(View view) {
        typeSort++;
        switch (typeSort){
            case 1:
                DataBase.markets.get(DataBase.id).getProducts().sort(Product::compareTo);
                sort.setText(R.string.sort_by_price);
                break;
            case 2:
                DataBase.markets.get(DataBase.id).getProducts().sort(Product::compareAmount);
                sort.setText(R.string.sort_by_amount);
                break;
            case 3:
                typeSort = 0;
                DataBase.markets.get(DataBase.id).getProducts().sort(Product::compareName);
                sort.setText(R.string.sort_by_name);
                break;
        }
        refresh();
    }
}