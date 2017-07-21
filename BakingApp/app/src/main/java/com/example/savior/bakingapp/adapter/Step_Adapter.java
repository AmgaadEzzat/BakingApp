package com.example.savior.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.savior.bakingapp.R;
import com.example.savior.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by savior on 7/18/2017.
 */

public class Step_Adapter extends RecyclerView.Adapter<Step_Adapter.StepViewHolder> {

    public static final String STEPS = "steps";
    private final OnPositionListener mClickListener;
    private Context mContext;
    private Recipe mRecipe;

    public interface OnPositionListener {
        void onPositionSelected(int position);
    }


    public Step_Adapter(Context context, Recipe recipe, OnPositionListener listener) {
        this.mContext = context;
        this.mRecipe = recipe;
        mClickListener = listener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.onRelate(position);
    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size();
    }


    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_view_ingredient_step)
        TextView tvIngredientStep;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        void onRelate(int position) {
            tvIngredientStep.setText(mRecipe.getSteps().get(position).getShortDescription());
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickListener.onPositionSelected(clickedPosition);

        }
    }
}

