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

package com.apphousebd.austhub.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Asif Imtiaz Shaafi on 23-Dec-16.
 * Email: a15Shaafi.209@gmail.com
 */

public class ManageImageFile {

    public static final int REQUEST_CODE = 1001;
    private static final String FOLDER_NAME = "Android/data/com.apphousebd.austhub";
    private static File dir;
    private static String fileName = "userImage.jpeg";

    /***********************************************************************************
     * checking if the external storage is available or not, if available then checking the
     * directory,if the directory already exists then return true or if not,then try to make
     * the directory and return the result
     ************************************************************************************/
    private static boolean hasExternalStorageAndDirExists() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            File root = Environment.getExternalStorageDirectory();
            dir = new File(root.getAbsolutePath(), FOLDER_NAME);

            boolean dirExists = dir.exists();

            if (!dirExists) {
                dirExists = dir.mkdir();
            }

            return dirExists;

        } else {
            return false;
        }
    }

    //getting the user selected image and saving in the storage
    public static void saveImage(Context context, Bitmap img) {

        File myFile = null;

        if (hasExternalStorageAndDirExists()) {
            myFile = new File(dir, fileName); //if external storage available then save file in external
        }

        FileOutputStream out = null;
        try {
            if (myFile != null) {
                out = new FileOutputStream(myFile);
            } else {
                //if external not available,then save image in internal
                out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            }
            img.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //getting the image file
    public static File getSavedImg(Context context) {
        if (hasExternalStorageAndDirExists()) {
            return new File(dir, fileName);
        } else {
            //for internal storage
            return new File(context.getFilesDir(), fileName);
        }
    }

}
