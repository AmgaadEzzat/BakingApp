package com.example.savior.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savior.bakingapp.R;
import com.example.savior.bakingapp.StepsActivity;
import com.example.savior.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by savior on 7/18/2017.
 */

public class Recipe_Adapter  extends RecyclerView.Adapter<Recipe_Adapter.RecipeViewHolder> {

    private ArrayList<Recipe> mRecipe;
    private Context mContext;
    public static final String RECIPE = "recipe";

    public Recipe_Adapter(Context context, ArrayList<Recipe> recipes) {
        this.mContext = context;
        this.mRecipe = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder,int position) {
        holder.onRelate(position);
    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe)
        ImageView imageViewRecipe;
        @BindView(R.id.tv_recipe)
        TextView nameRecipe;


        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void onRelate(int position) {
            imageViewRecipe.setImageResource(R.drawable.bakingrecipe);
            nameRecipe.setText(mRecipe.get(position).getName());
            }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, StepsActivity.class);
            intent.putExtra(RECIPE, mRecipe.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}

