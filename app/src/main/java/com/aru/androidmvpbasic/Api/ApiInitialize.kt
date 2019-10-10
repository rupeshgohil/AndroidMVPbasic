package com.aru.androidmvpbasic.Api

import com.aru.androidmvpbasic.Api.ApiConstant.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit




object ApiInitialize {

    private var retrofit: Retrofit? = null
    private lateinit var apiInIt: ApiInterface

    var url=""

    @Synchronized
    fun getApiInIt(): ApiInterface {
        return apiInIt
    }

    fun initialize(): Retrofit {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        if (ApiInitialize.retrofit == null) {
            ApiInitialize.retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(ApiInitialize.requestHeader)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }

        return ApiInitialize.retrofit!!
    }

    @JvmStatic
    fun initializes(): ApiInterface {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(requestHeader)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        apiInIt = retrofit!!.create(ApiInterface::class.java)
        return apiInIt
    }
    private val requestHeader: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build()
        }

}
