package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Market.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>  {
    interface OnClickListener{
        void onProductClick(Product product, int position);
    }
    private OnClickListener onClickListener;
    private List<Product> products;
    private static int change = 0;
    public ProductAdapter(List<Product> products,OnClickListener onClickListener){
        this.products = products;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater  = LayoutInflater.from(context);
        change++;

        View view = layoutInflater.inflate((change%2==0) ? R.layout.item : R.layout.item_colored,parent,false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
        Product product = products.get(position);

        holder.itemView.setOnClickListener(v -> {
            // вызываем метод слушателя, передавая ему данные
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
            nameProduct.setText(product.getNameProduct());
            priceProduct.setText(product.getPrice() + "грн");
            amountProduct.setText(product.getAmount() + "шт");
        }
    }
}
