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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataBase.ReminderDatabase;
import com.apphousebd.austhub.dataModel.reminderDataModel.ReminderItemModel;

import static com.apphousebd.austhub.backgroundTasks.Receiver.REMINDER_ID;

public class ReminderDetailActivity extends AppCompatActivity {

    int itemId = -1;

    ReminderItemModel itemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);

        itemId = getIntent().getIntExtra(REMINDER_ID, -1);

        if (itemId != -1) {
            ReminderDatabase database = new ReminderDatabase(this);

            itemModel = database.getReminderDataById(itemId);

            if (itemModel == null)
                finish();


            ((TextView) findViewById(R.id.reminder_subject_detail_activity))
                    .setText(itemModel.getSubject());
            ((TextView) findViewById(R.id.reminder_details_detail_activity))
                    .setText(itemModel.getDetails());
            ((TextView) findViewById(R.id.reminder_date_detail_activity))
                    .setText(String.format("Date: %s", itemModel.getDate()));
            ((TextView) findViewById(R.id.reminder_time_detail_activity))
                    .setText(String.format("Time: %s", itemModel.getTime()));

        } else
            finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.reminder_details_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (itemId == -1 || itemModel == null)
            finish();

        switch (item.getItemId()) {
            case R.id.action_delete:
                ReminderDatabase database = new ReminderDatabase(this);
                database.deleteReminder(itemId);
                finish();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditReminder.class);
                intent.putExtra(REMINDER_ID, itemId);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}