package com.example.mapxplorer.Market;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Adatpers.RvProductAdapter;
import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class Edit extends AppCompatActivity  {
    Product tempProduct;
    int tempPos;
    Context context;
    View view;
    MaterialButton add;
    private RecyclerView recyclerView;
    RvProductAdapter adapter;
    SwipeController swipeController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_edit, null, false);
        setContentView(view);
        add = findViewById(R.id.add);
        add.setOnClickListener(v -> add());
        recyclerView = findViewById(R.id.rview);

        swipeController = new SwipeController(new SwipeController.SwipeControllerActions() {
                @Override public void onRightClicked(int position) { delete(position); }
                @Override public void onLeftClicked(int position){ edit(position); }

        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RvProductAdapter(this, DataBase.category.getProducts());

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
       

    }
    public void isUndo(){
        Snackbar.make(context,view ,getStr(R.string.undo) , Snackbar.LENGTH_LONG).setAction(R.string.undo, v -> {
            DataBase.category.getProducts().add(tempProduct);
            adapter.notifyItemInserted(tempPos);
            refreshDB();

        }).show();

    }


    @SuppressLint("NotifyDataSetChanged")
    public void edit(int pos){
        if(DataBase.category.getProducts().isEmpty()){ return; }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View edit  = inflater.inflate(R.layout.edit_product_dialog, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("dialog");
        dialog.setView(edit);
        final AlertDialog test = dialog.create();

        System.out.println(DataBase.category);
        EditText name = edit.findViewById(R.id.edit_Name);
        EditText price = edit.findViewById(R.id.edit_Price);
        EditText amount = edit.findViewById(R.id.edit_Amount);
        CheckBox discount = edit.findViewById(R.id.edit_Discount);

        Product product = DataBase.category.getProducts().get(pos);
        name.setText(product.getNameProduct());
        price.setText(String.valueOf(product.getPrice()));
        amount.setText(String.valueOf(product.getAmount()));
        discount.setChecked(product.isDiscount());
        Button save = edit.findViewById(R.id.edit_Save);
        save.setOnClickListener(v -> {
            test.dismiss();
            String nameS = name.getText().toString();
            int amountS = Integer.parseInt(amount.getText().toString().trim());
            double priceS = Double.parseDouble(price.getText().toString().trim());
            boolean discountS = (discount.isChecked());
            if(nameS.equals("")){ nameS = "Продукт"; }
            if(amountS < 0){ amountS = 0; }
            if(priceS < 0){ priceS = 0.0; }
            product.setNameProduct(nameS);
            product.setPrice(priceS);
            product.setAmount(amountS);
            product.setDiscount(discountS);
            refreshDB();
            adapter.notifyDataSetChanged();

        });
        test.show();
    }
    public void add(){
        Product product = new Product();
        Snackbar.make(view, getResources().getString(R.string.edit_this), Snackbar.LENGTH_LONG).show();
        product.setNameProduct("Edit");
        product.setAmount(0);
        product.setPrice(0);
        DataBase.ActiveShowingMarket.getCategories().get(DataBase.posCategory).getProducts().add(product);
        refreshDB();
    }
    public void delete(int position){
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
        tempProduct = DataBase.category.getProducts().get(position);
        isUndo();
        tempPos = position;
        DataBase.category.getProducts().remove(position);

        refreshDB();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void refreshDB(){
        String Uid = FirebaseAuth.getInstance().getUid();
        if (Uid == null) { return ; }
        Map<String, Object> data = new HashMap<>();
        data.put(Uid, DataBase.ActiveSessionUser);
        System.out.println(DataBase.category);
        DataBase.reference.child(Uid).removeValue();
        DataBase.reference.updateChildren(data);
        adapter = new RvProductAdapter(this, DataBase.ActiveShowingMarket.getCategories().get(DataBase.posCategory).getProducts());

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public String getStr(int Id){
        return getApplicationContext().getString(Id);
    }
}