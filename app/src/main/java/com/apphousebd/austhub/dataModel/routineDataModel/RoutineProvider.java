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

package com.apphousebd.austhub.dataModel.routineDataModel;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asif Imtiaz Shaafi on 06-Dec-16.
 * Email: a15shaafi.209@gmail.com
 */

public class RoutineProvider {

    private static final String TAG = "RoutineProvider";

    private Context mContext;
    private ProgressDialog mProgressDialog;

    ///These will be stored in database
    //Below these will all comes from database
    private static String MATH2203 = "Mathematics IV";
    private static String CSE2208 = "Algorithms Lab";
    private static String CSE2200 = "Software Development-III";
    private static String CSE2209 = "Digital Electronics and Pulse Techniques";
    private static String CSE2201 = "Numerical Methods";
    private static String CSE2210 = "Digital Electronics and Pulse Techniques Lab";
    private static String CSE2202 = "Numerical Methods Lab";
    private static String CSE2213 = "Computer Architecture";
    private static String CSE2207 = "Algorithms";
    private static String CSE2214 = "Assembly Language Programming";


    public static List<RoutineStructure> getRoutineList()
    {
        List<RoutineStructure> mRoutine = new ArrayList<>();

        RoutineStructure structure =
                new RoutineStructure("0", "2_2",
                        "section_c", "saturday",
                        "Time: 08:00 to 10:30;Room: 7B07;CSE 2202(C1/C2);"+ CSE2202  +";Teacher(s): SK, Sadik!" +
                                "Time: 10:30 to 11:20;Room: 7A06;CSE 2209;"+ CSE2209 +";Teacher(s): TAB!" +
                                "Time: 11:20 to 12:10;Room: 7A06;CSE 2213;"+ CSE2213 +";Teacher(s): TAZ!" +
                                "Time: 12:10 to 01:00;Room: 7A06;CSE 2201;"+ CSE2201 +";Teacher(s): SK");
        mRoutine.add(structure);

        RoutineStructure structure0 =
                new RoutineStructure("1", "2_2",
                "section_c", "sunday",
                        "Time: 10:30 to 01:00;Room: 7B03;CSE 2200(C1/C2);"+ CSE2200 +";Teacher(s): Sadik, MHWS!" +
                        "Time: 01:00 to 03:30;Room: 7B05;CSE 2214(C1);"+ CSE2214 +";Teacher(s): TAZ, Asma");
        mRoutine.add(structure0);

        RoutineStructure structure1 =
                new RoutineStructure("2", "2_2",
                "section_c", "monday",
                        "Time: 01:00 to 01:50;Room: 7A05;CSE 2207;"+ CSE2207 +";Teacher(s): FMS!" +
                        "Time: 01:50 to 03:30;Room: 7A05;MATH 2203;"+ MATH2203 +";Teacher(s): EH");
        mRoutine.add(structure1);

        RoutineStructure structure2 =
                new RoutineStructure("3", "2_2",
                "section_c", "tuesday",
                        "Time: 08:00 to 10:30;Room: 7B05;CSE 2208(C1);"+ CSE2208 +";Teacher(s): FMS, Rajon!" +
                        "Time: 10:30 to 11:20;Room: 7C06;CSE 2213;"+ CSE2213 +";Teacher(s): TAZ!" +
                        "Time: 11:20 to 12:10;Room: 7C06;CSE 2209;"+ CSE2209 +";Teacher(s): TAB!" +
                        "Time: 12:10 to 01:00;Room: 7C06;CSE 2201;"+ CSE2201 +";Teacher(s): SK!" +
                        "Time: 01:00 to 03:30;Room: 7B07;CSE 2214(C2);"+ CSE2214 +";Teacher(s): TAZ, Asma");
        mRoutine.add(structure2);

        RoutineStructure structure3 =
                new RoutineStructure("4", "2_2",
                "section_c", "wednesday",
                        "Time: 10:30 to 01:00;Room: 7B04;CSE 2210(C1/C2);"+ CSE2210 +";Teacher(s): TAB, Upoma!" +
                        "Time: 01:00 to 01:50;Room: 7A06;CSE 2207;"+ CSE2207 +";Teacher(s): FMS!" +
                        "Time: 01:50 to 02:40;Room: 7A06;CSE 2201;"+ CSE2201 +";Teacher(s): SK!" +
                        "Time: 02:40 to 03:30;Room: 7A06;MATH 2203;"+ MATH2203 +";Teacher(s): EH");
        mRoutine.add(structure3);

        RoutineStructure structure4 =
                new RoutineStructure("5", "2_2",
                "section_c", "thursday",
                        "Time: 10:30 to 11:20;Room: 7C07;CSE 2213;"+ CSE2213 +";Teacher(s): TAZ!" +
                        "Time: 11:20 to 12:10;Room: 7C07;CSE 2207;"+ CSE2207 +";Teacher(s): FMS!" +
                        "Time: 12:10 to 01:00;Room: 7C07;CSE 2209;"+ CSE2209 +";Teacher(s): TAB!" +
                        "Time: 1:00 to 3:30;Room: 7B05;CSE 2208(C2);"+ CSE2208 +";Teacher(s): FMS, RH");
        mRoutine.add(structure4);

        return mRoutine;
    }
    public static String CSE22C = "saturday=" +
            "Time: 08:00 to 10:30;Room: 7B07;CSE 2202(C1/C2);"+ CSE2202  +";Teacher(s): SK, Sadik!" +
            "Time: 10:30 to 11:20;Room: 7A06;CSE 2209;"+ CSE2209 +";Teacher(s): TAB!" +
            "Time: 11:20 to 12:10;Room: 7A06;CSE 2213;"+ CSE2213 +";Teacher(s): TAZ!" +
            "Time: 12:10 to 01:00;Room: 7A06;CSE 2201;"+ CSE2201 +";Teacher(s): SK ~"

