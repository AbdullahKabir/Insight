/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:41 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.mainUi.fragments.result;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity;
import com.apphousebd.austhub.mainUi.adapters.SaveFileAdapter;
import com.apphousebd.austhub.utilities.Constants;
import com.apphousebd.austhub.utilities.ManageSavedFiles;
import com.apphousebd.austhub.utilities.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity.IS_SAVED;
import static com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity.RESULT_STRING;
import static com.apphousebd.austhub.utilities.ManageImageFile.REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unchecked")
public class SavedResultFragment extends Fragment {

    private static final String TAG = "SavedResultFragment";

    private List<String> savedFileList = null;
    private List<String> savedFileContentList = null;
    private ListView savedFilesListView;
    ///search view for searching the file
    private SearchView mFileSearchView;
    private TextView savedFilesEmptyText;

    private DatabaseReference mDatabaseReference;
    private UserModel model;

    public SavedResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ///Toast.makeText(getContext(), TAG + " in onCreateView", Toast.LENGTH_SHORT).show();

        model = Utils.getUserModel(getContext().getApplicationContext());
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FILE_DATABASE);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_result, container, false);

        savedFilesListView = view.findViewById(R.id.saved_files_list_view);

        savedFilesEmptyText = view.findViewById(R.id.empty_list_text);

        ///referencing the search view
        mFileSearchView = view.findViewById(R.id.save_result_search_view);

        ///getting the files from the external storage

        int permission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        } else {
            setUpTheList();
        }

        return view;
    }

    private void getResultsFromDatabase() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(model.getuId()).exists()) {
                    savedFileList = new ArrayList<>();
                    savedFileContentList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.child(model.getuId()).getChildren()) {

                        Map<String, String> fileMap =
                                (Map<String, String>) snapshot.getValue();
                        if (fileMap != null) {
//                        savedFileList = Collections.singletonList(fileMap.keySet().toString());
                            Log.d(TAG, "onDataChange: data: " + fileMap.keySet());
                            for (String key : fileMap.keySet()) {
                                savedFileList.add(key);
                                savedFileContentList.add(fileMap.get(key));
                            }

                            settingUpTheSearchView(savedFileList);

                        }
                    }
                }
                if (savedFileList != null && savedFileList.size() > 0) {
                    savedFilesEmptyText.setVisibility(View.GONE);
                    savedFilesListView.setVisibility(View.VISIBLE);
                    setListAdapter(savedFileList);
                } else {
                    savedFilesEmptyText.setVisibility(View.VISIBLE);
                    savedFilesListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                savedFilesEmptyText.setVisibility(View.VISIBLE);
                savedFilesListView.setVisibility(View.GONE);
            }
        });
    }

    private void setListAdapter(final List<String> objects) {
        ArrayAdapter adapter =
                new SaveFileAdapter(getContext(),
                        model.getuId(),
                        R.layout.save_result_list_item, objects);
        savedFilesListView.setAdapter(adapter);

        savedFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position: " + position, Toast.LENGTH_SHORT).show();

                String resultString = savedFileContentList.get(position);

                Intent intent = new Intent(getContext(), ResultDisplayActivity.class);
                intent.putExtra(RESULT_STRING, resultString);
                intent.putExtra(IS_SAVED, true);
                startActivity(intent);
            }
        });

        savedFilesListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

    }

    public void settingUpTheSearchView(final List<String> mainList) {
        mFileSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                List<String> filteredList = new ArrayList<>();

                for (String name : mainList) {
                    if (name.contains(newText)) {
                        filteredList.add(name);
                    }
                }

                setListAdapter(filteredList);

                return true;
            }
        });
    }

    ///file permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setUpTheList();
                } else {
                    Toast.makeText(getContext(), "Please allow the permission to use this service!", Toast.LENGTH_LONG).show();
                    //finish();
                }
        }

    }

    private void setUpTheList() {
        if (model != null) {
            ManageSavedFiles.getSavedFileList(
                    model.getuId()
            );

            getResultsFromDatabase();

        }
    }

}
