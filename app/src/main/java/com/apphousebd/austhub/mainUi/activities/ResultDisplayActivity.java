package com.apphousebd.austhub.mainUi.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.utilities.ManageSavedFiles;
import com.apphousebd.austhub.utilities.MyTextWatcher;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_SEMESTER;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_YEAR;
import static com.apphousebd.austhub.utilities.ManageSavedFiles.SAVING_FOLDER_NAME;

public class ResultDisplayActivity extends AppCompatActivity {

    public static final String IS_SAVED = "is_file_save";
    public static final String RESULT_STRING = "result";
    private static final int REQUEST_CODE = 1010;
    //date of the result being seen or saved
    SimpleDateFormat mSimpleDateFormat;
    private String mResultString;
    private FloatingActionButton fab;
    private SharedPreferences preferences;
    ///file name
    private String fileName;
    ///extension for file
    private String fileEx;
    ///dialogs
    private AlertDialog.Builder mBuilder;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView resultShowingTextView = (TextView) findViewById(R.id.result_text_view);
        fab = (FloatingActionButton) findViewById(R.id.result_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptAndSaveFile();
            }
        });

        mResultString = getIntent().getStringExtra(RESULT_STRING);

        if (TextUtils.isEmpty(mResultString)) {
            ///if result string is empty that means something is wrong! so show the user the
            ///error msg

            mResultString = "Sorry something went wrong! No result found to display!";
        }

        boolean isFileSaved = getIntent().getBooleanExtra(IS_SAVED, false);

        /***********************************************************************************
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

    private void promptAndSaveFile() {

        ///checking if the user has chosen any default file extension for saving

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        fileEx = preferences.getString(getString(R.string.file_save_mode), null);

        if (fileEx == null) {
            promptUserToSelectDefaultSavingOption();
        } else {
            //checking for permissions
            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            );

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            } else {
                promptUserToGiveFileName();
            }
        }
    }

    private void promptUserToGiveFileName() {
        ///getting the reference of the view that will be shown to use to get the name input
        View savingView = LayoutInflater.from(this)
                .inflate(R.layout.saving_file_name_dialog, null);

        ///getting the refs of the edit texts containing the id/name
        final EditText id1 = (EditText) savingView.findViewById(R.id.file_name_edit_text_1);
        final EditText id2 = (EditText) savingView.findViewById(R.id.file_name_edit_text_2);
        final EditText id3 = (EditText) savingView.findViewById(R.id.file_name_edit_text_3);
        final EditText id4 = (EditText) savingView.findViewById(R.id.file_name_edit_text_4);


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
                                STD_YEAR + "_" + STD_SEMESTER + ")" + fileEx;

                        if (fileName.length() == 21) ///as 15_00_00_000(2_2).txt == 21 letters
                        {
                            if (ManageSavedFiles.hasNoDuplicates(fileName)) {
                                saveFile();
                            } else {

                                warnUserAboutDuplicate();
                            }
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
                .setMessage("A file with same name is saved on your device!")
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

        if (ManageSavedFiles.saveResultFile(fileName, mResultString)) {

            ///hide the save icon
            fab.setVisibility(View.GONE);

            ///show the user the file name and location
            Toast.makeText(ResultDisplayActivity.this, "Result Saved as \n" +
                    fileName + "\n in Folder: \n" + SAVING_FOLDER_NAME, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ResultDisplayActivity.this, "Result Could Not be Saved.\n" +
                            "Please Check Your External Storage is OK and Try Again!"
                    , Toast.LENGTH_LONG).show();
        }
    }

    /***********************************************************************************
     * if the default saving option is not set then prompt the user for the 1st time
     * to set the option and use it next time.. also tell the user where he/she can
     * change it later
     ************************************************************************************/
    private void promptUserToSelectDefaultSavingOption() {

        final List<String> saveOptionValue =
                Arrays.asList(getResources().getStringArray(R.array.save_mode_values));
        final List<String> saveOptions =
                Arrays.asList(getResources().getStringArray(R.array.save_modes));
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice,
                        saveOptions);

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
        }

        mBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        mBuilder.setTitle("Save As:")
                .setIcon(android.R.drawable.ic_menu_save)
                .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fileEx = saveOptionValue.get(which);
//                        Toast.makeText(ResultDisplayActivity.this, "selected: " + fileEx, Toast.LENGTH_SHORT).show();

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (fileEx == null)
                            fileEx = saveOptionValue.get(0);

                        //setting the save mode in pref
                        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.settings, true);
                        preferences.edit()
                                .putString(getString(R.string.file_save_mode), fileEx)
                                .apply();

                        ///telling the user where he/she can change the setting
                        Toast.makeText(ResultDisplayActivity.this, "You can change the default option from\n" +
                                "Settings>>Change Saving File Type ", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(ResultDisplayActivity.this, "selected: " + fileEx, Toast.LENGTH_SHORT).show();

                    }
                });

        mDialog = mBuilder.create();
        mDialog.show();
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
