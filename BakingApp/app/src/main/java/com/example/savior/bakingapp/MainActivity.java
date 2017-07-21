package com.example.savior.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.savior.bakingapp.adapter.Recipe_Adapter;
import com.example.savior.bakingapp.api.Client;
import com.example.savior.bakingapp.api.Interface;
import com.example.savior.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.savior.bakingapp.adapter.Recipe_Adapter.RECIPE;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_recipe)
    RecyclerView recyclerViewRecipe;
    @BindView(R.id.progress_bar)
    ProgressBar waiting;
    private ArrayList<Recipe> mRecipe;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE, mRecipe);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerViewRecipe.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            recyclerViewRecipe.setLayoutManager(new GridLayoutManager(this, 2));
        }

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelableArrayList(RECIPE);
            getRecipe();
        } else {
            showRecipe();
        }
    }


    private void showRecipe() {
        Interface mInterface = Client.getClient().create(Interface.class);
        final Type TYPE = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        Call<JsonArray> call = mInterface.getRecipe();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                mRecipe = new Gson().fromJson(response.body(), TYPE);
                getRecipe();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                waiting.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, R.string.error_no_internet, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getRecipe() {
        waiting.setVisibility(View.INVISIBLE);
        Recipe_Adapter recipeAdapter = new Recipe_Adapter(MainActivity.this, mRecipe);
        recyclerViewRecipe.setAdapter(recipeAdapter);
    }



}
