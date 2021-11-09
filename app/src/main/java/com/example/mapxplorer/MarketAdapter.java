package com.example.mapxplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Market.Market;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ProductViewHolder>  {
    interface OnClickListener{
        void onProductClick(Market product, int position);
    }
    private OnClickListener onClickListener;
    private List<Market> markets;
    protected static int change = 0;
    public MarketAdapter(List<Market> markets, OnClickListener onClickListener){
        this.markets = markets;
        this.onClickListener = onClickListener;
    }

    @NonNull @Override public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        change++;

        View view = layoutInflater.inflate(R.layout.item_market,parent,false);

        return new ProductViewHolder(view);
    }
    @Override public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(markets.get(position));
        Market product = markets.get(position);

        holder.itemView.setOnClickListener(v -> {
            // вызываем метод слушателя, передавая ему данные
            onClickListener.onProductClick(product, position);
        });
    }
    @Override
    public int getItemCount() {
        return markets.size();
    }
    static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView nameMarket;
        TextView openTime;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            nameMarket = itemView.findViewById(R.id.marketName);
            openTime = itemView.findViewById(R.id.openTime);
        }

        @SuppressLint("SetTextI18n")
        void bind(Market market){
            nameMarket.setText(market.getNameMarket());
            openTime.setText(market.getOpenTime());
        }
    }
}
