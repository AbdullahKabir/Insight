/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:33 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.mainUi.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.backgroundTasks.DataDownloadService;
import com.apphousebd.austhub.dataModel.DownloadModelClass;
import com.apphousebd.austhub.utilities.Animations;
import com.apphousebd.austhub.utilities.ChangeDefaultColor;
import com.apphousebd.austhub.utilities.NetworkConnectionManager;
import com.apphousebd.austhub.utilities.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.apphousebd.austhub.mainUi.activities.DialogActivity.ERROR;
import static com.apphousebd.austhub.utilities.Constants.MESSAGE_PROGRESS;
import static com.apphousebd.austhub.utilities.Constants.versionCheckUrl;

/**
 * Created by Asif Imtiaz Shaafi on 02, 2017.
 * Email: a15shaafi.209@gmail.com
 */

@SuppressWarnings("ConstantConditions")
public class UpdateActivity extends AppCompatActivity {

    private static final String TAG = "UpdateActivity";

    // progress dialog for showing the download progress
    private Dialog mDialog;
    private ProgressBar mCheckProgressBar, mDownloadProgress;
    private TextView updateDateTextView, updateVersionTextView, mUpdateDownloadProgressText;
    private LocalBroadcastManager bManager;
    private Button updateButton;
    private SimpleDateFormat mSimpleDateFormat;

    private SharedPreferences mSharedPreferences;
    long version = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mSimpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChangeDefaultColor.changeStatusColor(this, R.color.colorPrimaryDark);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        updateButton = findViewById(R.id.update_button);
        mCheckProgressBar = findViewById(R.id.update_check_progress_bar);
        mDownloadProgress = findViewById(R.id.update_download_progress_bar);
        updateDateTextView = findViewById(R.id.update_last_checked_text_view);
        updateVersionTextView = findViewById(R.id.update_last_version_text_view);
        mUpdateDownloadProgressText = findViewById(R.id.update_download_progress_text);

        String time = mSharedPreferences.getString(getString(R.string.check_time), null);

        showRoutineVersion();

        if (TextUtils.isEmpty(time)) {
            updateDateTextView.setText(R.string.nvr_checked);
        } else {

            updateDateTextView.setText(String.format("%s\n%s", getString(R.string.last_checked_on), time));
        }

