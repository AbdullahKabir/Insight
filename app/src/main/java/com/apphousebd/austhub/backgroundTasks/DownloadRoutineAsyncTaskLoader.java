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

package com.apphousebd.austhub.backgroundTasks;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.apphousebd.austhub.utilities.NetworkConnectionManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Response;

import static com.apphousebd.austhub.utilities.NetworkConnectionManager.FAILED;

/**
 * Created by Asif Imtiaz Shaafi on 11, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class DownloadRoutineAsyncTaskLoader extends AsyncTaskLoader<String> {

    private static final String TAG = "DownloadRoutineAsync";

    private String dept = "";

    public DownloadRoutineAsyncTaskLoader(Context context) {
        super(context);
    }

    public DownloadRoutineAsyncTaskLoader(Context context, String dept) {
        super(context);
        this.dept = dept;
    }

    @Override
    public String loadInBackground() {

        Response response = NetworkConnectionManager.getRoutineJson(getContext(), dept);

        Log.d(TAG, "loadInBackground: getting response");

        InputStream inputStream = null;

        String fileString = FAILED;
        try {
            if (response != null && response.isSuccessful()) {

                Log.d(TAG, "loadInBackground: response successful");

                int count;
                byte data[] = new byte[1024 * 32]; // to hold the line data of each line in the input/output stream

                inputStream = new BufferedInputStream(response.body().byteStream(), 1024 * 32);
//        File outputFile = new File(Environment.getExternalStoragePublicDirectory("Android/data"),
//                "file.txt");

                // creating a file where the downloaded data will be stored
                File outputFile = new File(getContext().getFilesDir(), "Json.txt");
                OutputStream output = new FileOutputStream(outputFile);
                Log.d(TAG, "loadInBackground: final text: " + fileString);

                while ((count = inputStream.read(data)) != -1) {

                    Log.d(TAG, "loadInBackground: reading data");

                    if (isLoadInBackgroundCanceled()) {
                        fileString = FAILED;
                        Log.d(TAG, "loadInBackground: loading canceled");
                        return fileString;
                    }

                    output.write(data, 0, count);
                }

                fileString = readFromFile();

                Log.d(TAG, "loadInBackground: final text: " + fileString);
            } else {
                Log.d(TAG, "loadInBackground: response unsuccessful");
                fileString = FAILED;
            }
        } catch (IOException e) {
            fileString = FAILED;
            e.printStackTrace();
            Log.e(TAG, "loadInBackground: " + e.getLocalizedMessage(), e);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(TAG, "loadInBackground: " + fileString);
        return fileString;
    }


    private String readFromFile() {

        String text = FAILED;
        FileInputStream inputStream1 = null;

        try {

            // getting the file where the data is stored and then sending it to the json helper to
            // decode it and save it into database

            File outputFile = new File(getContext().getFilesDir(), "Json.txt");

            inputStream1 = new FileInputStream(outputFile);

            int length = (int) outputFile.length();

            byte[] bytes = new byte[length];
            inputStream1.read(bytes);

            text = new String(bytes);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "File can not be read: " + e.toString());
        } finally {
            if (inputStream1 != null) {
                try {
                    inputStream1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return text;
    }
}
