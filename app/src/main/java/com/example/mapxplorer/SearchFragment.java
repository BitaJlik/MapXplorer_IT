package com.example.mapxplorer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
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
        listener = (product, position) ->
                Toast.makeText(getContext(), "Вибрано " + product.getNameMarket(),
                        Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.activity_search, container, false);
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