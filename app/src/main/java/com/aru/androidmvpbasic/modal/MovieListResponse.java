/**
 * @file MovieListResponse.java
 * @brief This class will be useful when are going to call api for get movies.
 * @author Shrikant
 * @date 14/04/2018
 */
package com.aru.androidmvpbasic.modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieListResponse implements Serializable {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private ArrayList<Movie> results = new ArrayList<>();

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
