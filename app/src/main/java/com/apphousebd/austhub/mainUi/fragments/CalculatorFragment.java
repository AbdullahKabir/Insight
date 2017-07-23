package com.apphousebd.austhub.mainUi.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataBase.CgpaDatabaseHelper;
import com.apphousebd.austhub.dataModel.cgpaDataModel.CGPACalculation;
import com.apphousebd.austhub.dataModel.courseDataModel.CourseData;
import com.apphousebd.austhub.dataModel.courseDataModel.CourseModel;
import com.apphousebd.austhub.dataModel.courseDataModel.CourseTableConstants;
import com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity;
import com.apphousebd.austhub.utilities.Animations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;
import static com.apphousebd.austhub.mainUi.activities.MainActivity.getStudentYearAndSemester;
import static com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity.IS_SAVED;
import static com.apphousebd.austhub.mainUi.activities.ResultDisplayActivity.RESULT_STRING;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_DEPT;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_SEMESTER;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_YEAR;


public class CalculatorFragment extends Fragment {


    private static final String TAG = "CalculatorActivity";

    //database
    CgpaDatabaseHelper helper;

    //textInputLayout container linear layout
    LinearLayout mContainerLayout;

    //mark input edit text array
    TextInputLayout mTextInputLayout;
    List<TextInputLayout> mTextInputLayouts;
    EditText mEditText;
    List<EditText> mEditTexts;
    Button mCgpaBtn;
    LinearLayout mLayout;
    TextView mCreditText;

    ///details showing text
    TextView semesterText;

    //course structure
    CourseModel mCourseModel;
    List<String> courses;


    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        ///displaying semester
        semesterText = (TextView) view.findViewById(R.id.user_year_semester_section_calculator);
        Animations.setScaleXAnimation(semesterText);

