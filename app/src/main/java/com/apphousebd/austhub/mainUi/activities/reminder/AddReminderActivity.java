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

package com.apphousebd.austhub.mainUi.activities.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataBase.ReminderDatabase;
import com.apphousebd.austhub.dataModel.reminderDataModel.ReminderItemModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.apphousebd.austhub.backgroundTasks.Receiver.REMINDER_ID;

public class AddReminderActivity extends AppCompatActivity { ///implements View.OnClickListener

    private int alarmHour;
    private int alarmMinute;
    private int alarmDateDay;
    private int alarmDateMonth;
    private int alarmDateYear;
    private Calendar mCalendar;


    //date picker
    @BindView(R.id.date_picker)
    TextView mDatePickerText;

    ///fabs
    @BindView(R.id.save_fab)
    LinearLayout saveFab;
    @BindView(R.id.cancel_fab)
    LinearLayout cancelFab;
    @BindView(R.id.save_reminder_fab)
    FloatingActionButton mainFab;
    private boolean isFabOpen = false;

    //reminder details
    @BindView(R.id.title_edit_text)
    TextView mTitleText;
    @BindView(R.id.title_edit_text_layout)
    TextInputLayout mTitleTextLayout;
    @BindView(R.id.details_edit_text)
    TextView mDetailsText;
    @BindView(R.id.details_edit_text_layout)
    TextInputLayout mDetailsTextLayout;

    TextView mTimePickerText;
    public static final String REMINDER = "id";


    SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //animation
        setAnimations();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ///adding toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //binding view
        ButterKnife.bind(this);

        formatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());

        initViews();

    }

    private void setAnimations() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setReturnTransition(new Explode());
            getWindow().setEnterTransition(new Explode());
        }

    }

    @OnClick(R.id.save_reminder_fab)
    public void setFabs(View view) {

        Animation fab_open = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_open);
        Animation fab_close = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_close);

        Animation rotate_forward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_up);
        Animation rotate_backward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_down);

        if (isFabOpen) {

            mainFab.startAnimation(rotate_backward);

            saveFab.setVisibility(View.GONE);
            cancelFab.setVisibility(View.GONE);

            saveFab.startAnimation(fab_close);
            cancelFab.startAnimation(fab_close);

            saveFab.setClickable(false);
            cancelFab.setClickable(false);

            isFabOpen = false;

        } else {

            mainFab.startAnimation(rotate_forward);

            saveFab.setVisibility(View.VISIBLE);
            cancelFab.setVisibility(View.VISIBLE);

            saveFab.startAnimation(fab_open);
            cancelFab.startAnimation(fab_open);

            saveFab.setClickable(true);
            cancelFab.setClickable(true);

            isFabOpen = true;

        }

    }

    private void initViews() {

        mTimePickerText = (TextView) findViewById(R.id.time_picker);

        mCalendar = Calendar.getInstance();

        ///initializing time variables
        alarmHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        alarmMinute = mCalendar.get(Calendar.MINUTE);

        showTimeToUser(alarmHour, alarmMinute);

        mTimePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });


        //initializing date
        alarmDateDay = mCalendar.get(Calendar.DATE);
        alarmDateMonth = mCalendar.get(Calendar.MONTH);
        alarmDateYear = mCalendar.get(Calendar.YEAR);


        mDatePickerText.setText(String.format("Date: %s",
                formatter.format(mCalendar.getTime())));

        mDatePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }

    public void showDateDialog() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        alarmDateDay = dayOfMonth;
                        alarmDateMonth = monthOfYear;
                        alarmDateYear = year;


                        mCalendar.set(Calendar.YEAR, alarmDateYear);
                        mCalendar.set(Calendar.MONTH, alarmDateMonth);
                        mCalendar.set(Calendar.DATE, alarmDateDay);

                        mDatePickerText.setText(String.format("Date: %s",
                                formatter.format(mCalendar.getTime())));
                    }
                },
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setThemeDark(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpd.setAccentColor(getResources().getColor(R.color.mdtp_accent_color, getBaseContext().getTheme()));
        } else {
            dpd.setAccentColor(getResources().getColor(R.color.mdtp_accent_color));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dpd.setEnterTransition(new Explode());
            dpd.setExitTransition(new Slide(Gravity.BOTTOM));
        }
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void showTimeDialog() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                        alarmHour = hourOfDay;
                        alarmMinute = minute;

                        showTimeToUser(alarmHour, alarmMinute);
                    }
                },
                alarmHour = mCalendar.get(Calendar.HOUR_OF_DAY),
                alarmMinute = mCalendar.get(Calendar.MINUTE), false

        );
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_1);
        timePickerDialog.setThemeDark(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerDialog.setAccentColor(getResources().getColor(R.color.mdtp_accent_color, getBaseContext().getTheme()));
        } else {
            timePickerDialog.setAccentColor(getResources().getColor(R.color.mdtp_accent_color));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            timePickerDialog.setEnterTransition(new Explode());
            timePickerDialog.setExitTransition(new Slide(Gravity.BOTTOM));
        }
        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
    }

    private String showTimeToUser(int alarmHour, int alarmMinute) {

        String reminderTime;

        String dayFormat;
        int displayAlarmHour;
        if (alarmHour > 12) {
            displayAlarmHour = alarmHour - 12;
            dayFormat = "pm";
        } else if (alarmHour == 0) {
            displayAlarmHour = 12;
            dayFormat = "am";
        } else {
            displayAlarmHour = alarmHour;
            dayFormat = "am";
        }

        if (alarmMinute < 10) {
            reminderTime = displayAlarmHour + ":0" + alarmMinute + " " + dayFormat;
        } else {
            reminderTime = displayAlarmHour + ":" + alarmMinute + " " + dayFormat;
        }

        mTimePickerText.setText(String.format("Time: %s", reminderTime));

        return reminderTime;
    }

    @Override
    public boolean onSupportNavigateUp() {

        supportFinishAfterTransition();

        return true;
    }

    @OnClick(R.id.cancel_fab)
    public void cancel(View v) {
        supportFinishAfterTransition();
    }

    @OnClick(R.id.save_fab)
    public void setAlarm(View view) {

        int setAlarmId = validateReminder();

        if (setAlarmId == -1) return;

        mCalendar.set(Calendar.YEAR, alarmDateYear);
        mCalendar.set(Calendar.MONTH, alarmDateMonth);
        mCalendar.set(Calendar.DATE, alarmDateDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        mCalendar.set(Calendar.MINUTE, alarmMinute);
        mCalendar.set(Calendar.SECOND, 0);


        Intent intent = new Intent("com.apphousebd.austhub.backgroundTasks.Receiver");
        intent.putExtra(REMINDER_ID, setAlarmId);
//        Intent intent = new Intent(getApplicationContext(), Receiver.class);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getBaseContext(), setAlarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (mCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Toast.makeText(this, "Please select a valid alarm time!", Toast.LENGTH_SHORT).show();
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent);

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent);
            }

            Toast.makeText(this, "Alarm Added!", Toast.LENGTH_SHORT).show();
            Intent data = new Intent();