        if (updateDateTextView.getText().equals(getString(R.string.last_checked_on))) {
            updateDateTextView.setText(String.format("%s\nNever", getString(R.string.last_checked_on)));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckProgressBar.setVisibility(View.VISIBLE);
                updateButton.setVisibility(View.GONE);
                checkForVersion();
            }
        });

        ///adding animation
        Animations.setAlphaXAnimation(updateButton);
    }

    private void showRoutineVersion() {
        long version = Utils.getDataVersion(getApplicationContext());
        if (version != 0) {
            String versionText = String.valueOf(version);
            if (versionText.startsWith("1")) {
                versionText = "Spring, " + versionText.substring(versionText.length() - 4);
            } else if (versionText.startsWith("2")) {
                versionText = "Fall, " + versionText.substring(versionText.length() - 4);
            }
            updateVersionTextView.setText(String.format("%s\n%s", getString(R.string.current_data_version), versionText));
        }
    }

    private void checkForVersion() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(versionCheckUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage(), e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getBaseContext(), DialogActivity.class);

                        intent.putExtra(ERROR, getString(R.string.splah_data_fetch_error_msg));

                        startActivity(intent);

                        updateButton.setVisibility(View.VISIBLE);
                        mCheckProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateDateTextView.setText(
                                    String.format("%s\n%s", getString(R.string.last_checked_on),
                                            mSimpleDateFormat.format(new Date())));
                            mSharedPreferences.edit()
                                    .putString(
                                            getString(R.string.check_time),
                                            mSimpleDateFormat.format(new Date())
                                    )
                                    .apply();
                        }
                    });

                    final String result = response.body().string();

                    if (!TextUtils.isEmpty(result)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                version = Long.parseLong(result);
                                if (version == Utils.getDataVersion(getApplicationContext())) {
                                    Dialog dialog =
                                            new AlertDialog.Builder(UpdateActivity.this)
                                                    .setTitle("Nice job!")
                                                    .setCancelable(true)
                                                    .setMessage("You have the latest version of routine!")
                                                    .setPositiveButton("OK", null)
                                                    .create();
                                    dialog.show();

                                    updateButton.setVisibility(View.VISIBLE);
                                    mCheckProgressBar.setVisibility(View.GONE);
                                } else {
                                    updateButton.setVisibility(View.GONE);
                                    mCheckProgressBar.setVisibility(View.GONE);
                                    askToDownloadLatestDataFromServer();
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UpdateActivity.this, "Version check failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getBaseContext(), DialogActivity.class);

                            intent.putExtra(ERROR, getString(R.string.splah_data_fetch_error_msg) + "\nresponse unsuccessful");

                            startActivity(intent);

                            updateButton.setVisibility(View.VISIBLE);
                            mCheckProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mDownloadProgress != null && mDownloadProgress.getVisibility() == View.VISIBLE) {
            mDownloadProgress.setVisibility(View.GONE);
            mUpdateDownloadProgressText.setVisibility(View.GONE);
        }

        if (bManager != null) {
            bManager.unregisterReceiver(broadcastReceiver);
        }
    }

    private void askToDownloadLatestDataFromServer() {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        if (mSharedPreferences.getBoolean(getString(R.string.pref_show_download_alert), true)) {
            final CharSequence[] array = {"Don't show again"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("Looks like you don't have the latest routine!Would you like to download the latest routine to get up-to-date?")
                    //.setMessage("Would you like to download the latest data to get up-to-date?")
                    .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ListView lv = ((AlertDialog) dialog).getListView();
                            String selected = (String) lv.getTag();
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            if (selected != null && selected.contains("Don't show again")) {
                                editor.putBoolean(getString(R.string.pref_show_download_alert), false);
                            } else {
                                editor.putBoolean(getString(R.string.pref_show_download_alert), true);
                            }

                            editor.apply();

                            downloadLatestData();
                        }
                    })
                    .setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ListView lv = ((AlertDialog) dialog).getListView();
                            lv.setTag("don't show");
                            Toast.makeText(UpdateActivity.this, "don't show", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null);

            mDialog = builder.create();

            mDialog.show();
        } else {
            downloadLatestData();
        }
    }

    private void downloadLatestData() {

        if (NetworkConnectionManager.hasNetworkConnection(getBaseContext())) {
            startDownload();
        }

    }

    private void startDownload() {

        Intent intent = new Intent(this, DataDownloadService.class);
        startService(intent);

        if (mDownloadProgress != null && mDownloadProgress.getVisibility() == View.GONE) {

            mDownloadProgress.setVisibility(View.VISIBLE);
            mUpdateDownloadProgressText.setVisibility(View.VISIBLE);
            mUpdateDownloadProgressText.setText(R.string.downloading);
        }

    }

    private void registerReceiver() {

        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

        // setting up the progress dialog to show the user download update
        mDownloadProgress.setMax(100);
        mDownloadProgress.setProgress(0);
        mDownloadProgress.setIndeterminate(false);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null &&
                    intent.getAction().equals(MESSAGE_PROGRESS)) {

                mDownloadProgress.setVisibility(View.VISIBLE);
                mUpdateDownloadProgressText.setVisibility(View.VISIBLE);
                mCheckProgressBar.setVisibility(View.GONE);

                DownloadModelClass download = intent.getParcelableExtra("download");

                mDownloadProgress.setProgress(download.getProgress());
                if (download.getTotalFileSize() > 0) {
                    mUpdateDownloadProgressText.setText(String.format(Locale.getDefault(),
                            "Downloaded (%.2f/%d) MB", download.getCurrentFileSize(), download.getTotalFileSize()));

                } else {
                    mUpdateDownloadProgressText.setText(String.format(Locale.getDefault(),
                            "Downloaded %.2f MB", download.getCurrentFileSize()));

                }
                if (mDownloadProgress != null && mDownloadProgress.getVisibility() == View.VISIBLE && download.getProgress() == 100) {

                    if (version != 0) {
                        mCheckProgressBar.setVisibility(View.GONE);
                        mDownloadProgress.setVisibility(View.GONE);
                        mUpdateDownloadProgressText.setText(R.string.download_complete);
                        Utils.setDataVersion(getApplicationContext(), version);
                        showRoutineVersion();
                        mUpdateDownloadProgressText.setText(R.string.download_complete);
                        Toast.makeText(context, "Routine updated", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Toast.makeText(context, "Try again later", Toast.LENGTH_SHORT).show();

                    }

                } else if (mDownloadProgress != null && mDownloadProgress.getVisibility() == View.VISIBLE && download.getProgress() == -1) {

                    mUpdateDownloadProgressText.setText(R.string.download_failed);

                    mDownloadProgress.setVisibility(View.GONE);
                    mUpdateDownloadProgressText.setVisibility(View.GONE);

                    // notifying user about download failing

                    Dialog dialog;

                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                    builder.setTitle("Download Failed");
                    builder.setCancelable(true);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.setMessage("Sorry downloading failed. Try again later!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });

                    dialog = builder.create();
                    dialog.show();
                }
            }
        }
    };
}
