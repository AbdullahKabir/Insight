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

import com.apphousebd.austhub.dataModel.reminderDataModel.ReminderItemModel;

import java.util.ArrayList;
import java.util.List;

import static com.apphousebd.austhub.dataModel.reminderDataModel.ReminderTableConstants.COLUMN_LIST;
import static com.apphousebd.austhub.dataModel.reminderDataModel.ReminderTableConstants.DATE;
import static com.apphousebd.austhub.dataModel.reminderDataModel.ReminderTableConstants.DETAILS;
import static com.apphousebd.austhub.dataModel.reminderDataModel.ReminderTableConstants.ID;
import static com.apphousebd.austhub.dataModel.reminderDataModel.ReminderTableConstants.SUBJECT;
import static com.apphousebd.austhub.dataModel.reminderDataModel.ReminderTableConstants.TABLE_NAME;
import static com.apphousebd.austhub.dataModel.reminderDataModel.ReminderTableConstants.TIME;

/**
 * Created by Asif Imtiaz on 01,2017.
 * Email: a15shaafi.209@gmail.com
 */

public class ReminderDatabase {

    private SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public ReminderDatabase(Context context) {
        mSQLiteOpenHelper = new DbHelper(context);
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
    }

    private void openDatabase() {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
    }

    private void closeDatabase() {
        mSQLiteDatabase.close();
    }


    public int addReminder(ReminderItemModel reminder) {
        openDatabase();
        ContentValues values = new ContentValues();

        values.put(SUBJECT, reminder.getSubject());
        values.put(DETAILS, reminder.getDetails());
        values.put(DATE, reminder.getDate());
        values.put(TIME, reminder.getTime());

        int id = (int) mSQLiteDatabase.insert(TABLE_NAME, null, values);
        closeDatabase();

        return id;
    }

    public void deleteReminder(int id) {
        openDatabase();
        mSQLiteDatabase
                .execSQL("DELETE FROM " + TABLE_NAME
                        + " WHERE " + ID + "=\"" + id + "\";");
        closeDatabase();
    }

    public void deleteReminder(String subject) {
        openDatabase();
        mSQLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME +
                " WHERE " + SUBJECT + "=\"" + subject + "\";");
        closeDatabase();
    }

    public List<ReminderItemModel> getReminderData() {
        openDatabase();
        List<ReminderItemModel> data = new ArrayList<>();
        ///String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";
        ///Cursor cursor = sqLiteDatabase.rawQuery(query , null);

        Cursor cursor = mSQLiteDatabase.query(
                TABLE_NAME,
                COLUMN_LIST,
                null,
                null,
                null,
                null,
                ID + " DESC"

        );
        int id;
        String subject, details, date, time;
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(SUBJECT)) != null) {
                id = cursor.getInt(cursor.getColumnIndex(ID));
                subject = cursor.getString(cursor.getColumnIndex(SUBJECT));
                details = cursor.getString(cursor.getColumnIndex(DETAILS));
                date = cursor.getString(cursor.getColumnIndex(DATE));
                time = cursor.getString(cursor.getColumnIndex(TIME));
                ReminderItemModel item = new ReminderItemModel(id, subject, details, date, time);
                data.add(item);
            }
        }
        cursor.close();
        closeDatabase();
        return data;
    }


    public ReminderItemModel getReminderDataById(int id) {
        openDatabase();

        Cursor cursor = mSQLiteDatabase.query(
                TABLE_NAME,
                COLUMN_LIST,
                ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                ID + " DESC"

        );
        String subject, details, date, time;
        ReminderItemModel item = null;
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(SUBJECT)) != null) {
                id = cursor.getInt(cursor.getColumnIndex(ID));
                subject = cursor.getString(cursor.getColumnIndex(SUBJECT));
                details = cursor.getString(cursor.getColumnIndex(DETAILS));
                date = cursor.getString(cursor.getColumnIndex(DATE));
                time = cursor.getString(cursor.getColumnIndex(TIME));
                item = new ReminderItemModel(id, subject, details, date, time);

            }
        }
        cursor.close();
        closeDatabase();
        return item;
    }

    public boolean isDatabaseEmpty() {
        return DatabaseUtils.queryNumEntries(mSQLiteDatabase, TABLE_NAME) == 0;
    }

    public void updateReminder(ReminderItemModel itemModel) {

        openDatabase();

        ContentValues values = new ContentValues();

        values.put(SUBJECT, itemModel.getSubject());
        values.put(DETAILS, itemModel.getDetails());
        values.put(DATE, itemModel.getDate());
        values.put(TIME, itemModel.getTime());

        mSQLiteDatabase.update(TABLE_NAME, values, ID + "=?", new String[]{String.valueOf(itemModel.getId())});

        closeDatabase();

    }
}
