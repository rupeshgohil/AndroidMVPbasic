package com.aru.androidmvpbasic.Api

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.aru.androidmvpbasic.movielist.presenter.MovieListview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("ParcelCreator")
class ApiRequest<T>(private var activity: Activity,
                    objectType: T,
                    private val TYPE: Int,
                    private val isShowProgressDialog: Boolean,
                    private val apiResponseInterface: MovieListview.ApiResponseInterface) : Callback<T>, Parcelable {

    private var retryCount = 0
    private var call: Call<T>? = null
    private var mProgressview: MovieListview.View? = null
    init {

        call = objectType as Call<T>?
        call!!.enqueue(this)
    }
    override fun onResponse(call: Call<T>, response: Response<T>)
    {


        Log.e("API : ",call.request().url().toString()+ " CODe "+response.code())
        if(response.code() == 200){
            if(response.isSuccessful){
                apiResponseInterface.getApiResponse(ApiResponseManager(response.body(), TYPE))

            }

        }else {

            val error = ErrorUtils.parseError(response)
            if(response.code() ==412){
                apiResponseInterface.getApiResponse(ApiResponseManager(response.body(), TYPE))
            }
           if(error.message.success != null)
            Toast.makeText(activity, error.message.success.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onFailure(call: Call<T>, error: Throwable) {
        error.printStackTrace()

        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(TAG, "Retrying... ($retryCount out of $TOTAL_RETRIES)")
            retry()
            return
        }

    }

    private fun retry() {
        call!!.clone().enqueue(this)
    }

    companion object {
        private val TAG = "ApiRequest"
        private val TOTAL_RETRIES = 1
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(TYPE)
        parcel.writeByte(if (isShowProgressDialog) 1 else 0)
        parcel.writeInt(retryCount)
    }

    override fun describeContents(): Int {
        return 0
    }


}
