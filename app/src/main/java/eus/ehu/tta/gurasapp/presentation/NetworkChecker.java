package eus.ehu.tta.gurasapp.presentation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jontx on 17/01/2018.
 */

public class NetworkChecker {

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (conn != null)
            networkInfo = conn.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public static int getConnType(Context context) {

        int type = -1;
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (conn != null)
            networkInfo = conn.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            type = networkInfo.getType();
        }
        return type;
    }
}
