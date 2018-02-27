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

import android.content.ContentValues;

/**
 * Created by Asif Imtiaz Shaafi on 06-Dec-16.
 * Email: a15shaafi.209@gmail.com
 */

public class RoutineStructure {

    private String _id;
    private String session;
    private String section;
    private String day;
    private String routine_details;

    public RoutineStructure(String _id,String session, String secName, String day, String routine_details) {
        this._id = _id;
        this.session = session;
        this.section = secName;
        this.day = day;
        this.routine_details = routine_details;
    }

    public RoutineStructure(String day, String routine_details) {
        this.day = day;
        this.routine_details = routine_details;
    }

    public String getRoutine_details() {
        return routine_details;
    }

    public String getSession() {
        return session;
    }

    public String getSection() {
        return section;
    }

    public String getDay() {
        return day;
    }

    @Override
    public String
    toString() {
        return "RoutineStructure{" +
                "_id=" + _id +
                ", secName='" + section + '\'' +
                ", day='" + day + '\'' +
                ", routine_details='" + routine_details + '\'' +
                '}';
    }

    public ContentValues getContentValues()
    {
        ContentValues values = new ContentValues();

        values.put(RoutineTableConstants.COLUMN_SESSION, session);
        values.put(RoutineTableConstants.COLUMN_SEC_NAME, section);
//        values.put(RoutineTableConstants.COLUMN_DAY, day);
        values.put(RoutineTableConstants.COLUMN_DETAILS, routine_details);

        return values;
    }

}
