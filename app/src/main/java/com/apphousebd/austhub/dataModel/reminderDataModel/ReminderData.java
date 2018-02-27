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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SayefReyadh on 1/20/2017.
 *
 */

public class ReminderData {

    ///Dummy Data Source

    private static String[] subjects = {"NM" , "CA"};
    private static String[] details = {"Numerical" , "Computer"};
    private static String[] date = {"1/1/17" , "2/2/17"};
    public static String[] time = {"9:00" , "8:00"};

    private static List<ReminderItemModel> reminderData = new ArrayList<>();
    private static boolean isNotSet = false;
    public static List<ReminderItemModel> getData()
    {
        if(!isNotSet)
        {
            List<ReminderItemModel> data = new ArrayList<>();
            for(int j = 0 ; j < subjects.length ; j++)
            {
                ReminderItemModel ob = new ReminderItemModel(0 , subjects[j] , details[j] , date[j] , time[j]);
                ///ReminderActivity.REMINDER_DATABASE_HELPER.addReminder(ob);
                data.add(ob);
            }
            isNotSet = true;
            reminderData = data;
            return data;
        }
        else
            return reminderData;
    }

    public static void addData(ReminderItemModel item)
    {
        reminderData.add(item);
    }

}
