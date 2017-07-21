package com.example.savior.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.savior.bakingapp.model.Step;

import java.util.ArrayList;

import static com.example.savior.bakingapp.adapter.Step_Adapter.STEPS;

/**
 * Created by savior on 7/18/2017.
 */

public class StepsDetailsActivity  extends AppCompatActivity {


    int position;
    boolean isTab;
    ArrayList<Step> mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_details_activity);
        if (savedInstanceState == null) {
            position = getIntent().getIntExtra(StepsActivity.POSITION, 0);
            isTab = getIntent().getBooleanExtra(StepsActivity.PANES, false);
            mRecipe = getIntent().getParcelableArrayListExtra(STEPS);
            StepsDetailsFragment fragment = new StepsDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(StepsActivity.POSITION, position);
            bundle.putBoolean(StepsActivity.PANES, isTab);
            bundle.putParcelableArrayList(STEPS, mRecipe);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.step_details_frame, fragment).commit();

        }
    }

}

