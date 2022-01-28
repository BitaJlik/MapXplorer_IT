package com.example.mapxplorer.Adatpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mapxplorer.Market.Product;
import com.example.mapxplorer.R;
import com.example.mapxplorer.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.mapxplorer.databinding.FragmentMarketProductsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MarketProductsAdapter extends RecyclerView.Adapter<MarketProductsAdapter.ProductViewHolder> {

    public interface OnClickListener{
        void onProductClick(Product product, int position);
    }
    private final List<Product> products;
    private static int change = 0;
    public MarketProductsAdapter(List<Product> products){
        this.products = products;
    }

    @NonNull
    @Override
    public MarketProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater  = LayoutInflater.from(context);
        change++;

        View view = layoutInflater.inflate(R.layout.item,parent,false);
        if(change %2 ==0) change = 0;

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
        Product product = products.get(position);
        holder.itemView.setOnClickListener(v -> {
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
}