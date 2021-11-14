package com.example.mapxplorer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Market.Market;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Market> arrayList;
    MarketAdapter.OnClickListener listener;
    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("Creating", "Search activity");

        arrayList = new ArrayList<>();
        listener = (product, position) -> {
            DataBase.ActiveShowingMarket = DataBase.getAllMarkets().get(position);
            Intent intent = new Intent(getContext(),ShowProductsInMarket.class);
            startActivity(intent);
        };
        View view = inflater.inflate(R.layout.activity_search, container, false);
        TextView dontSee = view.findViewById(R.id.dontSeeText);
        if(!DataBase.getAllMarkets().isEmpty()){
            dontSee.setText("");
        }
        recyclerView = view.findViewById(R.id.rview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.SimpleOnItemTouchListener simple = new RecyclerView.SimpleOnItemTouchListener();
        init();
        refresh();
        recyclerView.addOnItemTouchListener(simple);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
    private void init(){
        arrayList.clear();
        arrayList.addAll(DataBase.getAllMarkets());
    }
    private void refresh(){
        init();
        MarketAdapter adapter = new MarketAdapter(arrayList,listener);
        recyclerView.setAdapter(adapter);
    }

}