            + "sunday=" + "Time: 10:30 to 01:00;Room: 7B03;CSE 2200(C1/C2);"+ CSE2200 +";Teacher(s): Sadik, MHWS!" +
            "Time: 01:00 to 03:30;Room: 7B05;CSE 2214(C1);"+ CSE2214 +";Teacher(s): TAZ, Asma ~"

            + "monday=" + "Time: 01:00 to 01:50;Room: 7A05;CSE 2207;"+ CSE2207 +";Teacher(s): FMS!" +
            "Time: 01:50 to 03:30;Room: 7A05;MATH 2203;"+ MATH2203 +";Teacher(s): EH ~"

            + "tuesday=" + "Time: 08:00 to 10:30;Room: 7B05;CSE 2208(C1);"+ CSE2208 +";Teacher(s): FMS, Rajon!" +
            "Time: 10:30 to 11:20;Room: 7C06;CSE 2213;"+ CSE2213 +";Teacher(s): TAZ!" +
            "Time: 11:20 to 12:10;Room: 7C06;CSE 2209;"+ CSE2209 +";Teacher(s): TAB!" +
            "Time: 12:10 to 01:00;Room: 7C06;CSE 2201;"+ CSE2201 +";Teacher(s): SK!" +
            "Time: 01:00 to 03:30;Room: 7B07;CSE 2214(C2);"+ CSE2214 +";Teacher(s): TAZ, Asma ~"

            + "wednesday=" + "Time: 10:30 to 01:00;Room: 7B04;CSE 2210(C1/C2);"+ CSE2210 +";Teacher(s): TAB, Upoma!" +
            "Time: 01:00 to 01:50;Room: 7A06;CSE 2207;"+ CSE2207 +";Teacher(s): FMS!" +
            "Time: 01:50 to 02:40;Room: 7A06;CSE 2201;"+ CSE2201 +";Teacher(s): SK!" +
            "Time: 02:40 to 03:30;Room: 7A06;MATH 2203;"+ MATH2203 +";Teacher(s): EH ~"

            + "thursday=" + "Time: 10:30 to 11:20;Room: 7C07;CSE 2213;"+ CSE2213 +";Teacher(s): TAZ!" +
            "Time: 11:20 to 12:10;Room: 7C07;CSE 2207;"+ CSE2207 +";Teacher(s): FMS!" +
            "Time: 12:10 to 01:00;Room: 7C07;CSE 2209;"+ CSE2209 +";Teacher(s): TAB!" +
            "Time: 1:00 to 3:30;Room: 7B05;CSE 2208(C2);"+ CSE2208 +";Teacher(s): FMS, RH ~";


//
//    public RoutineProvider(Context context) {
//        mContext = context;
//
//
//        //check if the network is connected
//        if (hasNetworkConnection(context)) {
//
//
//            /***********************************************************************************
//             starting background task to download routine from internet and
//             saving them in local sqlite database
//             ************************************************************************************/
//            new DownloadRoutine().execute();
//        }
//    }
//
//
//    class DownloadRoutine extends AsyncTask<Void, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            mProgressDialog = new ProgressDialog(mContext);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setTitle("Please Wait!");
//            mProgressDialog.setMessage("Downloading Routine...");
//
//            mProgressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            return NetworkConnectionManager.getRoutineJson(mContext);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            if (mProgressDialog != null && mProgressDialog.isShowing())
//                mProgressDialog.cancel();
//
//            Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
//        }
//    }
}
