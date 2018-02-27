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

/**
 * Created by Asif Imtiaz Shaafi on 11, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class Constants {
    public static final String FIREBASE_FILE_DATABASE = "files";
    public static final String FIREBASE_USER_DATABASE = "user";
    public static final String USER_PROFILE_IMAGE_STORAGE = "user_profile_image/";
    public static String PROJECT_ID = "com.apphousebd.austhub";

    public static final String DONE = PROJECT_ID + ".DONE";
    public static final String DOWNLOAD = PROJECT_ID + ".DOWNLOAD";

    public static final String MESSAGE_PROGRESS = PROJECT_ID + ".message_progress";


//    download urls

    //    private static String BASE_URL = "http://10.0.2.2/austhub/";
    private static String BASE_URL = "http://www.androidtesting.tk/austhub/";
//    private static String BASE_URL = "http://192.168.0.101/austhub/";

    public static String versionCheckUrl = BASE_URL + "version_check.php";
    public static String ROUTINE_DOWNLOAD_URL = BASE_URL + "version_routine.php";
    public static String UPDATE_ROUTINE_DOWNLOAD_URL = BASE_URL + "routine.php";

}
