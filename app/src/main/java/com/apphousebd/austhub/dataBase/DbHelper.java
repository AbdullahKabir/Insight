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

package com.apphousebd.austhub.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apphousebd.austhub.dataModel.courseDataModel.CourseTableConstants;
import com.apphousebd.austhub.dataModel.reminderDataModel.ReminderTableConstants;
import com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants;

/**
 * Created by Asif Imtiaz Shaafi on 07-Dec-16.
 * Email: a15shaafi.209@gmail.com
 */

public class DbHelper extends SQLiteOpenHelper {

    /*************************************************************************
     * all CRUD methods for the database tables are in the respective database
     * classes, so that the database tables can be maintained easily
     *
     * In this class,only the required code for making the databases and tables
     * are in place, all constants are also kept in the respective constant
     * classes in dataModel package
     ***************************************************************************/

    private static String DB_NAME = "austhub.db";
    private static int DB_VERSION = 3;


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        ///creating table for storing routine
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_ARCH);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_BBA);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_CE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_CSE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_EEE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_IPE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_ME);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_TE);

        ///creating table for storing courses
        sqLiteDatabase.execSQL(CourseTableConstants.CREATE_TABLE);

        ///creating table for storing user reminder
        sqLiteDatabase.execSQL(ReminderTableConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        ///deleting and recreating table on upgrade

        ///course
        sqLiteDatabase.execSQL(CourseTableConstants.DROP_TABLE);
        sqLiteDatabase.execSQL(CourseTableConstants.CREATE_TABLE);

        ///routine
        ///creating table for storing routine
        sqLiteDatabase.execSQL(RoutineTableConstants.DROP_TABLE_ARCH);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_ARCH);

        sqLiteDatabase.execSQL(RoutineTableConstants.DROP_TABLE_BBA);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_BBA);

        sqLiteDatabase.execSQL(RoutineTableConstants.DROP_TABLE_CE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_CE);

        sqLiteDatabase.execSQL(RoutineTableConstants.DROP_TABLE_CSE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_CSE);

        sqLiteDatabase.execSQL(RoutineTableConstants.DROP_TABLE_EEE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_EEE);

        sqLiteDatabase.execSQL(RoutineTableConstants.DROP_TABLE_IPE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_IPE);

        sqLiteDatabase.execSQL(RoutineTableConstants.DROP_TABLE_ME);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_ME);

        sqLiteDatabase.execSQL(RoutineTableConstants.DROP_TABLE_TE);
        sqLiteDatabase.execSQL(RoutineTableConstants.CREATE_TABLE_TE);


        ///reminder
        sqLiteDatabase.execSQL(ReminderTableConstants.DROP_TABLE);
        sqLiteDatabase.execSQL(ReminderTableConstants.CREATE_TABLE);

    }
}
