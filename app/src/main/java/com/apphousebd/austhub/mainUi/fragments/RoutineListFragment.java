/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:42 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.mainUi.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.backgroundTasks.DataDownloadService;
import com.apphousebd.austhub.dataBase.RoutineDatabase;
import com.apphousebd.austhub.dataModel.DownloadModelClass;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.utilities.NetworkConnectionManager;
import com.apphousebd.austhub.utilities.Utils;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants.TABLE_NAME;
import static com.apphousebd.austhub.utilities.Constants.MESSAGE_PROGRESS;

public class RoutineListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String SECTION = "section";
    private static final String TAG = "RoutineListFragment";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_DAY = "EXTRA_DAY";


    private TextView mClockText;
    private String userSection;
    private int userSemester, userYear;
    private View view;
    private PhotoView routineImg;

    // progress dialog for showing the download progress
    private ProgressDialog mDownloadProgress;
    private Dialog mDialog;


    private UserModel mUserModel;
    private List<String> mSectionList;
    private RoutineDatabase database;
    private LocalBroadcastManager bManager;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null && intent.getAction().equals(MESSAGE_PROGRESS)) {

                DownloadModelClass download = intent.getParcelableExtra("download");

                mDownloadProgress.show();
                mDownloadProgress.setProgress(download.getProgress());
                mDownloadProgress.setTitle(String.format(Locale.getDefault(),
                        "Downloaded (%.2f) MB", download.getCurrentFileSize()));

                if (mDownloadProgress != null && mDownloadProgress.isShowing() && download.getProgress() == 100) {

                    mDownloadProgress.setTitle("Download Completed!");

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            mDownloadProgress.dismiss();

                            gettDetails();
                            setSpinners();
                            loadRoutineImage();

                            Toast.makeText(getContext(), "Routine updated", Toast.LENGTH_SHORT).show();
                        }
                    }, 500);
                } else if (mDownloadProgress != null && mDownloadProgress.isShowing() && download.getProgress() == -1) {

                    mDownloadProgress.setTitle("Download Failed, Try Later!");

                    mDownloadProgress.dismiss();

                    // notifying user about download failing

                    Dialog dialog;

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    public RoutineListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_routine_list, container, false);

        mUserModel = Utils.getUserModel(getContext().getApplicationContext());
        database = new RoutineDatabase(getContext());

        if (mUserModel == null)
            getActivity().finish();
        else {

            //getting details
            gettDetails();

            mClockText = view.findViewById(R.id.activity_routine_clock_day);

            routineImg = view.findViewById(R.id.routine_img);

            Button routineShowButton = view.findViewById(R.id.routine_show_btn);
            routineShowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadRoutineImage();
                }
            });

        }
        return view;
    }

    private void gettDetails() {
        mSectionList = Arrays.asList(getContext().getResources().getStringArray(R.array.std_section_value));
        userYear = Integer.parseInt(mUserModel.getYear());
        userSection = mUserModel.getSection();
        userSemester = Integer.parseInt(mUserModel.getSemester());
    }

    private void setSpinners() {
        Spinner yearSpinner = view.findViewById(R.id.routine_year_spinner);
        yearSpinner.setSelection(userYear - 1);

        Spinner semesterSpinner = view.findViewById(R.id.routine_semester_spinner);
        semesterSpinner.setSelection(userSemester - 1);

        Spinner sectionSpinner = view.findViewById(R.id.routine_section_spinner);
        sectionSpinner.setSelection(mSectionList.indexOf(userSection));

        yearSpinner.setOnItemSelectedListener(this);
        semesterSpinner.setOnItemSelectedListener(this);
        sectionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        setTimeInRoutine();

        mUserModel = Utils.getUserModel(getContext().getApplicationContext());
        if (mUserModel != null) {
            if (database.getTableItemCount(TABLE_NAME + "_" + mUserModel.getDept()) > 0) {

                setSpinners();

                loadRoutineImage();
            } else {
                registerReceiver();
                Toast.makeText(getContext(), "Sorry no data found", Toast.LENGTH_SHORT).show();
                gettDetails();
                setSpinners();
                askToDownloadLatestDataFromServer();
            }
        }
//        //adding animation
//        Animations.setAlphaXAnimation(semesterShowingText);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (bManager != null) {
            bManager.unregisterReceiver(broadcastReceiver);
        }
    }

    private void loadRoutineImage() {
        RoutineDatabase database = new RoutineDatabase(getContext());
        String decodedImage = database.getRoutine(
                userYear,
                userSemester,
                "section_" + userSection,
                mUserModel.getDept()
        );

        Log.d(TAG, "loadRoutineImage: year: " + userYear);
        Log.d(TAG, "loadRoutineImage: semester: " + userSemester);
        Log.d(TAG, "loadRoutineImage: section: " + userSection);
        if (!TextUtils.isEmpty(decodedImage)) {
            byte[] decodedString = Base64.decode(decodedImage, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            routineImg.setImageBitmap(decodedBitmap);
        } else {
            routineImg.setImageResource(R.drawable.no_routine);
        }
    }

    public void setTimeInRoutine() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.US);
        String date = simpleDateFormat.format(new Date());
        if (getResources().getConfiguration().orientation != ORIENTATION_LANDSCAPE) {
            mClockText.setTextSize(22);
        } else {
            mClockText.setTextSize(16);
        }

        //adding font
        Typeface typeface = Typeface.createFromAsset(
                getContext().getAssets(), "fonts/Kalam-Bold.ttf");
        mClockText.setTypeface(typeface);
        mClockText.setText(date);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();
        Log.d(TAG, "onItemSelected: id: " + id);
        switch (id) {
            case R.id.routine_year_spinner:
                userYear = i + 1;
                Log.d(TAG, "onItemSelected: year selected: " + i);
                break;
            case R.id.routine_semester_spinner:
                userSemester = i + 1;
                break;
            case R.id.routine_section_spinner:
                userSection = mSectionList.get(i);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void askToDownloadLatestDataFromServer() {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setMessage("Looks like you don't have the latest routine of your department!\n" +
                        "Would you like to download the latest routine to get up-to-date?")
                //.setMessage("Would you like to download the latest data to get up-to-date?")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        downloadLatestData();
                    }
                })
                .setNegativeButton("Cancel", null);

        mDialog = builder.create();

        mDialog.show();
    }

    private void downloadLatestData() {

        if (NetworkConnectionManager.hasNetworkConnection(getContext())) {
            startDownload();
        }

    }

    private void startDownload() {

        Intent intent = new Intent(getContext(), DataDownloadService.class);
        getActivity().startService(intent);

    }

    private void registerReceiver() {

        bManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

        // setting up the progress dialog to show the user download update
        mDownloadProgress = new ProgressDialog(getActivity());
        mDownloadProgress.setMax(100);
        mDownloadProgress.setIndeterminate(true);
        mDownloadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadProgress.setTitle("Downloading...");
        mDownloadProgress.setCancelable(false);

    }

}
