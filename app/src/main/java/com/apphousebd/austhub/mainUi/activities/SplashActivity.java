package com.apphousebd.austhub.mainUi.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.userAuth.UserSignIn;
import com.apphousebd.austhub.utilities.JsonHelper;
import com.apphousebd.austhub.utilities.NetworkConnectionManager;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.apphousebd.austhub.mainUi.activities.DialogActivity.ERROR;
import static com.apphousebd.austhub.mainUi.activities.DialogActivity.INTERNET_ON;
import static com.apphousebd.austhub.utilities.NetworkConnectionManager.FAILED;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private static final int REQUEST_CODE = 1010;

    private static final int SPLASH_TIME_OUT = 1200;

    //std info
    public static int STD_YEAR = 0;
    public static int STD_SEMESTER = 0;
    public static String STD_DEPT = "";

    ///progress dailog
    ProgressBar mProgressBar;
    LinearLayout mLinearLayout;

    GifImageView mGifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation != ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_splash);
        } else {
            setContentView(R.layout.activity_splash_land);
        }

        mLinearLayout = (LinearLayout) findViewById(R.id.splash_download_container);

        mProgressBar = (ProgressBar) findViewById(R.id.splash_progress);
        mProgressBar.setMax(100);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String year = preferences.getString(this.getString(R.string.std_year), "0");
        String semester = preferences.getString(getString(R.string.std_sem), "0");

        STD_YEAR = Integer.parseInt(year);
        STD_SEMESTER = Integer.parseInt(semester);

        //checking for permissions
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                showPermissionRequestReason();

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            }
        } else {
//            List<RoutineServerJsonModel> models = new ArrayList<>();
//
//            RoutineServerJsonModel model =
//                    new RoutineServerJsonModel("1", "2_2", "section_c", CSE22C);
//            models.add(model);
////
////            RoutineServerJsonModel model1 =
////                    new RoutineServerJsonModel("2", "2_1", "section_c", CSE22C);
////            models.add(model1);
//
//            String j = JsonHelper.encodeToJson(this, models);
//
//            JsonHelper.decodeJsonData(this, j);
            proceedToApp();
        }

    }

    private void showPermissionRequestReason() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("We need the permission to write in your external storage to" +
                        " save the routine data, images and the results.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedToApp();
                } else {
//                    Toast.makeText(this, "Please allow the permission to use the app!", Toast.LENGTH_LONG).show();
//                    finish();

                    showPermissionRequestReason();
                    //proceedToApp();
                }
        }

    }

    private void proceedToApp() {
        //gif //asset file
        try {
            GifDrawable gifFromAssets = new GifDrawable(getAssets(), "loading.gif");
            mGifImageView = (GifImageView) findViewById(R.id.gif);
            mGifImageView.setImageDrawable(gifFromAssets);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            new Handler().postDelayed(new Runnable() {

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */

                @Override
                public void run() {

                    if (STD_YEAR == 0 && STD_SEMESTER == 0) {

//                        new DownloadRoutineFromServer(
//                                SplashActivity.this,
//                                SplashActivity.this,
//                                mProgressBar,
//                                mLinearLayout).execute();
//
//                        new CheckStdInfo(SplashActivity.this).execute();
                        startActivity(new Intent(getApplicationContext(),
                                UserSignIn.class));
                        finish();

                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }

                }
            }, SPLASH_TIME_OUT);
        }
    }

    class CheckStdInfo extends AsyncTask<Void, Integer, Void> {

        Context mContext;
        String fileString = null;
        int target = 0;

        CheckStdInfo(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (STD_YEAR == 0 && STD_SEMESTER == 0) {
                // progressDialog.show();
                mProgressBar.setProgress(0);
                mLinearLayout.setVisibility(View.VISIBLE);
                mGifImageView.setVisibility(View.GONE);
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (STD_YEAR == 0 && STD_SEMESTER == 0) {
                Response response = NetworkConnectionManager.getRoutineJson(mContext);
                Response response2 = NetworkConnectionManager.getRoutineJson(mContext);

                InputStream inputStream = null;

                try {
                    if (response != null && response2 != null
                            && response.isSuccessful() && response2.isSuccessful()) {
                        inputStream = response.body().byteStream();
                        byte[] buff = new byte[1024 * 4];
                        int downloaded = 0;
                        target = (int) response.body().contentLength();

                        //publishProgress(target);
                        fileString = response2.body().string();
//                        Log.i(TAG, "doInBackground: text: " + fileString);
//                        JsonHelper.decodeJsonData(mContext, fileString);
                        while (true) {
                            int read = inputStream.read(buff);
                            if (read == -1) {
                                break;
                            }
                            //write buffer
                            downloaded += read;
                            publishProgress(downloaded);
                            if (isCancelled()) {
                                fileString = FAILED;
                                return null;
                            }
                        }
                    } else {
                        fileString = FAILED;
                    }
                } catch (IOException e) {
                    fileString = FAILED;
                    e.printStackTrace();
                    Log.e(TAG, "doInBackground: " + e.getLocalizedMessage(), e);
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.i(TAG, "doInBackground: " + fileString);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setMax(target);
            mProgressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            mLinearLayout.setVisibility(View.GONE);
//            mGifImageView.setVisibility(View.VISIBLE);
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent i = null;

            //true meaning that the data is already downloaded
            if (STD_YEAR != 0 && STD_SEMESTER != 0) {

                i = new Intent(SplashActivity.this, MainActivity.class);

            } else if (!fileString.equals(FAILED) && !TextUtils.isEmpty(fileString)) {
                //at this point there is no data saved for the user

                //decode the data
//                Log.i(TAG, "adding in database: " + fileString);
                JsonHelper.decodeJsonData(SplashActivity.this, fileString);

                if (STD_YEAR == 0 && STD_SEMESTER == 0) {
                    //this means the app is loaded
                    i = new Intent(mContext, UserSignIn.class);

                }

            } else if (fileString.equals(FAILED) || TextUtils.isEmpty(fileString)) {

                //something wrong,so call error activity to show error dialog

                i = new Intent(mContext, DialogActivity.class);


                if (NetworkConnectionManager.isConnectedToInternet(mContext)) {

                    i.putExtra(ERROR, getString(R.string.splah_data_fetch_error_msg));

                } else {

//                    Log.e("splash", "onPostExecute: inactive network");

                    i.putExtra(ERROR, getString(R.string.splash_internet_error_msg));

                    i.putExtra(INTERNET_ON, true);
                }
            }

            if (i != null) startActivity(i);

            // close this activity
            finish();
        }
    }
}
