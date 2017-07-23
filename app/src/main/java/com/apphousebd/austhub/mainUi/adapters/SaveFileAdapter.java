package com.apphousebd.austhub.mainUi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apphousebd.austhub.R;

import java.util.ArrayList;
import java.util.List;


public class SaveFileAdapter extends ArrayAdapter<String> {

    private List<String> fileNames;
    private List<String> selectedFiles;

    public SaveFileAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        fileNames = objects;
        selectedFiles = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.save_result_list_item, parent, false);
        }

        TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
        nameText.setText(fileNames.get(position));

        if (selectedFiles.contains(fileNames.get(position))) {
            convertView.setBackgroundResource(R.drawable.button_bg);
        } else {
            convertView.setBackgroundResource(R.drawable.list_bg);
        }


        return convertView;
    }

}
