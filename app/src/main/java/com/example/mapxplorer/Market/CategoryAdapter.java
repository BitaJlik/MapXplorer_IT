package com.example.mapxplorer.Market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mapxplorer.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>  {
    public interface OnClickListener{
        void onProductClick(Category category, int position);
    }
    private OnClickListener onClickListener;
    private List<Category> categories;
    private static int change = 0;
    public CategoryAdapter(List<Category> categories, OnClickListener onClickListener){
        this.categories = categories;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater  = LayoutInflater.from(context);
        change++;

        View view = layoutInflater.inflate( R.layout.category_item,parent,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
        Category category = categories.get(position);

        holder.itemView.setOnClickListener(v -> {
            // вызываем метод слушателя, передавая ему данные
            onClickListener.onProductClick(category, position);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView nameCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            nameCategory = itemView.findViewById(R.id.categoryName);
        }

        @SuppressLint("SetTextI18n")
        void bind(Category category){
            nameCategory.setText(category.getNameCategory());
        }
    }
}

