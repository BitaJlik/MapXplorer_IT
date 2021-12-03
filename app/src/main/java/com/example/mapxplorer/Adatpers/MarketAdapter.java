package com.example.mapxplorer.Adatpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.R;

import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ProductViewHolder>  implements Filterable {
    public interface OnClickListener{
        void onProductClick(Market product, int position);
    }
    private final OnClickListener onClickListener;
    private final List<Market> markets;
    private final List<Market> marketList;
    protected static int change = 0;
    public MarketAdapter(List<Market> markets, OnClickListener onClickListener){
        this.markets = markets;
        this.marketList = new ArrayList<>(markets);
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
    @Override
    public Filter getFilter() {
        return filter;
    }
    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Market> filtered = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filtered.addAll(marketList);
            }else {
                String stringPattern = constraint.toString().toLowerCase().trim();

                for(Market market : marketList){
                    if(market.getNameMarket().toLowerCase().contains(stringPattern)){
                        filtered.add(market);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            markets.clear();
            markets.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
