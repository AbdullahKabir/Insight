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

/**
 * Created by SayefReyadh on 1/7/2017.
 *
 */
public class RoutineModel {

    private String time;
    private String courseNumber;
    private String courseName;
    private String roomNumber;
    private String teachersName;

    public RoutineModel(String time, String courseNumber, String courseName, String roomNumber, String teachersName) {
        this.time = time;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.roomNumber = roomNumber;
        this.teachersName = teachersName;
    }

    public String getTime() {
        return time;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getTeachersName() {
        return teachersName;
    }
}
