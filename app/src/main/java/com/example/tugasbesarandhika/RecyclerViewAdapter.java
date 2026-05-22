package com.example.tugasbesarandhika;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tugasbesarandhika.databinding.ItemLayoutBinding;

import java.util.List;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.GridViewHolder>{

    private List<Meal> meals;
    private Context context;
    private ItemLayoutBinding binding;

    public RecyclerViewAdapter(Context context, List<Meal> meals) {
        this.meals = meals;
        this.context = context;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = binding.inflate(LayoutInflater.from(parent.
                getContext()), parent, false);
        return new GridViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        final String
                id = meals.get(position).getIdMeal(),
                meal = meals.get(position).getStrMeal(),
                photo = meals.get(position).getStrMealThumb();
        binding.tvMeal.setText(meal);

        Glide.with(context)
                .load(photo)
                .centerCrop()
                .placeholder(R.drawable.broken_image_24)
                .into(binding.imgMeal);

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, DetailActivity.class);
            i.putExtra("i_idMeal", id);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        //agar data yang ditampilkan stabil
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        private final ItemLayoutBinding binding;

        public GridViewHolder(ItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}