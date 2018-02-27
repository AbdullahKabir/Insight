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

package com.apphousebd.austhub.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.UserModel;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Asif Imtiaz Shaafi on 10, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class Utils {


    public static void saveUser(Context context, UserModel model) {

        if (model == null) {
            Toast.makeText(context, "Please restart the app!", Toast.LENGTH_LONG).show();
            return;
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()

                .putString(context.getString(R.string.pref_user_name), model.getName())
                .putString(context.getString(R.string.pref_user_email), model.getEmail())
                .putString(context.getString(R.string.pref_user_uid), model.getuId())
                .putString(context.getString(R.string.pref_user_img), model.getImgLink())
                .putString(context.getString(R.string.pref_user_phone), model.getPhone())
                .putString(context.getString(R.string.std_year), model.getYear())
                .putString(context.getString(R.string.std_sem), model.getSemester())
                .putString(context.getString(R.string.std_dept), model.getDept())
                .putString(context.getString(R.string.std_sec), model.getSection())

                .apply();

//        adding the user to the dept topic
        FirebaseMessaging.getInstance().subscribeToTopic(model.getDept());
    }

    public static UserModel getUserModel(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        if (TextUtils.isEmpty(sharedPreferences.getString(context.getString(R.string.pref_user_name), ""))) {
            return null;
        }

        return new UserModel(
                sharedPreferences.getString(context.getString(R.string.pref_user_name), ""),
                sharedPreferences.getString(context.getString(R.string.pref_user_email), ""),
                sharedPreferences.getString(context.getString(R.string.pref_user_phone), ""),
                sharedPreferences.getString(context.getString(R.string.pref_user_uid), ""),
                sharedPreferences.getString(context.getString(R.string.pref_user_img), ""),
                sharedPreferences.getString(context.getString(R.string.std_dept), ""),
                sharedPreferences.getString(context.getString(R.string.std_sem), ""),
                sharedPreferences.getString(context.getString(R.string.std_year), ""),
                sharedPreferences.getString(context.getString(R.string.std_sec), "")
        );
    }

    public static void clearUser(Context context) {
        UserModel model = getUserModel(context);
        if (model != null) {
//        unsubscribing from topic
            FirebaseMessaging.getInstance().unsubscribeFromTopic(model.getDept());
        }

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit()
                .remove(context.getString(R.string.pref_user_name))
                .remove(context.getString(R.string.pref_user_email))
                .remove(context.getString(R.string.pref_user_phone))
                .remove(context.getString(R.string.pref_user_uid))
                .remove(context.getString(R.string.pref_user_img))
                .remove(context.getString(R.string.std_dept))
                .remove(context.getString(R.string.std_sem))
                .remove(context.getString(R.string.std_year))
                .remove(context.getString(R.string.std_sec))
                .apply();

    }

    public static long getDataVersion(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(context.getString(R.string.pref_data_version), 0);
    }

    public static void setDataVersion(Context context, long version) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(context.getString(R.string.pref_data_version), version)
                .apply();
    }
}
