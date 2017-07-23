package com.apphousebd.austhub.mainUi.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.mainUi.fragments.CalculatorFragment;
import com.apphousebd.austhub.mainUi.fragments.HomeFragment;
import com.apphousebd.austhub.mainUi.fragments.ReminderFragment;
import com.apphousebd.austhub.mainUi.fragments.ResultFragment;
import com.apphousebd.austhub.mainUi.fragments.RoutineListFragment;
import com.apphousebd.austhub.userAuth.SignUp;
import com.apphousebd.austhub.userAuth.UserProfileActivity;
import com.apphousebd.austhub.utilities.ChangeDefaultColor;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_DEPT;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_SEMESTER;
import static com.apphousebd.austhub.mainUi.activities.SplashActivity.STD_YEAR;
import static com.apphousebd.austhub.utilities.ManageImageFile.getSavedImg;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity
        implements HomeFragment.HomeFragmentListener {

    public static final String MAIN_ACTIVITY = "MainActivity";
    public static final int MAIN_INDEX = 0;
    //tags for the fragments,so that we can track them later
    public static final String HOME_TAG = "home";
    public static final String ROUTINE_TAG = "routine";
    public static final String CALC_TAG = "calculator";
    public static final String ALARM_TAG = "alarm";
    public static final String RESULT_TAG = "result";
    //index number to get track of the nav items
    public static int CURRENT_INDEX = -1;
    //fragment tag array
    public static String[] fragmentTags = {HOME_TAG, ROUTINE_TAG, CALC_TAG,
            ALARM_TAG, RESULT_TAG};
    public static int[] bgIds = {R.drawable.main_bg, R.drawable.routine_bg, R.drawable.routine_bg, R.drawable.reminder_bg};
    //currently using fragment tag
    public static String CURRENT_TAG = HOME_TAG; //initially home
    //titles
    static String[] titles;
    private static Fragment mCalculatorFragment;
    protected DrawerLayout mBaseDrawer;
    protected NavigationView mBaseNavigationView;
    protected Toolbar mBaseToolbar;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;
    protected View navHeader;
    //nav header
    protected TextView mUserNameText;
    protected ImageView mUserImage;
    private TextView titleCustomText;
    ///container of main activity fragment
    private CoordinatorLayout mCoordinatorContainer;

    public static Fragment getFragment(int index) {
        switch (index) {
            case 0:
                return new HomeFragment();
            case 1:
                return new RoutineListFragment();
            case 2:
                if (mCalculatorFragment == null) {
                    mCalculatorFragment = new CalculatorFragment();
                }

                return mCalculatorFragment;

            case 3:
                return new ReminderFragment();
            case 4:
                return new ResultFragment();


            default:
                return new HomeFragment();
        }
    }

    //getting student details
    public static void getStudentYearAndSemester(Context context) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        String year = preferences.getString(context.getString(R.string.std_year), "0");
        String semester = preferences.getString(context.getString(R.string.std_sem), "0");

        STD_DEPT = preferences.getString(context.getString(R.string.std_dept), "cse");
        STD_YEAR = Integer.parseInt(year);
        SplashActivity.STD_SEMESTER = Integer.parseInt(semester);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        /*
        set up everything of base activity
         */
        setBase();

        //setting up the toolbar and the icon for navigation bar and drawer layout
        setBaseToolbar(toolbar);

        setIconForDrawer(this, toolbar);

        titles = getResources().getStringArray(R.array.title_array);

        if (savedInstanceState == null && CURRENT_INDEX == -1) {
            CURRENT_INDEX = MAIN_INDEX;
        }

        mCoordinatorContainer = (CoordinatorLayout) findViewById(R.id.main_activity_container);

        ///changing status bar
        ChangeDefaultColor.changeStatusColor(this, R.color.colorPrimaryDark);

    }

    private void setBase() {

        mBaseDrawer = (DrawerLayout) findViewById(R.id.base_drawer);

        mBaseNavigationView = (NavigationView) findViewById(R.id.base_nav);
        ///nav header
        navHeader = mBaseNavigationView.getHeaderView(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //getting the saved values
        getStudentYearAndSemester(this);

        if (CURRENT_INDEX == -1) {
            CURRENT_INDEX = MAIN_INDEX;
        }

        loadFragment();

        setNavigationHeader();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ///to toggle the home/hamburger icon
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (isDrawerOpen()) {
            mBaseDrawer.closeDrawers();
        }
        //if user is in main activity but in other fragment than home,then send back the user to home
        else if (CURRENT_INDEX != 0) {
            removeCheck(CURRENT_INDEX);
            CURRENT_INDEX = 0;
            loadFragment();
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public void onButtonClickListener(int index) {

        //changeBg(this, mCoordinatorContainer);

        ///if the selected index is less then 5, that is 0 to 4,then reload the fragment,
        ///otherwise start a new activity according to the selected index

        if (index < 5) {

            //changing the nav selected item
            removeCheck(CURRENT_INDEX);

            CURRENT_INDEX = index;

            ///refresh and reload the fragment of the activity to new selected fragment
            loadFragment();

        } else {
            switch (index) {
                case 5:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    break;
                case 6:
                    startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                    break;
                case 7:
                    startActivity(new Intent(getApplicationContext(), UpdateActivity.class));
            }
        }
    }

    public void changeBg(int bgId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCoordinatorContainer.setBackground(getResources()
                    .getDrawable(bgId, getTheme()));
        } else {
            mCoordinatorContainer.setBackground(getResources().getDrawable(bgId));
        }

    }

    //removing the check/select from the menu
    protected void removeCheck(int index) {
        if (mBaseNavigationView.getMenu().getItem(index).isChecked()) {
            mBaseNavigationView.getMenu().getItem(index).setChecked(false);
        }
    }

    private void setNavigationHeader() {

        //navigation bar header

        //getting the layout's reference
        mUserNameText = (TextView) navHeader.findViewById(R.id.nav_name_text_view);
        mUserImage = (ImageView) navHeader.findViewById(R.id.nav_image);

        TextView mUserDetailsText = (TextView) navHeader.findViewById(R.id.nav_std_details_text_view);

        String detailsText = "";

        switch (STD_YEAR) {
            case 1:
                detailsText = "1st Year";
                break;
            case 2:
                detailsText = "2nd Year";
                break;

            case 3:
                detailsText = "3rd Year";
                break;
            case 4:
                detailsText = "4th Year";
                break;

            case 5:
                detailsText = "5th Year";
                break;

        }

        switch (STD_SEMESTER) {
            case 1:
                detailsText += " 1st Semester";
                break;
            case 2:
                detailsText += " 2nd Semester";
                break;
        }

        mUserDetailsText.setText(STD_DEPT.toUpperCase() + ": " + detailsText);

        SharedPreferences preferences = getSharedPreferences(SignUp.USER_DETAILS, MODE_PRIVATE);

        String name = preferences.getString(getString(R.string.std_name), null);

        if (name != null)
            mUserNameText.setText(name);

        //loading image with picasso
        File file = getSavedImg(getBaseContext());
        if (file != null && file.length() != 0) {
            Log.e("manage image", "getFilePath: " + file);
            Picasso.with(getBaseContext())
                    .load(file)
                    .fit()
                    .into(mUserImage);
        } else {
            Picasso.with(getBaseContext())
                    .load(R.mipmap.ic_launcher)
                    .fit()
                    .into(mUserImage);
        }

    }

    //change the item section of the navigation view
    protected void changeSelectedItem(int index) {

        mBaseNavigationView.getMenu().getItem(index).setChecked(true);
    }

    //setting the hamburger icon on the actionbar/toolbar
    protected void setIconForDrawer(final Activity activity, Toolbar toolbar) {

        setNavigationHeader();

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mBaseDrawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mBaseDrawer.addDrawerListener(mActionBarDrawerToggle);

        //navigation click event handle
        mBaseNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //changing the nav selected item
                removeCheck(CURRENT_INDEX);

                Intent intent = null;
                int tag_index;

                switch (item.getItemId()) {

                    case R.id.action_home:

                        tag_index = 0;

                        break;
                    case R.id.action_routine:

                        tag_index = 1;

                        break;
                    case R.id.action_cpga:

                        tag_index = 2;

                        break;
                    case R.id.action_alarm:

                        tag_index = 3;

                        break;
                    case R.id.action_result:

                        tag_index = 4;

                        break;
                    case R.id.action_settings:

                        tag_index = 5;
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);

                        break;
                    case R.id.action_us:

                        tag_index = 6;
                        intent = new Intent(getApplicationContext(), AboutUsActivity.class);

                        break;

//                    case R.id.action_check_update:
//                        tag_index = 7;
//                        intent = new Intent(getApplicationContext(), UpdateActivity.class);
//
//                        break;

                    default:
                        tag_index = 0;
                        break;
                }

//                Toast.makeText(BaseActivity.this, "index:" + tag_index, Toast.LENGTH_SHORT).show();

                /***********************************************************************************
                 check if the current selected index is an item fragment or activity
                 if activity then launch the activity
                 else load the selected fragment and change the current index to change the select state
                 of the nav drawer

                 Note: index range 0 to 3 is fragment and others are activity
                 ************************************************************************************/

                if (tag_index >= 5) {


                    startActivity(intent);

                } else {
                    CURRENT_INDEX = tag_index;

                    /***********************************************************************************
                     checking if the navigation item was selected from main activity
                     or any other activity, if from main activity then load the selected framgent
                     otherwise set the current index to the selected item and finish that calling activity
                     as in main activity,the code for checking current index and loading  is already
                     present.

                     if this check not placed here then the app will try to load the fragment in
                     the current working activity, where the fragment could not be loaded,
                     as a result,the app will crush
                     ************************************************************************************/
                    if (!activity.getClass().getSimpleName().equals(MAIN_ACTIVITY)) {
                        activity.finish();
                        return true;
                    }

                    loadFragment();
                }


                mBaseDrawer.closeDrawers();
                return true;
            }
        });

    }

    protected void setBaseToolbar(Toolbar toolbar) {
        mBaseToolbar = toolbar;
        setSupportActionBar(mBaseToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //drawer checking
    protected boolean isDrawerOpen() {
        return mBaseDrawer != null && mBaseDrawer.isDrawerOpen(GravityCompat.START);
    }

    public void loadFragment() {

        changeSelectedItem(CURRENT_INDEX);
        CURRENT_TAG = MainActivity.fragmentTags[CURRENT_INDEX];
//        final int id = bgIds[CURRENT_INDEX];

        setTitle();

        //changeBg(id);

        FragmentManager manager = getSupportFragmentManager();


        //if the selected item/activity/fragment is already running then just close the drawer
        if (manager.findFragmentByTag(CURRENT_TAG) != null) {
            return;
        }

        //else change the fragment

        final Fragment fragment = getFragment(CURRENT_INDEX);
        final FragmentTransaction transaction = manager.beginTransaction();


        //loading the fragment in a handler so that the fragment can take as much time as needed to
        //replace the current time and don't conflict with the main thread
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                transaction.setCustomAnimations(R.anim.acitivity_open,
                        R.anim.activity_close);
                transaction.replace(R.id.content_main_container,
                        fragment, CURRENT_TAG);

                transaction.commitAllowingStateLoss();
            }
        });


        //refresh toolbar
        invalidateOptionsMenu();
    }

    //    setting up custom font text for the title
    private void setTitle() {
        mBaseToolbar.removeView(titleCustomText);
        // creating a text view that will be shown in the action bar
        titleCustomText = new TextView(getApplicationContext());

        //text view parameters/ attributes
        Toolbar.LayoutParams layoutParams =
                new Toolbar.LayoutParams(
                        Toolbar.LayoutParams.MATCH_PARENT,
                        Toolbar.LayoutParams.WRAP_CONTENT);

        titleCustomText.setLayoutParams(layoutParams);

        //customizing the font from the assets/fonts folder

        int i = (int) (Math.random() * 10);
//        Toast.makeText(getApplicationContext(), "i: " + i, Toast.LENGTH_SHORT).show();
        String fontPath;
        if (i % 2 == 0) {
            fontPath = "fonts/DiplomataSC-Regular.ttf";
            titleCustomText.setTextSize(18f);
        } else {
            fontPath = "fonts/SonsieOne-Regular.ttf";
            titleCustomText.setTextSize(20f);
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);

        //adding font to text view
        titleCustomText.setTypeface(typeface);
//        titleCustomText.setTextColor(Color.BLACK);

        titleCustomText.setText(MainActivity.titles[CURRENT_INDEX]);
        //add the custom text view in toolbar
        mBaseToolbar.addView(titleCustomText);

    }

    public void openSettings(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void showUserProfile(View view) {
        startActivity(new Intent(this, UserProfileActivity.class));
        finish();
    }

}
