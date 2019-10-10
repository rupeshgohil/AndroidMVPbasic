package com.aru.androidmvpbasic.Api
import com.aru.androidmvpbasic.modal.Movie
import com.aru.androidmvpbasic.modal.MovieListResponse
import retrofit2.Call
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.*


interface ApiInterface {
    @GET("movie/popular")
    fun getMovieList(
        @Query("api_key") apiKey :String ,
        @Query("page") PageNo : Int ): Call<MovieListResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") credits: String): Call<Movie>


}
