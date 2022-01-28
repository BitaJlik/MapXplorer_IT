package com.example.mapxplorer.Market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.Market.Category;
import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.Market.Product;
import com.example.mapxplorer.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EditCategory extends AppCompatActivity {
    private EditText input;
    private int posItem = 0;
    private ArrayList<Category> categories;
    private Category category;
    SimpleAdapter adapter;
    ListView listView;
    ArrayList<HashMap<String, String>> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        listView = findViewById(R.id.listEdit);

        arrayList = new ArrayList<>();
        for(Category category : DataBase.ActiveShowingMarket.getCategories()){
            HashMap<String, String> map = new HashMap<>();
            map.put("Category",category.getNameCategory() );
            arrayList.add(map);
        }
        adapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Category"},
                new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            posItem = position;

            category = DataBase.ActiveShowingMarket.getCategories().get(posItem);
            input.setText(category.getNameCategory());

        });

        input = findViewById(R.id.editInput);
        MaterialButton add = findViewById(R.id.add);
        add.setOnClickListener(v -> add());
        MaterialButton edit = findViewById(R.id.buttonEdit);
        edit.setOnClickListener(v -> edit());
        MaterialButton delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> delete());
    }

    private void add() {

        if(input.getText().toString().isEmpty()) return;
        Category category = new Category();
        category.setNameCategory(input.getText().toString());
        category.setProducts(new ArrayList<>());
        DataBase.ActiveShowingMarket.getCategories().add(category);
        refreshDB();


    }
    private void edit() {
        if(category == null) return;
        category.setNameCategory(input.getText().toString());
        refreshDB();
    }

    private void delete() {

        if(category == null) return;
        categories.remove(category);
        refreshDB();
    }

    private void refreshDB(){
        String Uid = FirebaseAuth.getInstance().getUid();
        if (Uid == null) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put(Uid, DataBase.ActiveSessionUser);
        DataBase.reference.child(Uid).removeValue();
        DataBase.reference.updateChildren(data);
        arrayList.clear();
        for(Category category : DataBase.ActiveShowingMarket.getCategories()){
            HashMap<String, String> map = new HashMap<>();
            map.put("Category",category.getNameCategory() );
            arrayList.add(map);
        }
        adapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Category"},
                new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.invalidateViews();

    }


}