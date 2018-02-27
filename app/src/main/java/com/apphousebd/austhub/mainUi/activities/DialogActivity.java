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

package com.apphousebd.austhub.mainUi.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;

public class DialogActivity extends AppCompatActivity {

    public static final String ERROR = "error";
    public static final String INTERNET_ON = "internet";
    boolean goToSettings = false;
    boolean inform = false;
    private Button informBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        String errorMsg = getIntent().getStringExtra(ERROR);
        goToSettings = getIntent().getBooleanExtra(INTERNET_ON, false);
        inform = getIntent().getBooleanExtra("inform", false);

        TextView t = findViewById(R.id.error_dialog_text);
        if (!TextUtils.isEmpty(errorMsg)) t.setText(errorMsg);

        informBtn = findViewById(R.id.dialog_inform_btn);
        informBtn.setVisibility(inform ? View.VISIBLE : View.GONE);

    }

    public void finishActivity(View view) {

        if (goToSettings) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }

        finish();
    }

    public void sendErrorReport(View view) {
        String body = null;
        try {
            body = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            body = "\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER +
                    "\n-----------------------------\n**Error Report Starts Here**\n\n";
        } catch (PackageManager.NameNotFoundException e) {
            body = e.getLocalizedMessage();
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:apphousebd@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Error report from AustHub app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.choose_email_client)));
        } else {
            Toast.makeText(this, "No client was found to send error report", Toast.LENGTH_SHORT).show();
        }

        finish();

    }
}
