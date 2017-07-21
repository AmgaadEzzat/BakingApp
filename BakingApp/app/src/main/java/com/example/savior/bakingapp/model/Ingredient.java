package com.example.savior.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by savior on 7/18/2017.
 */

public class Ingredient implements Parcelable {

    @SerializedName("ingredient")
    private String ingredient;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("measure")
    private String measure;


    public Ingredient() {
    }

    protected Ingredient(Parcel in) {
        this.ingredient = in.readString();
        this.quantity = in.readInt();
        this.measure = in.readString();

    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public int getQuantity() {
        return quantity;
    }

    public Ingredient setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getMeasure() {
        return measure;
    }

    public Ingredient setMeasure(String measure) {
        this.measure = measure;
        return this;
    }

    public String getAll() {
        return ingredient;
    }

    public Ingredient setIngredient(String ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ingredient);
        dest.writeInt(this.quantity);
        dest.writeString(this.measure);

    }

    @Override
    public String toString() {
        return "Ingredient{" +
                ", ingredient='" + ingredient + '\'' +
                    "quantity=" + quantity +
                     ", measure='" + measure + '\'' + '}';
    }
}

