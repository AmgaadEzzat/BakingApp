package com.example.savior.bakingapp.api;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by savior on 7/18/2017.
 */

public interface Interface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<JsonArray> getRecipe();
}
