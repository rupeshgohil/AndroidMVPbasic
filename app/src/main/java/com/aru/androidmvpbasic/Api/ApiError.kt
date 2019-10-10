package com.aru.androidmvpbasic.Api

import com.google.gson.annotations.SerializedName


class ApiError {

    @SerializedName("status")
    var status: Int? = null

    @SerializedName("message")
    lateinit var message: Message


    inner class Message
    {
        @SerializedName("error")
        var error: String? = null

        @SerializedName("success")
        var success: String? = null


        @SerializedName("username")
        var username: String? = null

        @SerializedName("email")
        var email: String? = null

        @SerializedName("mobile")
        var mobile: String? = null


    }

}
