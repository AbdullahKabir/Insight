/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:41 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.mainUi.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.utilities.Constants;
import com.apphousebd.austhub.utilities.ManageSavedFiles;
import com.apphousebd.austhub.utilities.MyTextWatcher;
import com.apphousebd.austhub.utilities.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultDisplayActivity extends AppCompatActivity {

    public static final String IS_SAVED = "is_file_save";
    public static final String RESULT_STRING = "result";
    private static final int REQUEST_CODE = 1010;
    //date of the result being seen or saved
    SimpleDateFormat mSimpleDateFormat;
    private String mResultString;
    private FloatingActionButton fab;
    ///file name
    private String fileName;
    ///dialogs
    private AlertDialog.Builder mBuilder;
    private Dialog mDialog;

    private UserModel mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView resultShowingTextView = findViewById(R.id.result_text_view);
        fab = findViewById(R.id.result_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptUserToGiveFileName();
            }
        });

        mResultString = getIntent().getStringExtra(RESULT_STRING);

        if (TextUtils.isEmpty(mResultString)) {
            ///if result string is empty that means something is wrong! so show the user the
            ///error msg

            mResultString = "Sorry something went wrong! No result found to display!";
        }

        boolean isFileSaved = getIntent().getBooleanExtra(IS_SAVED, false);

        /* **********************************************************************************
         if the file is not saved that is the result is not saved then show the fab icon
         so that user has the option to save the file,
         otherwise hide the icon as user don't need to save the file
         ************************************************************************************/

        if (isFileSaved) {
            fab.setVisibility(View.GONE);
        } else {

            fab.setVisibility(View.VISIBLE);
        }

        resultShowingTextView.setText(mResultString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserModel = Utils.getUserModel(getApplicationContext());
        if (mUserModel == null)
            finish();
    }


    private void promptUserToGiveFileName() {
        ///getting the reference of the view that will be shown to use to get the name input
        View savingView = LayoutInflater.from(this)
                .inflate(R.layout.saving_file_name_dialog, null);

        ///getting the refs of the edit texts containing the id/name
        final EditText id1 = savingView.findViewById(R.id.file_name_edit_text_1);
        final EditText id2 = savingView.findViewById(R.id.file_name_edit_text_2);
        final EditText id3 = savingView.findViewById(R.id.file_name_edit_text_3);
        final EditText id4 = savingView.findViewById(R.id.file_name_edit_text_4);


        ///adding the text listener that will watch the text changing and take the cursor to next
        ///text filed when uer gives the input

        /* MyTextWatcher takes 3 parameters,
        *  the 1st is the current view/editText, which will be watched,
        *   the 2nd one is the view/editText where the cursor will go after current editText has te required input
        *    the 3rd is the size of the input which is expected in the current editText
        */

        id1.addTextChangedListener(new MyTextWatcher(id1, id2, 2));
        id2.addTextChangedListener(new MyTextWatcher(id2, id3, 2));
        id3.addTextChangedListener(new MyTextWatcher(id3, id4, 2)); //the fourth one is not needed as there is no next editText

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
        }

        mBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        mBuilder.setTitle("Enter Your ID:")
                .setView(savingView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fileName = id1.getText().toString() + "_" +
                                id2.getText().toString() + "_" +
                                id3.getText().toString() + "_" +
                                id4.getText().toString() + "(" +
                                ///adding year and semester
                                mUserModel.getYear() + "_" + mUserModel.getSemester()
                                + ")";

                        if (fileName.length() == 17) ///as 15_00_00_000(2_2) == 17 letters
                        {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FILE_DATABASE);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(mUserModel.getuId()).child(fileName).exists()) {
                                        warnUserAboutDuplicate();
                                    } else {
                                        saveFile();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ResultDisplayActivity.this, "Result saving Cancelled!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(ResultDisplayActivity.this, "Sorry! Invalid ID!\n" +
                                    "Please Enter a valid ID", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false);

        mDialog = mBuilder.create();
        mDialog.show();
    }

    /***********************************************************************************
     * show a popup that user already has a file saved with same name. so give the user the option to
     * 1. Override: so that the file is saved overriding the previous one
     * 2. Cancel: cancel the saving process
     ************************************************************************************/
    private void warnUserAboutDuplicate() {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
            mDialog = null;
        }

        mBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        mBuilder.setTitle("Warning!")
                .setMessage("A file with same name is already saved!")
                .setPositiveButton("Override", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveFile();
                    }
                })
                .setNegativeButton("Cancel", null);

        mDialog = mBuilder.create();
        mDialog.show();
    }

    private void saveFile() {
        ///saving date
        mSimpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());

        Date date = new Date();

        String fileDate = mSimpleDateFormat.format(date);

        //adding the date on the top of the result
        mResultString = "Result saved on:\n" +
                fileDate + "\n\n\n" + mResultString;

        if (ManageSavedFiles.saveResultFileInFirebase(mUserModel.getuId(), fileName, mResultString)) {

            ///hide the save icon
            fab.setVisibility(View.GONE);

            ///show the user the file name and location
            Snackbar.make(
                    findViewById(R.id.result_display_coordinator_layout),
                    "Result Saved as " + fileName,
                    Snackbar.LENGTH_LONG
            ).setAction("Show", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.CURRENT_INDEX = 4;
                    finish();
                }
            }).show();
        } else {
            Toast.makeText(ResultDisplayActivity.this, "Result Could Not be Saved.\n" +
                            "Please Check Your External Storage is OK and Try Again!"
                    , Toast.LENGTH_LONG).show();
        }
    }

    ///file permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    promptUserToGiveFileName();
                } else {
                    Toast.makeText(this, "Please allow the permission to use this service!", Toast.LENGTH_LONG).show();
                    //finish();
                }
        }

    }

}
