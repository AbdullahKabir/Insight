/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:43 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.dataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shaafi on 5/8/2017.
 */

public class DownloadModelClass implements Parcelable {

    public static final Parcelable.Creator<DownloadModelClass> CREATOR = new Parcelable.Creator<DownloadModelClass>() {
        public DownloadModelClass createFromParcel(Parcel in) {
            return new DownloadModelClass(in);
        }

        public DownloadModelClass[] newArray(int size) {
            return new DownloadModelClass[size];
        }
    };
    private int progress;
    private double currentFileSize;
    private int totalFileSize;

    public DownloadModelClass() {
    }

    private DownloadModelClass(Parcel in) {

        progress = in.readInt();
        currentFileSize = in.readInt();
        totalFileSize = in.readInt();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public double getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(double currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(progress);
        dest.writeDouble(currentFileSize);
        dest.writeInt(totalFileSize);
    }

}
