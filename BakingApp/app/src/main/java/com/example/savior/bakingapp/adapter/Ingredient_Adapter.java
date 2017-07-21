package com.example.savior.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savior.bakingapp.R;
import com.example.savior.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by savior on 7/18/2017.
 */

public class Ingredient_Adapter extends RecyclerView.Adapter<Ingredient_Adapter.IngredientViewHolder> {


    private ArrayList<Ingredient> mIngredient;

    public Ingredient_Adapter(ArrayList<Ingredient> ingredients) {
        this.mIngredient = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient, parent, false);
        return new IngredientViewHolder(view);

    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        holder.onRelate(position);
    }
    @Override
    public int getItemCount() {
        return mIngredient.size() ;
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;
        @BindView(R.id.tv_measure)
        TextView tvMeasure;
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;


        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onRelate(int position){
            tvIngredient.setText(mIngredient.get(position).getAll());
            tvQuantity.setText(Integer.toString(mIngredient.get(position).getQuantity()));
            tvMeasure.setText(mIngredient.get(position).getMeasure());

        }


    }

}

