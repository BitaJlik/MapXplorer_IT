package com.example.mapxplorer.Adatpers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Market.Product;
import com.example.mapxplorer.R;

import java.util.List;

public class RvProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Product> list;
    Context context;

    public RvProductAdapter(Context context, List<Product> articlesList) {
        this.list = articlesList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ProductHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Product product = list.get(position);
        if(holder instanceof ProductHolder){
           ((ProductHolder) holder).name.setText(product.getNameProduct());
           ((ProductHolder) holder).price.setText(String.valueOf(product.getPrice()));
           ((ProductHolder) holder).amount.setText(String.valueOf(product.getAmount()));
           if(product.isDiscount()){
               ((ProductHolder) holder).name.setTextColor(Color.RED);
           }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView amount;

        public ProductHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            amount = itemView.findViewById(R.id.productAmount);
        }
    }
}