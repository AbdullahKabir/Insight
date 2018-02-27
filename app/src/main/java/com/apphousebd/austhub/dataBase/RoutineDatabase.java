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

package com.apphousebd.austhub.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apphousebd.austhub.dataModel.routineDataModel.RoutineServerJsonModel;
import com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants;

import static com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants.COLUMN_DETAILS;

/**
 * Created by Asif Imtiaz Shaafi on 07-Dec-16.
 * Email: a15shaafi.209@gmail.com
 */

public class RoutineDatabase {

    private SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;


    public RoutineDatabase(Context context) {
        //connecting database
        mSQLiteOpenHelper = new DbHelper(context);
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
    }

    public void openDatabase() {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        mSQLiteDatabase.close();
    }

    public long getTableItemCount(String tableName) {
        return DatabaseUtils.queryNumEntries(mSQLiteDatabase, tableName);
    }

    public void insertItem(RoutineServerJsonModel structure, String dept) {
        ContentValues values = structure.getContentValues();
        mSQLiteDatabase.insert(
                RoutineTableConstants.TABLE_NAME + "_" + dept,
                null,
                values
        );
    }

    public void deleteAllItem(String dept) {
        mSQLiteDatabase.delete(RoutineTableConstants.TABLE_NAME + "_" + dept, null, null);
    }

    public String getRoutine(int year, int semester, String section, String dept) {
        String routineDetails = null;
        Cursor cursor = mSQLiteDatabase
                .query(RoutineTableConstants.TABLE_NAME + "_" + dept,
                        RoutineTableConstants.columns,
                        RoutineTableConstants.COLUMN_SESSION + "=? AND " +
                                RoutineTableConstants.COLUMN_SEC_NAME + "=?",
                        new String[]{year + "_" + semester, section.toLowerCase()},
                        null, null, null);

        while (cursor.moveToNext()) {
            routineDetails = cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS));
        }

        cursor.close();
        return routineDetails;
    }

}
