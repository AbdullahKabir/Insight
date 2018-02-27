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

package com.apphousebd.austhub.backgroundTasks;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.apphousebd.austhub.NetworkSingleton;
import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.DownloadModelClass;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.mainUi.activities.MainActivity;
import com.apphousebd.austhub.utilities.AustHubNotification;
import com.apphousebd.austhub.utilities.Constants;
import com.apphousebd.austhub.utilities.JsonHelper;
import com.apphousebd.austhub.utilities.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class DataDownloadService extends IntentService {

    public static final int DOWNLOAD_NOTIFICATION_ID = 1620;
    private static final String TAG = "DataDownloadService";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize = 0;

    public DataDownloadService() {
        super("DataDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            boolean cancel = intent.getBooleanExtra("cancel", false);
            if (cancel) {
                stopSelf();
            }

            intent.putExtra("cancel", true);

            PendingIntent pendingIntent =
                    PendingIntent.getService(getBaseContext(), 11, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AustHubNotification.createNotificationCannel(getBaseContext());
            }

            notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentTitle("AustHub Routine Download")
                    .setContentText("Downloading Routine")
                    .addAction(
                            android.R.drawable.ic_menu_close_clear_cancel,
                            "Cancel",
                            pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());

            initDownload();

        }
    }

    private void initDownload() {

        UserModel model = Utils.getUserModel(getApplicationContext());
        if (model == null)
            return;

        OkHttpClient client = NetworkSingleton.getClient();

        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("dept", model.getDept())
                .build();

        Request request = new Request.Builder()
//                .url("http://www.androidtesting.tk/uploaded_files/img_0044.jpg")
//                .url("http://10.0.2.2/badgemate/download_data_as_json.php")
                .url(Constants.UPDATE_ROUTINE_DOWNLOAD_URL)
                .post(formBody)
                .build();

        try {
            Call call = client.newCall(request);

            Response response = call.execute();
            Log.d(TAG, "initDownload: response :" + response);

            if (!response.isSuccessful()) {

                onDownloadFailed();
            } else {
                downloadFile(response.body());
            }

        } catch (IOException e) {

            Log.e(TAG, "initDownload: " + e.getLocalizedMessage());
            e.printStackTrace();

            onDownloadFailed();
        }
    }

    @Override
    public void onDestroy() {
        onDownloadFailed();
        Log.d(TAG, "onDestroy: download service destroyed");
        super.onDestroy();
    }

    private void downloadFile(ResponseBody body) throws IOException {

        if (body == null) {
            onDownloadFailed();
            return;
        }

        int count;
        byte data[] = new byte[1024 * 4]; // to hold the line data of each line in the input/output stream
        long fileSize = body.contentLength();
        Log.d(TAG, "downloadFile: file size: " + fileSize);
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 4);
//        File outputFile = new File(Environment.getExternalStoragePublicDirectory("Android/data"),
//                "file.txt");

        // creating a file where the downloaded data will be stored
        File outputFile = new File(getFilesDir() + "ProductJson.txt");
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;


        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) Math.ceil(fileSize / (Math.pow(1024, 2)));
            double current = Math.floor(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            DownloadModelClass download = new DownloadModelClass();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize(current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            Log.d(TAG, "downloadFile: data is being written");

            output.write(data, 0, count);
        }


        readFromFile();
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();


    }

    private void readFromFile() {

        String text = null;
        FileInputStream inputStream1 = null;

        Log.d(TAG, "readFromFile: starting reading");
        try {

            // getting the file where the data is stored and then sending it to the json helper to
            // decode it and save it into database

            File outputFile = new File(getFilesDir() + "ProductJson.txt");

            inputStream1 = new FileInputStream(outputFile);

            int length = (int) outputFile.length();

            byte[] bytes = new byte[length];
            inputStream1.read(bytes);

            text = new String(bytes);

        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } finally {
            if (inputStream1 != null) {
                try {
                    inputStream1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!TextUtils.isEmpty(text) && !text.contains("failed")) {
            if (text.contains(">")) {
                String[] texts = text.split(">");
                JsonHelper.decodeJsonData(getBaseContext(), texts[texts.length - 1]);
            } else {
                JsonHelper.decodeJsonData(getBaseContext(), text);
            }

            onDownloadComplete();
        } else if (!TextUtils.isEmpty(text) && text.contains("failed")) {
            onDownloadFailed();
        }
    }

    private void sendNotification(DownloadModelClass download) {

        sendIntent(download);
        String msg;
        if (totalFileSize <= 0) {
            notificationBuilder.setProgress(0, 0, true);
            msg = "Downloading Routine " + download.getCurrentFileSize() + " MB";
        } else {
            notificationBuilder.setProgress(100, download.getProgress(), false);
            msg = "Downloading Routine " + download.getCurrentFileSize() + "/" + totalFileSize + " MB";
        }
        notificationBuilder.setContentText(msg);
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());
    }

    private void sendIntent(DownloadModelClass download) {

        Intent intent = new Intent(Constants.MESSAGE_PROGRESS);
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DataDownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete() {

        DownloadModelClass download = new DownloadModelClass();
        download.setProgress(100);
        sendIntent(download);

//        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
//        intent.setDataAndType(selectedUri, "resource/folder");
        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(getBaseContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);
        }
        notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        notificationBuilder.setContentText("Routine Downloaded");
        notificationBuilder.setAutoCancel(true);
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());

    }

    public void onDownloadFailed() {

        DownloadModelClass download = new DownloadModelClass();
        download.setProgress(-1);
        download.setTotalFileSize(0);
        sendIntent(download);

//        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
//        intent.setDataAndType(selectedUri, "resource/folder");
        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(getBaseContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);
        }
        notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        notificationBuilder.setContentText("Download Failed!");
        notificationBuilder.setAutoCancel(true);
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build());

    }
}
