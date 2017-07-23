package com.apphousebd.austhub.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Asif Imtiaz Shaafi on 07-Dec-16.
 * Email: a15shaafi.209@gmail.com
 */

public class NetworkConnectionManager {

    public static final String FAILED = "Failed!";
    private static final String TAG = "Network";

    /***********************************************************************************
     * connect to the server where the routine is stored,then fetch the data as
     * json and call the json helper to decode the data
     ************************************************************************************/
    public static void getRoutineDataFromServer(final Context context) {

        //check if the network is connected
        if (!hasNetworkConnection(context))
            return;

        OkHttpClient client = new OkHttpClient();

        //the form body
        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .build();

        //building the url body
        Request request = new Request.Builder()
//                .url("http://10.0.2.2/routine/index.php")
                .url("http://192.168.0.103/routine/index.php")
                .post(formBody)
                .build();

        //call the url and process the data
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


//                Toast.makeText(context, "Sorry Network Connection Failed!\n" +
//                        "Try Again Later!", Toast.LENGTH_SHORT).show();

                Log.e(TAG, "onFailure:network " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i(TAG, "onResponse: " + result);
                    JsonHelper.decodeJsonData(context, result);

                } else {
//                    Toast.makeText(context, "Sorry No Responce Found From Server,Try Later!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public static Response getRoutineJson(Context context) {

        OkHttpClient client = new OkHttpClient();

//        String url = "http://www.androidtesting.tk/austhub/";
        String url = "http://10.0.2.2/austhub/index.php";

        //the form body
        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .build();

        //building the url body
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(formBody)
                .build();

        try {
            //            if (response.isSuccessful()) {
//                String result = response.body().string();
//                Log.i(TAG, result);
//                return result;
//            } else {
//                return FAILED;
//            }
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return FAILED;
        return null;
    }


    /***********************************************************************************
     * check if the user has network connection on or off, if user is not connected to
     * internet,that is has no network connection,then prompt the user to activate network
     * connection
     ************************************************************************************/
    public static boolean hasNetworkConnection(final Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        boolean connected = info != null && info.isConnectedOrConnecting();

        if (connected) {
            return true;
        } else {
            //prompt user to activate connection

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(context)
                            .setMessage("Your Internet Connection is turned off!\n" +
                                    "You need to be connected to internet to use this service!")
                            .setPositiveButton("Turn On Internet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            })
                            .setNegativeButton("Cancel", null);

            Dialog dialog = builder.create();
            dialog.show();

            return false;
        }
    }

    public static boolean isConnectedToInternet(final Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnectedOrConnecting();
    }

}
