/**
 * @file Credits.java
 * @brief This is a pojo class, which will hold list of casts
 * @author Shrikant
 * @date 16/04/2018
 */
package com.aru.androidmvpbasic.modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Credits implements Serializable {

    @SerializedName("cast")
    private ArrayList<Cast> cast = new ArrayList<>();

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }
}
