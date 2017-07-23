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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity;
import com.apphousebd.austhub.mainUi.adapters.SaveFileAdapter;
import com.apphousebd.austhub.utilities.ManageSavedFiles;

import java.util.ArrayList;
import java.util.List;

import static com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity.IS_SAVED;
import static com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity.RESULT_STRING;
import static com.apphousebd.austhub.utilities.ManageImageFile.REQUEST_CODE;
import static com.apphousebd.austhub.utilities.ManageSavedFiles.EX_TXT;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedResultFragment extends Fragment {

    List<String> savedFileList = null;
    private ListView savedFilesListView;
    ///search view for searching the file
    private SearchView mFileSearchView;

    public SavedResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ///Toast.makeText(getContext(), TAG + " in onCreateView", Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_result, container, false);

        savedFilesListView = (ListView) view.findViewById(R.id.saved_files_list_view);
        TextView savedFilesEmptyText = (TextView) view.findViewById(R.id.empty_list_text);

        ///referencing the search view
        mFileSearchView = (SearchView) view.findViewById(R.id.save_result_search_view);

        ///getting the files from the external storage

        int permission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        } else {
            savedFileList = ManageSavedFiles.getSavedFileList();
            settingUpTheSearchView(savedFileList);
        }


        if (savedFileList != null && savedFileList.size() > 0) {
            savedFilesEmptyText.setVisibility(View.GONE);
            savedFilesListView.setVisibility(View.VISIBLE);
            setListAdapter(savedFileList);
        } else {
            savedFilesEmptyText.setVisibility(View.VISIBLE);
            savedFilesListView.setVisibility(View.GONE);
        }

        return view;
    }

    private void setListAdapter(final List<String> objects) {
        ArrayAdapter adapter =
                new SaveFileAdapter(getContext(), R.layout.save_result_list_item, objects);
        savedFilesListView.setAdapter(adapter);

        savedFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position: " + position, Toast.LENGTH_SHORT).show();

                String fileName = objects.get(position);
                if (fileName.contains(EX_TXT)) {
                    String resultString = ManageSavedFiles.getFileContent(fileName);


                    Intent intent = new Intent(getContext(), ResultDisplayActivity.class);
                    intent.putExtra(RESULT_STRING, resultString);
                    intent.putExtra(IS_SAVED, true);
                    startActivity(intent);
                }
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

                    savedFileList = ManageSavedFiles.getSavedFileList();
                    settingUpTheSearchView(savedFileList);
                } else {
                    Toast.makeText(getContext(), "Please allow the permission to use this service!", Toast.LENGTH_LONG).show();
                    //finish();
                }
        }

    }

}
