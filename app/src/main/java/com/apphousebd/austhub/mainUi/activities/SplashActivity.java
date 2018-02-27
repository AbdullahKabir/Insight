/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:43 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.mainUi.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.BuildConfig;
import com.apphousebd.austhub.R;
import com.apphousebd.austhub.backgroundTasks.DownloadRoutineAsyncTaskLoader;
import com.apphousebd.austhub.dataBase.RoutineDatabase;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.userAuth.SignUp;
import com.apphousebd.austhub.utilities.Constants;
import com.apphousebd.austhub.utilities.JsonHelper;
import com.apphousebd.austhub.utilities.NetworkConnectionManager;
import com.apphousebd.austhub.utilities.Utils;
import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants.TABLE_NAME;
import static com.apphousebd.austhub.mainUi.activities.DialogActivity.ERROR;
import static com.apphousebd.austhub.mainUi.activities.DialogActivity.INTERNET_ON;
import static com.apphousebd.austhub.utilities.NetworkConnectionManager.FAILED;

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final int SPLASH_REQUEST = 1011;
    private static final String TAG = "SplashActivity";
    private static final int REQUEST_CODE = 1010;
    private static final int RC_SIGN_IN = 1110;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1101;

    ///progress dialog
    ProgressBar mProgressBar;
    LinearLayout mLinearLayout;
    GifImageView mGifImageView;
    private CoordinatorLayout mContainerCoordinatorLayout;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        MobileAds.initialize(this, "ca-app-pub-5940609524922490~8528862964");


        if (getResources().getConfiguration().orientation != ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_splash);
        } else {
            setContentView(R.layout.activity_splash_land);
        }

        mLinearLayout = findViewById(R.id.splash_download_container);
        mContainerCoordinatorLayout = findViewById(R.id.splash_container);

        mProgressBar = findViewById(R.id.splash_progress);
        mProgressBar.setMax(100);

        if (checkPlayServices()) {

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
                proceedToApp();
            }
        }
    }


    private void proceedToApp() {

//        getting the firebase database reference
        reference = FirebaseDatabase.getInstance().getReference("user");
        reference.keepSynced(true);

//        getting the user, if any
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //gif //asset file
        try {
            GifDrawable gifFromAssets = new GifDrawable(getAssets(), "loading.gif");
            mGifImageView = findViewById(R.id.gif);
            mGifImageView.setVisibility(View.VISIBLE);
            mGifImageView.setImageDrawable(gifFromAssets);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

//          if no current sign in user if found
            if (firebaseUser == null) {

                getFirebaseAuthUser();

            } else {
                checkUserInDatabase(firebaseUser);
            }

        }
    }

    private void getFirebaseAuthUser() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.PhoneBuilder()
                                                .setDefaultCountryIso("bd")
                                                .build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()
                                ))
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */)
                        .build(),
                RC_SIGN_IN);
    }

    /**
     * Checking if the current authorized user is in the firebase database or not,
     * if in database, means user already registered, then proceed to main page
     * otherwise register the user
     *
     * @param firebaseUser FirebaseUser
     */
    public void checkUserInDatabase(final FirebaseUser firebaseUser) {
        final String userId = firebaseUser.getUid();

        Log.d(TAG, "checkUserInDatabase: user provider: " + firebaseUser.getProviders().size());
        for (String provider : firebaseUser.getProviders()) {
            Log.d(TAG, "checkUserInDatabase: provider: " + provider);
        }

        Log.d(TAG, "checkUserInDatabase");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).exists()) {

                    UserModel model = dataSnapshot.child(userId).getValue(UserModel.class);
                    Utils.saveUser(getApplicationContext(), model);

                    proceedToMain(model);

                } else {
                    signUpUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                showSnackbar(R.string.unknown_error);
            }
        });

    }

    private void signUpUser() {
        startActivityForResult(new Intent(SplashActivity.this, SignUp.class),
                SPLASH_REQUEST);
    }

    private void proceedToMain(final UserModel model) {
        Log.d(TAG, "proceedToMain");
        if (model == null) {
            Log.d(TAG, "proceedToMain: model is null");
            signUpUser();
            return;
        }

        RoutineDatabase database = new RoutineDatabase(getBaseContext());
        long count = database.getTableItemCount(TABLE_NAME + "_" + model.getDept());
        database.closeDatabase();

        if (Utils.getDataVersion(getApplicationContext()) == 0 && count <= 0) {
            Log.d(TAG, "proceedToMain: downloading data");

            downloadData(model.getDept());
        } else {
            Log.d(TAG, "proceedToMain: making intent");
            Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
            mainIntent.setAction("user");
            mainIntent.putExtra(getString(R.string.user), model);
            startActivity(mainIntent);
            mGifImageView.setVisibility(View.GONE);
            Log.d(TAG, "proceedToMain: intent started");
            finish();
        }
    }

    private void showSnackbar(int id) {
        if (mGifImageView != null) {
            mGifImageView.setVisibility(View.GONE);
        }
        Snackbar snackbar = Snackbar
                .make(mContainerCoordinatorLayout, id, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        proceedToApp();
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {

            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK && response != null) {
                checkUserInDatabase(FirebaseAuth.getInstance().getCurrentUser());
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_error);
        } else if (requestCode == SPLASH_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.getAction() != null) {
                    if (data.getAction().equals(Constants.DONE)) {
                        finish();
                    } else if (data.getAction().equals(Constants.DOWNLOAD)) {
                        String dept = String.valueOf(data.getData());

                        RoutineDatabase database = new RoutineDatabase(getBaseContext());
                        long count = database.getTableItemCount(TABLE_NAME + "_" + dept);
                        database.closeDatabase();

                        Log.d(TAG, "onActivityResult: dept: " + dept);

                        if (count == 0) {
                            downloadData(dept);
                        }
                    }
                }
            }
        }
    }

    private void downloadData(String dept) {
        mGifImageView.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
//        DownloadRoutineFromServer server =
//                new DownloadRoutineFromServer(this, mProgressBar, mLinearLayout, dept);
//        server.execute();

        Bundle bundle = new Bundle();
        bundle.putString("dept", dept);

        getSupportLoaderManager().restartLoader(11011, bundle, this).forceLoad();

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new DownloadRoutineAsyncTaskLoader(getBaseContext(), args.getString("dept"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String fileString) {
        Intent i = null;

        Log.d(TAG, "onLoadFinished: string: " + fileString);

        //true meaning that the data is already downloaded
        if (mLinearLayout != null && !TextUtils.isEmpty(fileString) &&
                !fileString.contains(FAILED)) {
            //at this point there is no data saved for the user

            //decode the data
            Log.d(TAG, "onLoadFinished: file string: " + fileString);
            String[] results = fileString.split(">");
            if (results.length > 1) {

                i = new Intent(getBaseContext(), MainActivity.class);

                Log.i(TAG, "adding in database: " + results[results.length - 2] + " " + results[results.length - 1]);
                JsonHelper.decodeJsonData(getBaseContext(), results[results.length - 1]);

                Log.d(TAG, "onPostExecute: version: " + Long.parseLong(results[results.length - 2].trim()));
                Utils.setDataVersion(getBaseContext().getApplicationContext(), Long.parseLong(results[results.length - 2].trim()));

            } else {

                i = new Intent(getBaseContext(), DialogActivity.class);

                i.putExtra(ERROR, getBaseContext().getString(R.string.splah_data_fetch_error_msg));
            }

        } else if (TextUtils.isEmpty(fileString) || fileString.equals(FAILED)) {

            //something wrong,so call error activity to show error dialog

            i = new Intent(getBaseContext(), DialogActivity.class);

            if (NetworkConnectionManager.isConnectedToInternet(getBaseContext())) {

                String error = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                        .getString(getString(R.string.download_error), "");
                if (!TextUtils.isEmpty(error) && error.contains("failed to connect")) {
                    i.putExtra(ERROR, "Couldn't connect to the server. Please try again." +
                            "\nIf error still exists, please inform the developers");
                    i.putExtra("inform", true);
                } else {
                    i.putExtra(ERROR, getString(R.string.splah_data_fetch_error_msg));
                }

            } else {

//                    Log.e("splash", "onPostExecute: inactive network");

                i.putExtra(ERROR, getString(R.string.splash_internet_error_msg));

                i.putExtra(INTERNET_ON, true);
            }
        }

        // close this activity
        if (i != null) {
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     *
     * @return boolean
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }
}