//---set the data to pass back---
            data.putExtra(REMINDER_ID, setAlarmId);
            setResult(RESULT_OK, data);
//---close the activity---

            supportFinishAfterTransition();
        }
    }

    private int validateReminder() {

        mTitleTextLayout.setError(null);
        mDetailsTextLayout.setError(null);

        boolean validTitle, validDetails, validTime;

        String title = mTitleText.getText().toString();
        String details = mDetailsText.getText().toString();

        if (TextUtils.isEmpty(title)) {
            validTitle = false;
            mTitleTextLayout.setError("Please Enter a Title");
        } else {
            validTitle = true;
        }

        if (TextUtils.isEmpty(details)) {
            validDetails = false;
            mDetailsTextLayout.setError("Please Enter Details");
        } else {
            validDetails = true;
        }


        mCalendar.set(Calendar.YEAR, alarmDateYear);
        mCalendar.set(Calendar.MONTH, alarmDateMonth);
        mCalendar.set(Calendar.DATE, alarmDateDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        mCalendar.set(Calendar.MINUTE, alarmMinute);
        mCalendar.set(Calendar.SECOND, 0);

        validTime = mCalendar.getTimeInMillis() > System.currentTimeMillis();

        if (!validTime)
            Toast.makeText(this, "Please select a valid time!\nTime can't be in the past time", Toast.LENGTH_SHORT).show();

        if (validDetails && validTime && validTitle) {

            ReminderDatabase reminderDatabase =
                    new ReminderDatabase(this);

            String time = showTimeToUser(alarmHour, alarmMinute);
            String date = formatter.format(mCalendar.getTime());

            ReminderItemModel itemClass = new ReminderItemModel(0, title, details, date, time);

            int id = reminderDatabase.addReminder(itemClass);
            itemClass.setId(id);

            return id;
        }

        return -1;
    }


    public long convertToMilliSec(String time, String date) {

//        String timeToConvert = date + " " + time + ":00 +0600";
        ///+0600 for bangladesh

//        Log.d("Date in milli :: ", "time: " + time);
//        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy k:mm:ss Z",
//                Locale.getDefault());
//        try {
//            Date mDate = sdf.parse(timeToConvert);
//            long timeInMilliseconds = mDate.getTime();
//            Log.d("Date in milli :: ", "time: " + timeInMilliseconds);
//            Log.d("Date in milli :: ", "system time: " + System.currentTimeMillis());
//            Date d = new Date(timeInMilliseconds);
//            Log.d("Date in", "convertToMilliSec: main time: " + d);
//            Log.d("Date in", "convertToMilliSec: main system time: " + new Date(System.currentTimeMillis()));
//            return timeInMilliseconds;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
        /*******************/
        String[] times = time.split(":");

        String[] dates = date.split("/");

        for (String d
                : dates) {
            Log.d("date", "convertToMilliSec: " + d);
        }


        Calendar c = Calendar.getInstance();
//        c.set(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]),
//                Integer.parseInt(times[0]), Integer.parseInt(times[1]), 0);
//
//        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[0]));
//        c.set(Calendar.MONTH, Integer.parseInt(dates[1]));
//        c.set(Calendar.YEAR, Integer.parseInt(dates[2]));
//        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
//        c.set(Calendar.MINUTE, Integer.parseInt(times[1]));
//        c.set(Calendar.SECOND, 0);


        long timeInMilliseconds = mCalendar.getTimeInMillis();
        Log.d("AlarmReceiver :: ", "time: " + timeInMilliseconds);
        Date d = new Date(timeInMilliseconds);
        Log.d("AlarmReceiver", "convertToMilliSec: main time: " + d);
        Log.d("AlarmReceiver", "convertToMilliSec: main system time: " + new Date(System.currentTimeMillis()));

        return timeInMilliseconds;

    }
}
