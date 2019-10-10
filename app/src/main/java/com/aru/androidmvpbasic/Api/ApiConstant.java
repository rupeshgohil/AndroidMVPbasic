package com.aru.androidmvpbasic.Api;

import retrofit2.Retrofit;

public class ApiConstant {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    public static final String API_KEY = "2e901364c3d103dcb00ced520e9bca3c";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w200/";
    public static final String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780/";
}