        //ref of the container that will hold the edit texts
        mContainerLayout = (LinearLayout) view.findViewById(R.id.mark_taking_linear_layout);
        mLayout = (LinearLayout) view.findViewById(R.id.content_calculator);
        //button for calculating cgpa
        mCgpaBtn = (Button) view.findViewById(R.id.calculate_cgpa_btn);
        mCgpaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCgpa();
            }
        });

        mCreditText = (TextView) view.findViewById(R.id.calculate_text);

        //getting database helper ref
        helper = new CgpaDatabaseHelper(getContext());

        //update database
        updateDatabase();

        Log.i(TAG, "onCreate: year: " + STD_YEAR + " sem: " + STD_SEMESTER);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart");

        getStudentYearAndSemester(getContext());

        ///displaying semester
        Typeface typeface = Typeface.createFromAsset(
                getContext().getAssets(), "fonts/DiplomataSC-Regular.ttf");
        semesterText.setTypeface(typeface);
        semesterText.setText("Semester: " + STD_YEAR + "." + STD_SEMESTER);

        if (STD_DEPT.equals("cse")) {

            fetchDataFromDatabase(STD_YEAR, STD_SEMESTER);
        } else {
            fetchDataFromTxtFile();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        //close database
        helper.closeDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();

        //open database
        helper.openDatabase();

        //setting the edit texts
        // TODO: 4/26/2017 uncomment the line
        if (courses != null && courses.size() > 0) {
            mCreditText.setText("Enter Marks:");
            mCgpaBtn.setVisibility(View.VISIBLE);
            setLayoutWithTextInputLayout(courses.size());
        } else {
            mCgpaBtn.setVisibility(View.GONE);
            mCreditText.setText("Sorry No Course Data Found!");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //removing all edit texts and list items
        removeAll();
    }

    private void fetchDataFromDatabase(int stdYear, int stdSemester) {

        helper.openDatabase();

        mCourseModel = helper.getCourse(stdYear, stdSemester);

        Log.i(TAG, "fetchDataFromDatabase: " + mCourseModel);

        if (mCourseModel != null) {
            updateUi(mCourseModel.getCourseTitles());
        }

        helper.closeDatabase();
    }

    private void fetchDataFromTxtFile() {
        BufferedReader reader = null;
        String fileName = "credits/" + STD_DEPT.toUpperCase() + STD_YEAR + STD_SEMESTER + ".txt";

        Log.d(TAG, "fetchDataFromTxtFile: " + fileName);

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getContext().getAssets().open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            int count = 0;

            StringBuilder courses = new StringBuilder();
            StringBuilder credits = new StringBuilder();

            while ((mLine = reader.readLine()) != null) {
                //process line
                if (count == 1) {
                    courses.append(mLine).append("!");
                    Log.d(TAG, "adding course: " + mLine + "\n");
                } else if (count == 2) {
                    credits.append(mLine).append("!");
                    Log.d(TAG, "adding credit: " + mLine + "\n");
                }

                count++;


                if (count == 3) count = 0;
            }

            courses.deleteCharAt(courses.lastIndexOf("!"));
            credits.deleteCharAt(credits.lastIndexOf("!"));

            Log.d(TAG, courses.toString() + ", " + credits.toString());

            mCourseModel = new CourseModel(STD_YEAR, STD_SEMESTER, courses.toString(), credits.toString());

            updateUi(mCourseModel.getCourseTitles());

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    private void updateUi(List<String> courses) {

        this.courses = courses;

        mTextInputLayouts = new ArrayList<>(courses.size());
        mEditTexts = new ArrayList<>(courses.size());
    }

    private void setLayoutWithTextInputLayout(int size) {


        for (int i = 0; i < size; i++) {

            mTextInputLayout = new TextInputLayout(getContext());
            mEditText = new EditText(getContext());

            //layout attributes for textinputlayout
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            //applying the layout attributes to the layout
            mTextInputLayout.setLayoutParams(params);


            mEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            mEditText.setHint(courses.get(i));
            mEditText.setTextColor(Color.parseColor("#FF3131FF"));
            mEditText.setTextSize(20f);
            mEditText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);


            mTextInputLayout.addView(mEditText);
            mTextInputLayout.setHintAnimationEnabled(true);
            mTextInputLayout.setBackgroundColor(Color.parseColor("#dad9f2"));
            mTextInputLayout.setPadding(4, 8, 4, 4);
            mTextInputLayout.setHintTextAppearance(android.R.style.TextAppearance_Medium);

            mContainerLayout.addView(mTextInputLayout);

            mTextInputLayouts.add(mTextInputLayout);
            mEditTexts.add(mEditText);
        }

    }


    private void removeAll() {
        mContainerLayout.removeAllViews();
        if (mEditTexts != null && mTextInputLayouts != null) {
            mEditTexts.clear();
            mTextInputLayouts.clear();
        }
    }

    private void updateDatabase() {
        helper.openDatabase();
        List<CourseModel> structures = CourseData.getCourseList();
        if (helper.getTableItemCount(CourseTableConstants.TABLE_NAME) == 0) {
            for (CourseModel s : structures) {

                try {
                    helper.insetCourse(s);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }

            }
        }
        helper.closeDatabase();
    }


    public void calculateCgpa() {
        List<Double> marks = new ArrayList<>(courses.size());

        boolean noError = true;

        int i = 0;

        for (EditText et :
                mEditTexts) {

            String mark = et.getText().toString().trim();

            if (!TextUtils.isEmpty(mTextInputLayouts.get(i).getError()))
                mTextInputLayouts.get(i).setError(null);

            if (TextUtils.isEmpty(mark)) {
                noError = false;
            }

            if (!TextUtils.isEmpty(mark)) {

                marks.add(Double.parseDouble(mark));
            } else {
                mTextInputLayouts.get(i).setErrorEnabled(true);
                mTextInputLayouts.get(i).setError("Please Enter Your Mark!");
                final int finalI = i;
                et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            mTextInputLayouts.get(finalI).setError(null);
                            mTextInputLayouts.get(finalI).setErrorEnabled(false);
                        }
                    }
                });
            }

            i++;
        }

        if (!noError) {
            Snackbar.make(mLayout, "Please Fix All Errors!", Snackbar.LENGTH_LONG).show();
        } else {

            CGPACalculation cgpaCalculation =
                    new CGPACalculation(courses,
                            mCourseModel.getCreditDouble(),
                            marks);

            String result = cgpaCalculation.getResultInStringFormat();

            Intent resultIntent = new Intent(getContext(), ResultDisplayActivity.class);

            resultIntent.putExtra(RESULT_STRING, result);
            resultIntent.putExtra(IS_SAVED, false);

            startActivity(resultIntent);
        }
    }

}
