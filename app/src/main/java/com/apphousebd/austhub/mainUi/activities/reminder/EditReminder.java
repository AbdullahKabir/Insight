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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataBase.ReminderDatabase;
import com.apphousebd.austhub.dataModel.reminderDataModel.ReminderItemModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.apphousebd.austhub.backgroundTasks.Receiver.REMINDER_ID;

public class EditReminder extends AppCompatActivity {

    private int alarmHour;
    private int alarmMinute;
    private int alarmDateDay;
    private int alarmDateMonth;
    private int alarmDateYear;
    private Calendar mCalendar;


    //date picker
    @BindView(R.id.date_picker)
    TextView mDatePickerText;

    //reminder details
    @BindView(R.id.title_edit_text)
    TextView mTitleText;
    @BindView(R.id.title_edit_text_layout)
    TextInputLayout mTitleTextLayout;
    @BindView(R.id.details_edit_text)
    TextView mDetailsText;
    @BindView(R.id.details_edit_text_layout)
    TextInputLayout mDetailsTextLayout;
    @BindView(R.id.alarm_state)
    Switch mAlarmStateSwitch;


    TextView mTimePickerText;

    private ReminderItemModel mItemModel;
    private SimpleDateFormat formatter;
    private SimpleDateFormat timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //animation
        setAnimations();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        int itemId = getIntent().getIntExtra(REMINDER_ID, -1);

        ReminderDatabase database = new ReminderDatabase(this);
        mItemModel = database.getReminderDataById(itemId);

        if (mItemModel == null) finish();

        //binding view
        ButterKnife.bind(this);

        formatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        initViews();

    }

    private void setAnimations() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setReturnTransition(new Explode());
            getWindow().setEnterTransition(new Explode());
        }

    }

    private void initViews() {

        mTimePickerText = (TextView) findViewById(R.id.time_picker);

        mCalendar = Calendar.getInstance();

        //initializing fields with reminder item date
        mTitleText.setText(mItemModel.getSubject());
        mDetailsText.setText(mItemModel.getDetails());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mm a", Locale.getDefault());
        try {
            Date date = dateFormat.parse(mItemModel.getDate() + " " + mItemModel.getTime());

            mCalendar.setTime(date);

//            Log.d("time", "initViews: " + date.toString() + ":::" + mCalendar.get(Calendar.HOUR_OF_DAY));

            ///initializing time variables
            alarmHour = mCalendar.get(Calendar.HOUR_OF_DAY);
            alarmMinute = mCalendar.get(Calendar.MINUTE);


            //initializing date
            alarmDateDay = mCalendar.get(Calendar.DATE);
            alarmDateMonth = mCalendar.get(Calendar.MONTH);
            alarmDateYear = mCalendar.get(Calendar.YEAR);


            showTimeToUser(alarmHour, alarmMinute);

            mTimePickerText.setText(timeFormatter.format(mCalendar.getTime()));
            mDatePickerText.setText(formatter.format(mCalendar.getTime()));



        mTimePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });


        mDatePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        } catch (ParseException e) {

            mTimePickerText.setText(mItemModel.getTime());
            mDatePickerText.setText(mItemModel.getDate());

            mTimePickerText.setEnabled(false);
            mDatePickerText.setEnabled(false);

            e.printStackTrace();
        }
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

                        mDatePickerText.setText(formatter.format(mCalendar.getTime()));
                    }
                },
                alarmDateYear,
                alarmDateMonth,
                alarmDateDay
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
                alarmHour,
                alarmMinute, false
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

    public void setAlarm(View view) {

        if (!mAlarmStateSwitch.isChecked()) {
            updateDatabaseReminder();
            return;
        }

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

            Toast.makeText(this, "Alarm Updated!", Toast.LENGTH_SHORT).show();
            Intent data = new Intent();
//---set the data to pass back---
            data.putExtra(REMINDER_ID, setAlarmId);
            setResult(RESULT_OK, data);
//---close the activity---

            supportFinishAfterTransition();
        }
    }

    private void updateDatabaseReminder() {

        mTitleTextLayout.setError(null);
        mDetailsTextLayout.setError(null);

        boolean validTitle, validDetails;

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

        if (validTitle && validDetails) {

            String time = timeFormatter.format(mCalendar.getTime());
            String date = formatter.format(mCalendar.getTime());


            mItemModel.setSubject(title);
            mItemModel.setDetails(details);
            mItemModel.setTime(time);
            mItemModel.setDate(date);

            ReminderDatabase database = new ReminderDatabase(this);

            database.updateReminder(mItemModel);

            finish();
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

            updateDatabaseReminder();

            return mItemModel.getId();
        }

        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.reminder_edit_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_save)
            setAlarm(findViewById(R.id.action_save));

        return super.onOptionsItemSelected(item);
    }
}
