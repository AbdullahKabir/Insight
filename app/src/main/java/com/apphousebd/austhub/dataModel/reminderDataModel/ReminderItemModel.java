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

import android.os.Parcel;
import android.os.Parcelable;

public class ReminderItemModel implements Parcelable {
    private int id;
    private String subject;
    private String details;
    private String date;
    private String time;

    public ReminderItemModel(int id, String subject, String details, String date, String time) {
        this.id = id;
        this.subject = subject;
        this.details = details;
        this.date = date;
        this.time = time;
    }


    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getDetails() {
        return details;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.subject);
        dest.writeString(this.details);
        dest.writeString(this.date);
        dest.writeString(this.time);
    }

    private ReminderItemModel(Parcel in) {
        this.id = in.readInt();
        this.subject = in.readString();
        this.details = in.readString();
        this.date = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<ReminderItemModel> CREATOR = new Parcelable.Creator<ReminderItemModel>() {
        @Override
        public ReminderItemModel createFromParcel(Parcel source) {
            return new ReminderItemModel(source);
        }

        @Override
        public ReminderItemModel[] newArray(int size) {
            return new ReminderItemModel[size];
        }
    };
}