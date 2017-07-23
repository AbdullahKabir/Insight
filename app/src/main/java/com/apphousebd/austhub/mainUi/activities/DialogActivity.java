package com.apphousebd.austhub.mainUi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.apphousebd.austhub.R;

public class DialogActivity extends AppCompatActivity {

    public static final String ERROR = "error";
    public static final String INTERNET_ON = "internet";

    boolean goToSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        String errorMsg = getIntent().getStringExtra(ERROR);
        goToSettings = getIntent().getBooleanExtra(INTERNET_ON, false);

        TextView t = (TextView) findViewById(R.id.error_dialog_text);
        if (!TextUtils.isEmpty(errorMsg)) t.setText(errorMsg);
    }

    public void finishActivity(View view) {

        if (goToSettings) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }

        finish();
    }
}
