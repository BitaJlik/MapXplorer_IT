package com.example.mapxplorer.Adatpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Market.Product;
import com.example.mapxplorer.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
    public interface OnClickListener{
        void onProductClick(Product product, int position);
    }
    private final OnClickListener onClickListener;
    private final List<Product> products;
    private final List<Product> productList;
    private static int change = 0;
    public ProductAdapter(List<Product> products,OnClickListener onClickListener){
        this.products = products;
        this.onClickListener = onClickListener;
        this.productList = new ArrayList<>(products);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater  = LayoutInflater.from(context);
        change++;

        View view = layoutInflater.inflate((change%2==0) ? R.layout.item : R.layout.item_colored,parent,false);
        if(change %2 ==0) change = 0;

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
        Product product = products.get(position);
        holder.itemView.setOnClickListener(v -> {
            onClickListener.onProductClick(product, position);
        });
    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView nameProduct;
        TextView priceProduct;
        TextView amountProduct;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            nameProduct = itemView.findViewById(R.id.productName);
            priceProduct = itemView.findViewById(R.id.productPrice);
            amountProduct = itemView.findViewById(R.id.productAmount);


        }

        @SuppressLint("SetTextI18n")
        void bind(Product product){
            nameProduct.setText(product.getNameProduct().trim());
            if(product.isDiscount()){
                priceProduct.setTextColor(Color.RED);
                priceProduct.setTypeface(Typeface.DEFAULT_BOLD);
            }
            priceProduct.setText(product.getPrice() + "грн");
            amountProduct.setText(product.getAmount() + "шт");
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filtered = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filtered.addAll(productList);
            }else {
                String stringPattern = constraint.toString().toLowerCase().trim();

                for(Product product : productList){
                    if(product.getNameProduct().toLowerCase().contains(stringPattern)){
                        filtered.add(product);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            products.clear();
            products.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

