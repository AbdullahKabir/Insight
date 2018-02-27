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

package com.apphousebd.austhub.dataModel.reminderDataModel;

/**
 * Created by Asif Imtiaz on 01,2017.
 * Email: a15shaafi.209@gmail.com
 */

public class ReminderTableConstants {

    public static final String TABLE_NAME = "reminder_table";
    public static final String ID = "id";
    public static final String SUBJECT = "subject";
    public static final String DETAILS = "details";
    public static final String DATE = "date";
    public static final String TIME = "time";

    public static String[] COLUMN_LIST =
            {ID, SUBJECT, DATE, DETAILS, TIME};

    public static String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SUBJECT + " TEXT, " +
                    DETAILS + " TEXT, " +
                    DATE + " TEXT, " +
                    TIME + " TEXT " +
                    ") ";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
