package com.example.mapxplorer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Adatpers.MarketAdapter;
import com.example.mapxplorer.Market.Market;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Market> arrayList;
    static MarketAdapter adapter;
    MarketAdapter.OnClickListener listener;
    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("Creating", "Search activity");

        arrayList = new ArrayList<>();
        listener = (product, position) -> {
            DataBase.ActiveShowingMarket = DataBase.getAllMarkets().get(position);
            MapsActivity.isViewMap = true;
            MapsActivity.view.setCheckedItem(R.id.nav_Map);
            getParentFragmentManager().beginTransaction().replace(R.id.fragment, MapsActivity.fragment).commit();
            MyCustomMapFragment.onMarket = true;
//            Intent intent = new Intent(getContext(),CategoryList.class);
//            startActivity(intent);
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
        setListener();
        return view;
    }
    private void init(){
        arrayList.clear();
        arrayList.addAll(DataBase.getAllMarkets());
    }
    private void refresh(){
        init();
        adapter = new MarketAdapter(arrayList,listener);
        recyclerView.setAdapter(adapter);
    }
    static void setListener(){
        MapsActivity.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}