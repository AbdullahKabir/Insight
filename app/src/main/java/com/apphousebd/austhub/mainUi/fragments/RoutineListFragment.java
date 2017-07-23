package com.apphousebd.austhub.mainUi.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.routineDataModel.RoutineProvider;
import com.apphousebd.austhub.dataModel.routineDataModel.RoutineStructure;
import com.apphousebd.austhub.mainUi.activities.RoutineDisplayActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.apphousebd.austhub.mainUi.activities.MainActivity.getStudentYearAndSemester;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_DEPT;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_SEMESTER;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_YEAR;

public class RoutineListFragment extends Fragment {

    public static final String SECTION = "section";
    private static final String TAG = "RoutineListFragment";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_DAY = "EXTRA_DAY";
    ///routine
    List<RoutineStructure> routineStructures = RoutineProvider.getRoutineList();
    private ListView routineList;
    private TextView mClockText;
    private String selectedDay;
    private String selectedSection;
    private View view;
    private PhotoView routineImg;
    private SharedPreferences preferences;

    public RoutineListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getting semester
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        selectedSection = preferences.getString(getString(R.string.std_sec), "A");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_routine_list, container, false);

        routineList = (ListView) view.findViewById(R.id.routine_day_list_view);

        mClockText = (TextView) view.findViewById(R.id.activity_routine_clock_day);

        routineImg = (PhotoView) view.findViewById(R.id.routine_img);

        setListForRoutine();

        setTimeInRoutine();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getStudentYearAndSemester(getContext());
        selectedSection = preferences.getString(getString(R.string.std_sec), "A");

        String imgPath = "file:///android_asset/img/" +
                STD_DEPT.toUpperCase() + STD_YEAR + STD_SEMESTER + selectedSection + ".jpg";
        Log.d(TAG, "onCreateView: " + imgPath);

        routineImg.setImageDrawable(null);
        routineImg.destroyDrawingCache();

        Picasso.with(getContext())
                .load(imgPath)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.no_routine)
                .into(routineImg);

        //getting semester
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        selectedSection = preferences.getString(getString(R.string.std_sec), "A");

        Log.i("routine_section", "onCreateView: section: " + selectedSection);

        ///displaying semester
        TextView semesterShowingText =
                (TextView) view.findViewById(R.id.user_year_semester_section);

        //adding font
        Typeface typeface = Typeface.createFromAsset(
                getContext().getAssets(), "fonts/Kalam-Bold.ttf");
        semesterShowingText.setTypeface(typeface);
        if (getResources().getConfiguration().orientation != ORIENTATION_LANDSCAPE) {
            semesterShowingText.setTextSize(22);
        } else {
            semesterShowingText.setTextSize(16);
        }

        semesterShowingText.setText(String.format("Section:: %s", selectedSection));
//
//        //adding animation
//        Animations.setAlphaXAnimation(semesterShowingText);
    }


    private void setListForRoutine() {

        List<String> dayList = new ArrayList<>(Arrays.asList("Saturday",
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dayList);
//                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dayList);

        routineList.setAdapter(arrayAdapter);
        routineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = String.valueOf(parent.getItemAtPosition(position));
                showRoutine(selectedDay);
            }
        });

    }

    private void showRoutine(String day) {

        Intent i = new Intent(getContext(), RoutineDisplayActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_DAY, day);
        extras.putString(SECTION, selectedSection);


        i.putExtra(BUNDLE_EXTRAS, extras);
        getContext().startActivity(i);
    }

    public void setTimeInRoutine() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.US);
        String date = simpleDateFormat.format(new Date());
        if (getResources().getConfiguration().orientation != ORIENTATION_LANDSCAPE) {
            mClockText.setTextSize(22);
        } else {
            mClockText.setTextSize(16);
        }

        //adding font
        Typeface typeface = Typeface.createFromAsset(
                getContext().getAssets(), "fonts/Kalam-Bold.ttf");
        mClockText.setTypeface(typeface);
        mClockText.setText(date);

    }

}
