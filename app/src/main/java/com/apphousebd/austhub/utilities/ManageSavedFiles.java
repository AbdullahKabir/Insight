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

import android.os.Environment;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Asif Imtiaz Shaafi on 2016.
 * Email: a15Shaafi.209@gmail.com
 */

public class ManageSavedFiles {

    public static final String SAVING_FOLDER_NAME = "com.apphousebd.austhub";
    public static final String EX_TXT = ".txt";
    public static final String EX_PDF = ".pdf";
    private static File dir;


    /***********************************************************************************
     * checking if the external storage is available or not, if available then checking the
     * directory,if the directory already exists then return true or if not,then try to make
     * the directory and return the result
     ************************************************************************************/
    private static boolean hasExternalStorageAndDirExists() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            File root = Environment.getExternalStorageDirectory();
            dir = new File(root.getAbsolutePath(), SAVING_FOLDER_NAME);

            boolean dirExists = dir.exists();

            if (!dirExists) {
                dirExists = dir.mkdir();
            }

            return dirExists;

        } else {
            return false;
        }
    }


    /***********************************************************************************
     * getting the file name,extension and result to create the file in the folder
     * and write the result into the file
     ************************************************************************************/
    public static boolean saveResultFile(String fileName, String fileContent) {

        if (hasExternalStorageAndDirExists()) {
            //making the file
            File resultFile = new File(dir, fileName);

            //creating file output stream to write on the file
            FileOutputStream outputStream = null;

            try {
                outputStream = new FileOutputStream(resultFile);
                outputStream.write(fileContent.getBytes());
                outputStream.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;

            } finally {

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return false;
    }


    /***********************************************************************************
     * look all files that are in the folder and get the files with .txt and .pdf
     * extension
     ************************************************************************************/
    public static List<String> getSavedFileList(String userID) {
        List<String> fileList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FILE_DATABASE);

        if (hasExternalStorageAndDirExists()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(EX_TXT) ||
                        file.getName().endsWith(EX_PDF)) {
                    String name = file.getName();
                    fileList.add(name);
                    if (name.contains(EX_TXT)) {
                        String fileContent = getFileContent(name);
                        if (!TextUtils.isEmpty(fileContent)) {
                            saveResultFileInFirebase(userID, name.replace(EX_TXT, ""), fileContent);
                            file.delete();
                        }
                    }
                }
            }
        }

        return fileList;
    }


    /***********************************************************************************
     * get the file name,then find it in the directory and then return the content
     * if file not found,then return null
     ************************************************************************************/
    public static String getFileContent(String fileName) {
        if (hasExternalStorageAndDirExists()) {

            File file = new File(dir, fileName);

            FileInputStream inputStream = null;

            StringBuilder stringBuffer = null;

            try {

                inputStream = new FileInputStream(file);

                InputStreamReader reader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(reader);

                stringBuffer = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return ((stringBuffer != null) ? stringBuffer.toString() : null);

        } else return null;
    }

    /***********************************************************************************
     * checks if the user has a file saved in same name
     ************************************************************************************/
    public static boolean hasNoDuplicates(final String userID, final String fileName) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FILE_DATABASE);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userID).child(fileName).exists()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }


    public static boolean saveResultFileInFirebase(String userID, String fileName, String resultString) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FILE_DATABASE);
        if (!TextUtils.isEmpty(resultString)) {
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put(fileName, resultString);
            reference.child(userID).child(fileName).setValue(fileMap);
            return true;
        }

        return false;
    }
}
