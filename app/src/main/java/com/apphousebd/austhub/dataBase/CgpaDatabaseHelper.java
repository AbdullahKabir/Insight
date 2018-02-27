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
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.apphousebd.austhub.dataModel.courseDataModel.CourseTableConstants;
import com.apphousebd.austhub.dataModel.courseDataModel.CourseModel;

/**
 * Created by Asif Imtiaz Shaafi on 12/14/2016.
 * Email: a15shaafi.209@gmail.com
 */

public class CgpaDatabaseHelper {

    private SQLiteDatabase mSQLiteDatabase;
    private SQLiteOpenHelper mSQLiteOpenHelper;

    public CgpaDatabaseHelper(Context context) {
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

    public void insetCourse(CourseModel structure) throws SQLiteException {
        ContentValues contentValues = structure.getCourseContentValues();
        mSQLiteDatabase.insert(CourseTableConstants.TABLE_NAME, null, contentValues);
    }

    public CourseModel getCourse(int year, int semester)
    {
        //cursor holds the returned data from the database
        Cursor cursor =
                mSQLiteDatabase.query(CourseTableConstants.TABLE_NAME,
                        CourseTableConstants.ALL_COLUMNS,
                        CourseTableConstants.COLUMN_YEAR + "=? AND " + CourseTableConstants.COLUMN_SEMESTER + "=?",
                        new String[] {String.valueOf(year),String.valueOf(semester) },
                        null,
                        null,
                        null

                        );

        CourseModel structure = new CourseModel();

        //getting the data from the cursor.. here checking if the cursor can go/point to a valid row,
        //if true that means there are some data
        while (cursor.moveToNext())
        {
            //year
            structure.setYear(cursor.getInt(cursor.getColumnIndex(CourseTableConstants.COLUMN_YEAR)));
            //semester
            structure.setSemester(cursor.getInt(cursor.getColumnIndex(CourseTableConstants.COLUMN_SEMESTER)));
            //course titles
            structure.setCourseTitlesString(cursor.getString(cursor.getColumnIndex(CourseTableConstants.COLUMN_COURSE)));
            structure.setCourseTitles(cursor.getString(cursor.getColumnIndex(CourseTableConstants.COLUMN_COURSE)));
            //course credits
            structure.setCourseCredits(cursor.getString(cursor.getColumnIndex(CourseTableConstants.COLUMN_CREDIT)));
            structure.setCourseCreditsString(cursor.getString(cursor.getColumnIndex(CourseTableConstants.COLUMN_CREDIT)));
        }

        //closing the cursor
        cursor.close();

        return structure;
    }

}
