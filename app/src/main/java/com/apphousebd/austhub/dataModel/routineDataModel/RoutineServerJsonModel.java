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

import static com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants.COLUMN_DETAILS;
import static com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants.COLUMN_SEC_NAME;
import static com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants.COLUMN_SESSION;

/**
 * Created by Asif Imtiaz Shaafi on February, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class RoutineServerJsonModel {

    private String _id;
    private String session;
    private String section;
    private String routine_details;

    public RoutineServerJsonModel(String _id, String session, String section, String routine_details) {
        this._id = _id;
        this.session = session;
        this.section = section;
        this.routine_details = routine_details;
    }

    public ContentValues getContentValues()
    {
        ContentValues values = new ContentValues(3);

        values.put(COLUMN_SESSION, session);
        values.put(COLUMN_SEC_NAME, section);
        values.put(COLUMN_DETAILS, routine_details);

        return values;
    }

    @Override
    public String toString() {
        return "RoutineServerJsonModel{" +
                "_id=" + _id +
                ", session='" + session + '\'' +
                ", section='" + section + '\'' +
                ", routine_details='" + routine_details + '\'' +
                '}';
    }


}
