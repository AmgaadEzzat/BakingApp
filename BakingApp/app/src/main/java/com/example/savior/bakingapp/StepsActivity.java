package com.example.savior.bakingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.savior.bakingapp.database.Contract.*;
import com.example.savior.bakingapp.adapter.Step_Adapter;
import com.example.savior.bakingapp.model.Ingredient;
import com.example.savior.bakingapp.model.Recipe;

import java.util.ArrayList;

import static com.example.savior.bakingapp.R.layout.recipe;
import static com.example.savior.bakingapp.adapter.Recipe_Adapter.RECIPE;
import static com.example.savior.bakingapp.adapter.Step_Adapter.STEPS;
import static com.example.savior.bakingapp.database.Contract.RECIPE_CONTENT_URI;

/**
 * Created by savior on 7/18/2017.
 */

public class StepsActivity  extends AppCompatActivity implements Step_Adapter.OnPositionListener{

    private Recipe mRecipe;
    public static final String POSITION = "position";
    public static final String PANES = "panes";
    private boolean isTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_activity);
        mRecipe = getIntent().getParcelableExtra(RECIPE);
        isTab = findViewById(R.id.detail_linear_layout) != null;
    }

    @Override
    public void onPositionSelected(int position) {
        Bundle bundle = new Bundle();

        if (isTab) {
            StepsDetailsFragment fragment = new StepsDetailsFragment();
            bundle.putInt(POSITION, position);
            bundle.putBoolean(PANES, isTab);
            bundle.putParcelableArrayList(STEPS, mRecipe.getSteps());
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ingredient_step_frame, fragment).commit();
        }
        else if (isTab && position == 0) {
            StepsDetailsFragment fragment = new StepsDetailsFragment();
            bundle.putInt(POSITION, position);
            bundle.putBoolean(PANES, isTab);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ingredient_step_frame, fragment).commit();
        }
        else {
            bundle.putInt(POSITION, position);
            bundle.putBoolean(PANES, isTab);
            bundle.putParcelableArrayList(STEPS, mRecipe.getSteps());
            Intent intent = new Intent(StepsActivity.this, StepsDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite_w, menu);
        return true;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_widget:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (isFavorite()) {
                        deleteFromWidget();
                        item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                        Toast.makeText(this, String.format(getString(R.string.remove_from_widget), mRecipe.getName()), Toast.LENGTH_LONG).show();
                    } else {
                        addToWidget();
                        item.setIcon(R.drawable.ic_favorite_black_24dp);
                        Toast.makeText(this, String.format(getString(R.string.add_to_widget), mRecipe.getName()), Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_widget);
        if (isFavorite()) {
            menuItem.setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            menuItem.setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
        return true;
    }


    synchronized private void deleteFromWidget() {
        getContentResolver().delete(RECIPE_CONTENT_URI, null, null);
    }

    synchronized private void addToWidget() {
        getContentResolver().delete(RECIPE_CONTENT_URI, null, null);
        getDetails(mRecipe.getAlls());
    }


    private void getDetails(ArrayList<Ingredient> ingredients) {

        for (Ingredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put(RecipeEntry.COLUMN_RECIPE_ID, mRecipe.getId());
            values.put(RecipeEntry.COLUMN_RECIPE_NAME, mRecipe.getName());
            values.put(RecipeEntry.COLUMN_INGREDIENT_NAME, ingredient.getAll());
            values.put(RecipeEntry.COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());
            values.put(RecipeEntry.COLUMN_INGREDIENT_MEASURE, ingredient.getMeasure());
            getContentResolver().insert(RECIPE_CONTENT_URI, values);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean isFavorite() {
        String[] projection = {RecipeEntry.COLUMN_RECIPE_ID};
        String selection = RecipeEntry.COLUMN_RECIPE_ID + " = " + mRecipe.getId();
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(RECIPE_CONTENT_URI,
                projection,
                selection,
                null,
                null,
                null);

        return (cursor != null ? cursor.getCount() : 0) > 0;
    }

}
