package com.example.mapxplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class Search extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        arrayList = new ArrayList<>();
        search = findViewById(R.id.searchText);
        init();
        ListView listView = findViewById(R.id.list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setOnItemClickListener((parent, view, position, id) -> System.out.println("+++" + arrayList.get(position)));
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(search.getText().toString());
                adapter.getFilter().filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) { }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        });
        Intent intent = new Intent(this, ShowProductsInMarket.class);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            startActivity(intent);
        });
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
    }
    public void init(){
        arrayList.clear();
    }
}