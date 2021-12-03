package com.example.mapxplorer;

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

import com.example.mapxplorer.Market.Category;
import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.Market.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditCategory extends AppCompatActivity {
    private EditText input;
    private int posItem = 0;
    private ArrayList<Category> categories;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        ListView listView = findViewById(R.id.listEdit);

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        categories = DataBase.ActiveShowingMarket.getCategories();
        for(Category category : DataBase.ActiveShowingMarket.getCategories()){
            HashMap<String, String> map = new HashMap<>();
            map.put("Category",category.getNameCategory() );
            arrayList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Category"},
                new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            posItem = position;

            category = categories.get(posItem);
            System.out.println("+++" + category);
            input.setText(category.getNameCategory());

        });

        input = findViewById(R.id.editInput);
        Button add = findViewById(R.id.add);
        add.setOnClickListener(v -> add());
        Button edit = findViewById(R.id.buttonEdit);
        edit.setOnClickListener(v -> edit());
        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> delete());
    }
    private void add() {
        DataBase.auth.signInWithEmailAndPassword(DataBase.ActiveSessionUser.getEmail(),
                DataBase.ActiveSessionUser.getPassword())
                .addOnSuccessListener(authResult -> {
                    if(input.getText().toString().isEmpty()) return;
                    Category category = new Category();
                    category.setNameCategory(input.getText().toString());
                    categories.add(category);
                    refreshDB();
                });
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
    if(Uid == null){
        return;
    }
    
    Map<String,Object> data = new HashMap<>();
    ArrayList<Market> markets = new ArrayList<>(DataBase.ActiveSessionUser.getMarkets());
    data.put("markets",markets);
    DataBase.reference.child(Uid).child("markets").removeValue();
    DataBase.reference.child(Uid).updateChildren(data);
    finish();
    Intent intent = new Intent(this,CategoryList.class);
    startActivity(intent);
}


}