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

/**
 * Created by Asif Imtiaz Shaafi on 06-Dec-16.
 * Email: a15shaafi.209@gmail.com
 */

public class RoutineTableConstants {

    public static final String TABLE_NAME = "routine_table";
    public static final String COLUMN_SESSION = "session";
    public static final String COLUMN_SEC_NAME = "section";
    public static final String COLUMN_DETAILS = "details";

    public static final String[] columns = {COLUMN_DETAILS, COLUMN_SEC_NAME, COLUMN_SESSION};


    // TODO: 07-Dec-16 make a create table statement for all other sessions
    public static final String CREATE_TABLE_ARCH = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "_arch" +
            " ( " +
            COLUMN_SESSION + " TEXT," +
            COLUMN_SEC_NAME + " TEXT," +
            COLUMN_DETAILS + " TEXT );";

    public static final String CREATE_TABLE_BBA = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "_bba" +
            " ( " +
            COLUMN_SESSION + " TEXT," +
            COLUMN_SEC_NAME + " TEXT," +
            COLUMN_DETAILS + " TEXT );";

    public static final String CREATE_TABLE_CE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "_ce" +
            " ( " +
            COLUMN_SESSION + " TEXT," +
            COLUMN_SEC_NAME + " TEXT," +
            COLUMN_DETAILS + " TEXT );";

    public static final String CREATE_TABLE_CSE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "_cse" +
            " ( " +
            COLUMN_SESSION + " TEXT," +
            COLUMN_SEC_NAME + " TEXT," +
            COLUMN_DETAILS + " TEXT );";

    public static final String CREATE_TABLE_EEE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "_eee" +
            " ( " +
            COLUMN_SESSION + " TEXT," +
            COLUMN_SEC_NAME + " TEXT," +
            COLUMN_DETAILS + " TEXT );";

    public static final String CREATE_TABLE_IPE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "_ipe" +
            " ( " +
            COLUMN_SESSION + " TEXT," +
            COLUMN_SEC_NAME + " TEXT," +
            COLUMN_DETAILS + " TEXT );";

    public static final String CREATE_TABLE_ME = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "_me" +
            " ( " +
            COLUMN_SESSION + " TEXT," +
            COLUMN_SEC_NAME + " TEXT," +
            COLUMN_DETAILS + " TEXT );";

    public static final String CREATE_TABLE_TE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "_te" +
            " ( " +
            COLUMN_SESSION + " TEXT," +
            COLUMN_SEC_NAME + " TEXT," +
            COLUMN_DETAILS + " TEXT );";

    public static final String DROP_TABLE_ARCH = "DROP TABLE IF EXISTS " + TABLE_NAME + "_arch";
    public static final String DROP_TABLE_BBA = "DROP TABLE IF EXISTS " + TABLE_NAME + "_bba";
    public static final String DROP_TABLE_CE = "DROP TABLE IF EXISTS " + TABLE_NAME + "_ce";
    public static final String DROP_TABLE_CSE = "DROP TABLE IF EXISTS " + TABLE_NAME + "_cse";
    public static final String DROP_TABLE_EEE = "DROP TABLE IF EXISTS " + TABLE_NAME + "_eee";
    public static final String DROP_TABLE_IPE = "DROP TABLE IF EXISTS " + TABLE_NAME + "_ipe";
    public static final String DROP_TABLE_ME = "DROP TABLE IF EXISTS " + TABLE_NAME + "_me";
    public static final String DROP_TABLE_TE = "DROP TABLE IF EXISTS " + TABLE_NAME + "_te";

}
