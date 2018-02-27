/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:42 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.dataModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Asif Imtiaz Shaafi on 10, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class UserModel implements Parcelable {
    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
    private String name, email, phone, uId, imgLink, dept, semester, year, section;

    public UserModel() {
    }

    public UserModel(String name, String email, String phone,
                     String uId, String imgLink, String dept,
                     String semester, String year, String section) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uId = uId;
        this.imgLink = imgLink;
        this.dept = dept;
        this.semester = semester;
        this.year = year;
        this.section = section;
    }

    protected UserModel(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.uId = in.readString();
        this.imgLink = in.readString();
        this.dept = in.readString();
        this.semester = in.readString();
        this.year = in.readString();
        this.section = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("email", email);
        result.put("phone", phone);
        result.put("uId", uId);
        result.put("imgLink", imgLink);
        result.put("dept", dept);
        result.put("semester", semester);
        result.put("year", year);
        result.put("section", section);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.uId);
        dest.writeString(this.imgLink);
        dest.writeString(this.dept);
        dest.writeString(this.semester);
        dest.writeString(this.year);
        dest.writeString(this.section);
    }
}
