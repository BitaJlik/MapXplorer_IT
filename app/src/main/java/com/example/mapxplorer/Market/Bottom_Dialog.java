package com.example.mapxplorer.Market;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.Adatpers.Info_Comments_Adapter;
import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.R;
import com.example.mapxplorer.User.Comment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bottom_Dialog  extends BottomSheetDialogFragment  {

    public static Bottom_Dialog newInstance() {
        return new Bottom_Dialog();
    }


    @SuppressLint("StaticFieldLeak") @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_info_market, container,false);
        Market market = DataBase.ActiveShowingMarket;

        RecyclerView recyclerView = view.findViewById(R.id.rv_comments);
        Info_Comments_Adapter adapter = new Info_Comments_Adapter(getContext(),market.getComments());
        LinearLayoutManager layout = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layout);
        registerForContextMenu(recyclerView);

        TextView nameMarket = view.findViewById(R.id.nameMarket);
        nameMarket.setText(market.getNameMarket());
        TextView time = view.findViewById(R.id.timeMarket);
        time.setText(market.getOpenTime());
        TextView owner = view.findViewById(R.id.contactInfoOwner);
        owner.setText(market.getOwner());
        TextView email = view.findViewById(R.id.contactInfoEmail);
        email.setText(market.getEmail());
        MaterialButton goToCategories = view.findViewById(R.id.toCategories);
        goToCategories.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CategoryList.class));
        });
        MaterialButton goToComments = view.findViewById(R.id.expand_button);
        if(DataBase.ActiveSessionUser.getEmail().equals("NULL")){
            goToComments.setVisibility(View.GONE);
        }
        else goToComments.setVisibility(View.VISIBLE);
        goToComments.setOnClickListener(v -> {
            @SuppressLint("SimpleDateFormat")
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
            Comment comment = new Comment();
            comment.setComment("");
            comment.setUser(DataBase.ActiveSessionUser.getName());
            comment.setEmail(DataBase.ActiveSessionUser.getEmail());
            comment.setDate(timeStamp);
            DataBase.ActiveShowingMarket.getComments().add(comment);
            Map<String,Object> data = new HashMap<>();

            data.put("markets",DataBase.ActiveSessionUser.getMarkets());

            DataBase.reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).updateChildren(data);
            Bottom_Dialog dialog = Bottom_Dialog.newInstance();
            dismiss();
            dialog.show(getParentFragmentManager(),"bottom");
            Toast.makeText(getActivity(), getResources().getString(R.string.type_comment), Toast.LENGTH_SHORT).show();
        });


        recyclerView.setAdapter(adapter);


        return view;
    }


}