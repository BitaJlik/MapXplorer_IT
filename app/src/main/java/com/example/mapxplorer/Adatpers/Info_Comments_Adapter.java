package com.example.mapxplorer.Adatpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.MainActivity;
import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.R;
import com.example.mapxplorer.User.Comment;
import com.example.mapxplorer.User.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Info_Comments_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private final List<Comment> list;
    public View.OnCreateContextMenuListener listener;
    Context context;
    public Info_Comments_Adapter(Context context, ArrayList<Comment> articlesList) {
        this.list = new ArrayList<>();
        this.context = context;
        list.addAll(articlesList);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new Info_Comments_Adapter.ProductHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Comment commen = list.get(position);
        ((ProductHolder) holder).name.setText(commen.getUser());
        ((ProductHolder) holder).comment.setText(commen.getComment());
        ((ProductHolder) holder).date.setText(commen.getDate());
        if (commen.getWarns() >= 3){
            ((ProductHolder) holder).name.setTextColor(Color.GREEN);
        }
        else if(commen.getWarns() <= -3){
            ((ProductHolder) holder).name.setTextColor(Color.parseColor("#FF4C4C"));
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    String text ;


    public class ProductHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {
        TextView name;
        TextView comment;
        TextView date;
        public ProductHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);

            name = itemView.findViewById(R.id.item_user);
            comment = itemView.findViewById(R.id.item_comment);
            date = itemView.findViewById(R.id.item_time);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Actions");
            if(!DataBase.ActiveSessionUser.getEmail().equals("NULL")){
                for(int i = 0;i < list.size();i++) {
                    if(getAdapterPosition() == i) {
                        if(list.get(i).getEmail().equals(DataBase.ActiveSessionUser.getEmail())){
                            menu.add(0, v.getId(), 0, "Edit")
                                    .setOnMenuItemClickListener(item -> {
                                        TextView comment = v.findViewById(R.id.item_comment);
                                        EditText edit = v.findViewById(R.id.edit_comment);
                                        MaterialButton cancel = v.findViewById(R.id.edit_cancel);
                                        MaterialButton save = v.findViewById(R.id.edit_save);
                                        text = comment.getText().toString();
                                        edit.setText(text);
                                        comment.setVisibility(View.GONE);
                                        save.setVisibility(View.VISIBLE);
                                        save.setClickable(true);
                                        cancel.setClickable(true);
                                        cancel.setVisibility(View.VISIBLE);
                                        edit.setVisibility(View.VISIBLE);

                                        save.setOnClickListener(v1 -> {
                                            System.out.println("+");
                                            text = edit.getText().toString();
                                            comment.setText(text);
                                            DataBase.ActiveShowingMarket.getComments()
                                                    .get(getAdapterPosition()).setComment(text);
                                            save.setVisibility(View.GONE);
                                            cancel.setVisibility(View.GONE);
                                            edit.setVisibility(View.GONE);
                                            comment.setVisibility(View.VISIBLE);

                                            save.setClickable(false);
                                            cancel.setClickable(false);
                                            refreshInDB();
                                        });
                                        cancel.setOnClickListener(v12 -> {
                                            System.out.println("-");
                                            save.setVisibility(View.GONE);
                                            cancel.setVisibility(View.GONE);
                                            edit.setVisibility(View.GONE);
                                            comment.setVisibility(View.VISIBLE);

                                            save.setClickable(false);
                                            cancel.setClickable(false);
                                        });

                                        //
                                        return true;
                                    });
                            menu.add(0, v.getId(), 0, "Delete")
                                    .setOnMenuItemClickListener(item -> {
                                        DataBase.ActiveShowingMarket.getComments().remove(getAdapterPosition());
                                        list.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                        refreshInDB();
                                        return true;
                                    });
                        }
                        break;
                    }
                }

                menu.add(0, v.getId(), 0, "Like")
                        .setOnMenuItemClickListener(item -> {
                            DataBase.ActiveShowingMarket.getComments().get(getAdapterPosition())
                                    .setWarns(DataBase.ActiveShowingMarket.getComments().get(getAdapterPosition()).getWarns() + 1);
                            Toast.makeText(context,context.getApplicationContext().getString(R.string.liked) , Toast.LENGTH_SHORT).show();

                            refreshInDB();
                            return true;
                        });
                menu.add(0, v.getId(), 0, "Warn")
                        .setOnMenuItemClickListener(item -> {
                            DataBase.ActiveShowingMarket.getComments().get(getAdapterPosition())
                                    .setWarns(DataBase.ActiveShowingMarket.getComments().get(getAdapterPosition()).getWarns() - 1);
                            Toast.makeText(context,context.getApplicationContext().getString(R.string.warned) , Toast.LENGTH_SHORT).show();
                            refreshInDB();
                            return true;
                        });
            }
            else {
                Toast.makeText(context,context.getApplicationContext().getString(R.string.please_login) , Toast.LENGTH_SHORT).show();
            }
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    public void refreshInDB(){
        Map<String, Object> data = new HashMap<>();
        for(User user : DataBase.users){
            if(user.getEmail().equals(DataBase.ActiveShowingMarket.getEmail())){
                data.put("markets",user.getMarkets());
                break;
            }
        }
        DataBase.reference.child(DataBase.ActiveShowingMarket.getUserUid()).child("markets").removeValue();
        DataBase.reference.child(DataBase.ActiveShowingMarket.getUserUid()).updateChildren(data);
        notifyDataSetChanged();
    }
}
