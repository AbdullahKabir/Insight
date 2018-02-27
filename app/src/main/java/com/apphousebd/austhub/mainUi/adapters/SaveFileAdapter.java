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

package com.apphousebd.austhub.mainUi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.utilities.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SaveFileAdapter extends ArrayAdapter<String> {

    private List<String> fileNames;
    private List<String> selectedFiles;
    private Context mContext;
    private String mUserID;

    public SaveFileAdapter(Context context, String userID, int resource, List<String> objects) {
        super(context, resource, objects);
        mContext = context;
        fileNames = objects;
        selectedFiles = new ArrayList<>();
        mUserID = userID;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.save_result_list_item, parent, false);
        }

        TextView nameText = convertView.findViewById(R.id.nameText);
        nameText.setText(fileNames.get(position));

        ImageView imageView = convertView.findViewById(R.id.fileDeleteImg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FILE_DATABASE);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(mUserID).child(fileNames.get(position)).exists()) {
                            reference.child(mUserID).child(fileNames.get(position)).setValue(null);
                            Toast.makeText(mContext, "Result Deleted", Toast.LENGTH_SHORT).show();
                            fileNames.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        if (selectedFiles.contains(fileNames.get(position))) {
            convertView.setBackgroundResource(R.drawable.button_bg);
        } else {
            convertView.setBackgroundResource(R.drawable.list_bg);
        }


        return convertView;
    }

}
