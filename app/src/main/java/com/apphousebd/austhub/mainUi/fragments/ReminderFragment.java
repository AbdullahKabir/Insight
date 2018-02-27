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

package com.apphousebd.austhub.mainUi.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataBase.ReminderDatabase;
import com.apphousebd.austhub.dataModel.reminderDataModel.ReminderItemModel;
import com.apphousebd.austhub.mainUi.activities.reminder.AddReminderActivity;
import com.apphousebd.austhub.mainUi.activities.reminder.ReminderDetailActivity;
import com.apphousebd.austhub.mainUi.adapters.ReminderAdapter;

import java.util.List;

import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

import static com.apphousebd.austhub.backgroundTasks.Receiver.REMINDER_ID;
import static com.apphousebd.austhub.mainUi.activities.reminder.AddReminderActivity.REMINDER;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment
        implements ReminderAdapter.ItemClickCallback, View.OnClickListener {

    public static final int ADD_REQUEST = 1110;


    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private List<ReminderItemModel> listData;
    private FloatingActionButton addReminder;
    private ReminderDatabase mReminderDatabase;
    private TextView mEmptyText;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        mReminderDatabase = new ReminderDatabase(getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ///animation for recycler view with help of support library: jp.wasabeef:recyclerview-animators:2.2.5
        recyclerView.setItemAnimator(new OvershootInLeftAnimator(2f));
        recyclerView.getItemAnimator().setAddDuration(600);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        recyclerView.getItemAnimator().setMoveDuration(200);
        recyclerView.getItemAnimator().setChangeDuration(200);
        //endAnimation

        addReminder = (FloatingActionButton) view.findViewById(R.id.reminder_button);

        addReminder.setOnClickListener(this);

        mEmptyText = (TextView) view.findViewById(R.id.reminder_empty_text);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        initAdapter();
    }

    private void initAdapter() {

        listData = mReminderDatabase.getReminderData();

        ///checking if list data is null, if null then show user that list is empty
        ///else show remind list
        if (listData != null && listData.size() > 0) {

            //hiding text and then showing list
            mEmptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new ReminderAdapter(listData, getContext());
            recyclerView.setAdapter(adapter);

            adapter.setItemClickCallback(this);

        } else {

            ///showing the empty text msg and hide list
            mEmptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), AddReminderActivity.class);
        startActivityForResult(intent, ADD_REQUEST);
    }

    ///if the intent result is of the adding intent/ adding reminder, then add the reminder to recycleView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_REQUEST && data != null) {
            ReminderItemModel model = data.getParcelableExtra(REMINDER);
            listData.add(0, model);
            if (adapter == null || listData.size() == 1 || adapter.getItemCount() == 0) {
                initAdapter();
//                adapter.notifyItemInserted(0);
            } else {
                adapter.notifyItemInserted(0);
            }
        }

    }

    /*
    private View.OnClickListener addReminderListiner = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };
    */


    @Override
    public void onItemClick(int pos) {
        ReminderItemModel item = listData.get(pos);

        Intent i = new Intent(getContext(), ReminderDetailActivity.class);

        i.putExtra(REMINDER_ID, item.getId());

        startActivity(i);
    }


    @Override
    public void onDeleteIconClick(int pos) {
        //update our data

        ///Delete From Database
        ///REMINDER_DATABASE_HELPER.deleteReminder(item.getSubject());
        //pass new data to adapter and update

        ///ReminderData.reminderData.remove(item);
        ///adapter.setListData((ArrayList<ReminderItemModel>) ReminderData.getData());
        ///adapter.notifyDataSetChanged();

        ReminderItemModel item = listData.get(pos);
        if (pos != RecyclerView.NO_POSITION) {
            listData.remove(pos);
            mReminderDatabase.deleteReminder(item.getSubject());
            adapter.notifyItemRemoved(pos);
        }

        if (listData != null && listData.size() > 0) {

            //hiding text and then showing list
            mEmptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {

            ///showing the empty text msg and hide list
            mEmptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }


}
