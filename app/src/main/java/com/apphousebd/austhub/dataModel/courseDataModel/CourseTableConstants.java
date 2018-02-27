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

package com.apphousebd.austhub.dataModel.courseDataModel;

/**
 * Created by Asif Imtiaz Shaafi on 12/14/2016.
 * Email: a15shaafi.209@gmail.com
 */

public class CourseTableConstants {

    public static final String TABLE_NAME = "courses_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SEMESTER = "semester";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_COURSE = "courses";
    public static final String COLUMN_CREDIT = "course_credits";

    public static String[] ALL_COLUMNS = {
            COLUMN_YEAR, COLUMN_SEMESTER, COLUMN_COURSE, COLUMN_CREDIT
    };


    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_YEAR + " INTEGER, " +
            COLUMN_SEMESTER + " INTEGER, " +
            COLUMN_COURSE + " TEXT, " +
            COLUMN_CREDIT + " TEXT ); ";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
