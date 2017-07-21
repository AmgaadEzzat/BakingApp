package com.example.savior.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.savior.bakingapp.adapter.Ingredient_Adapter;
import com.example.savior.bakingapp.adapter.Step_Adapter;
import com.example.savior.bakingapp.model.Ingredient;
import com.example.savior.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.savior.bakingapp.adapter.Recipe_Adapter.RECIPE;

/**
 * Created by savior on 7/18/2017.
 */

public class StepsFragment  extends Fragment {

    private Step_Adapter mStepAdapter;
    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredient;
    private Ingredient_Adapter mIngredientAdapter;


    @BindView(R.id.recycler_view_ingredients)
    RecyclerView rvIngredient;
    @BindView(R.id.rv_ingredients_steps)
    RecyclerView rvIngredientStep;

    private Step_Adapter.OnPositionListener mClickListener;

    public StepsFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE, mRecipe);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
        }
        final View rootView = inflater.inflate(R.layout.ingredient_step_fragment, container, false);
        ButterKnife.bind(this, rootView);
        mRecipe = getActivity().getIntent().getParcelableExtra(RECIPE);
        mIngredient = mRecipe.getAlls();

        mStepAdapter = new Step_Adapter(getContext(), mRecipe, mClickListener);
        rvIngredientStep.setAdapter(mStepAdapter);

        mIngredientAdapter = new Ingredient_Adapter(mIngredient);
        rvIngredient.setAdapter(mIngredientAdapter);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClickListener = (Step_Adapter.OnPositionListener) context;
        } catch (ClassCastException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

}

