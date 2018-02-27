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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.mainUi.AppCompatPreferenceActivity;
import com.apphousebd.austhub.utilities.Constants;
import com.apphousebd.austhub.utilities.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String TAG = "SettingsActivity";

    public static String appVersion(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //adding the settings fragment
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container_settings, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //settings fragment class
    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings);

//            show the version
            Preference versionPref = findPreference(getString(R.string.title_version));
            try {
                if (versionPref != null)
                    versionPref.setSummary(appVersion(getActivity().getApplicationContext()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

//            listen to the user details change and update them in database
            Preference deptPref = findPreference(getString(R.string.std_dept));
            Preference yearPref = findPreference(getString(R.string.std_year));
            Preference semPref = findPreference(getString(R.string.std_sem));
            Preference secPref = findPreference(getString(R.string.std_sec));

            deptPref.setOnPreferenceChangeListener(this);
            yearPref.setOnPreferenceChangeListener(this);
            semPref.setOnPreferenceChangeListener(this);
            secPref.setOnPreferenceChangeListener(this);

            // feedback preference click listener
            Preference myPref = findPreference(getString(R.string.key_send_feedback));
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return true;
                }
            });

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

            UserModel model = Utils.getUserModel(getActivity().getApplicationContext());

            if (model != null) {
                Log.d(TAG, "onPreferenceChange: name: " + model.getName());

                if (preference.getKey().equals(getString(R.string.std_dept))) {
                    Log.d(TAG, "onPreferenceChange: " + o);

                    FirebaseMessaging.getInstance().unsubscribeFromTopic(model.getDept());

                    model.setDept((String) o);

                    FirebaseMessaging.getInstance().subscribeToTopic(model.getDept());

                } else if (preference.getKey().equals(getString(R.string.std_year))) {
                    model.setYear((String) o);
                } else if (preference.getKey().equals(getString(R.string.std_sem))) {
                    model.setSemester((String) o);
                } else if (preference.getKey().equals(getString(R.string.std_sec))) {
                    model.setSection((String) o);
                }

                Utils.saveUser(getActivity().getApplicationContext(), model);
                Map<String, Object> postValues = model.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/" + model.getuId() + "/", postValues);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_USER_DATABASE);
                reference.updateChildren(childUpdates);

                return true;

            } else
                return false;
        }

        /**
         * Email client intent to send support mail
         * Appends the necessary device information to email body
         * useful when providing support
         */
        public void sendFeedback(Context context) {
            String body;
            try {
                body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                body = "\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                        Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                        "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER +
                        "\n-----------------------------\n**Feedback Starts Here**\n\n";
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "sendFeedback: " + e.getLocalizedMessage(), e);
                body = e.getLocalizedMessage();
            }
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("message/rfc822");
            intent.setData(Uri.parse("mailto:apphousebd@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from AustHub app");
            intent.putExtra(Intent.EXTRA_TEXT, body);
            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
            } else {
                Toast.makeText(context, "No client was found to send feedback", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
