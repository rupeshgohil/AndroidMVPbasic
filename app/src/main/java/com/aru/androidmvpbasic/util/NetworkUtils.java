package com.aru.androidmvpbasic.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


/**
 * Purpose of this Class is to check internet connection of phone and perform actions on user's input
 */
public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        boolean isCheck = false;
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        isCheck = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        if(!isCheck)
            Toast.makeText(context,"No internet connection",Toast.LENGTH_LONG).show();

        return isCheck;
    }
}