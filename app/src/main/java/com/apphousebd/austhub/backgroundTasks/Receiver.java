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

package com.apphousebd.austhub.backgroundTasks;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.apphousebd.austhub.dataBase.ReminderDatabase;
import com.apphousebd.austhub.dataModel.reminderDataModel.ReminderItemModel;

/**
 * Created by Shaafi on 2/21/2017.
 * email: a15shaafi.209@gmail.com
 */

public class Receiver extends WakefulBroadcastReceiver {

    public static String REMINDER_ID = "reminder_id";


    @Override
    public void onReceive(Context context, Intent intent) {

        //getting the data from database
        int id = intent.getIntExtra(REMINDER_ID, -1);

//        Toast.makeText(context, "id received! : " + id, Toast.LENGTH_SHORT).show();

        //wake up screen
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isInteractive();
        } else {
            isScreenOn = pm.isScreenOn();
        }
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

            wl_cpu.acquire(10000);
        }

        ReminderDatabase reminderDatabase = new ReminderDatabase(context);


        Log.d("receiver", "onReceive: id received! : " + id);
        Toast.makeText(context, "id received! : " + id, Toast.LENGTH_SHORT).show();

        ReminderItemModel model = reminderDatabase.getReminderDataById((int) id);

        if (model != null) {

            Log.d("receiver", "onReceive: item! : " + model.getSubject());
            NewMessageNotification.notify(
                    context,
                    model.getSubject(),
                    model.getDetails(),
                    id
            );

        }
//        else {
//            NewMessageNotification.notify(context, "hello", "its notification", id);
//        }

    }
}
