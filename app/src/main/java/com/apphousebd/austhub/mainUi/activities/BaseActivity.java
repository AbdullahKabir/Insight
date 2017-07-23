package com.apphousebd.austhub.mainUi.activities;

import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
//
//    //index number to get track of the nav items
//    public static int CURRENT_INDEX = -1;
//    protected DrawerLayout mBaseDrawer;
//    protected FrameLayout mBaseFrame;
//    protected NavigationView mBaseNavigationView;
//    protected Toolbar mBaseToolbar;
//    protected ActionBarDrawerToggle mActionBarDrawerToggle;
//    protected View navHeader;
//
//    private TextView titleCustomText;
//
//    //nav header
//    protected TextView mUserNameText;
//    protected ImageView mUserImage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mBaseToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mBaseToolbar);
//
//
//        //getting the student year and semester
//        SharedPreferences preferences =
//                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//
//        String year = preferences.getString(this.getString(R.string.std_year), "0");
//        String semester = preferences.getString(getString(R.string.std_sem), "0");
//
//        SplashActivity.STD_YEAR = Integer.parseInt(year);
//        SplashActivity.STD_SEMESTER = Integer.parseInt(semester);
//
//    }
//
//    /***********************************************************************************
//     * overloading the default content view method so that it can take the activity's
//     * default layout and then add it to the frame layout of the base drawer layout.
//     * as a result the activity's layout will become the child layout of the drawer/base
//     * layout and it will have the navigation view in it.
//     ************************************************************************************/
//    @Override
//    public void setContentView(int layoutResID) {
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(this)
//                .inflate(R.layout.activity_base, null);
//
//        mBaseDrawer = (DrawerLayout) view.findViewById(R.id.base_drawer);
//
//        mBaseNavigationView = (NavigationView) view.findViewById(R.id.base_nav);
//
//        mBaseFrame = (FrameLayout) view.findViewById(R.id.base_frame_container);
//
//        //setting the frame layout of the base layout as the parent of the activity's layout
//        getLayoutInflater().inflate(layoutResID, mBaseFrame, true);
//
//        ///nav header
//        navHeader = mBaseNavigationView.getHeaderView(0);
//
//        super.setContentView(mBaseDrawer);
//
//    }
//
//    //removing the check/select from the menu
//    protected void removeCheck(int index) {
//        if (mBaseNavigationView.getMenu().getItem(index).isChecked()) {
//            mBaseNavigationView.getMenu().getItem(index).setChecked(false);
//        }
//    }
//
//    private void setNavigationHeader() {
//
//        //navigation bar header
//
//        //getting the layout's reference
//        mUserNameText = (TextView) navHeader.findViewById(R.id.nav_name_text_view);
//        mUserImage = (ImageView) navHeader.findViewById(R.id.nav_image);
//
//        SharedPreferences preferences = getSharedPreferences(SignUp.USER_DETAILS, MODE_PRIVATE);
//
//        String name = preferences.getString(getString(R.string.std_name), null);
//
//        if (name != null)
//            mUserNameText.setText(name);
//
//        //loading image with picasso
//        File file = getSavedImg(getBaseContext());
//        if (file != null && file.length() != 0) {
//            Log.e("manage image", "getFilePath: " + file);
//            Picasso.with(getBaseContext())
//                    .load(file)
//                    .fit()
//                    .into(mUserImage);
//        } else {
//            Picasso.with(getBaseContext())
//                    .load(R.mipmap.ic_launcher)
//                    .fit()
//                    .into(mUserImage);
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (isDrawerOpen()) {
//            mBaseDrawer.closeDrawers();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    //change the item section of the navigation view
//    protected void changeSelectedItem(int index) {
//
//        mBaseNavigationView.getMenu().getItem(index).setChecked(true);
//    }
//
//    //setting the hamburger icon on the actionbar/toolbar
//    protected void setIconForDrawer(final Activity activity, Toolbar toolbar) {
//
//        setNavigationHeader();
//
//        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mBaseDrawer, toolbar,
//                R.string.drawer_open, R.string.drawer_close);
//        mBaseDrawer.addDrawerListener(mActionBarDrawerToggle);
//
//        //navigation click event handle
//        mBaseNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                //changing the nav selected item
//                removeCheck(CURRENT_INDEX);
//
//                Intent intent = null;
//                int tag_index;
//
//                switch (item.getItemId()) {
//
//                    case R.id.action_home:
//
//                        tag_index = 0;
//
//                        break;
//                    case R.id.action_routine:
//
//                        tag_index = 1;
//
//                        break;
//                    case R.id.action_cpga:
//
//                        tag_index = 2;
//
//                        break;
//                    case R.id.action_alarm:
//
//                        tag_index = 3;
//
//                        break;
//                    case R.id.action_result:
//
//                        tag_index = 4;
//
//                        break;
//                    case R.id.action_settings:
//
//                        tag_index = 5;
//                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
//
//                        break;
//                    case R.id.action_us:
//
//                        tag_index = 6;
//                        intent = new Intent(getApplicationContext(), AboutUsActivity.class);
//
//                        break;
//
//                    default:
//                        tag_index = 0;
//                        break;
//                }
//
////                Toast.makeText(BaseActivity.this, "index:" + tag_index, Toast.LENGTH_SHORT).show();
//
//                /***********************************************************************************
//                 check if the current selected index is an item fragment or activity
//                 if activity then launch the activity
//                 else load the selected fragment and change the current index to change the select state
//                 of the nav drawer
//
//                 Note: index range 0 to 3 is fragment and others are activity
//                 ************************************************************************************/
//
//                if (tag_index >= 5) {
//
//
//                    startActivity(intent);
//
//                } else {
//                    CURRENT_INDEX = tag_index;
//
//                    /***********************************************************************************
//                     checking if the navigation item was selected from main activity
//                     or any other activity, if from main activity then load the selected framgent
//                     otherwise set the current index to the selected item and finish that calling activity
//                     as in main activity,the code for checking current index and loading  is already
//                     present.
//
//                     if this check not placed here then the app will try to load the fragment in
//                     the current working activity, where the fragment could not be loaded,
//                     as a result,the app will crush
//                     ************************************************************************************/
//                    if (!activity.getClass().getSimpleName().equals(MAIN_ACTIVITY)) {
//                        activity.finish();
//                        return true;
//                    }
//
//                    loadFragment();
//                }
//
//
//                mBaseDrawer.closeDrawers();
//                return true;
//            }
//        });
//
//    }
//
//    protected void setBaseToolbar(Toolbar toolbar) {
//        mBaseToolbar = toolbar;
//        setSupportActionBar(mBaseToolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null)
//            actionBar.setDisplayHomeAsUpEnabled(true);
//    }
//
//    //drawer checking
//    protected boolean isDrawerOpen() {
//        return mBaseDrawer != null && mBaseDrawer.isDrawerOpen(GravityCompat.START);
//    }
//
//    public void loadFragment() {
//
//        changeSelectedItem(CURRENT_INDEX);
//        CURRENT_TAG = MainActivity.fragmentTags[CURRENT_INDEX];
//
//        setTitle();
//
//        FragmentManager manager = getSupportFragmentManager();
//
//
//        //if the selected item/activity/fragment is already running then just close the drawer
//        if (manager.findFragmentByTag(CURRENT_TAG) != null) {
//            return;
//        }
//
//        //else change the fragment
//
//        final Fragment fragment = getFragment(CURRENT_INDEX);
//        final FragmentTransaction transaction = manager.beginTransaction();
//
//
//        //loading the fragment in a handler so that the fragment can take as much time as needed to
//        //replace the current time and don't conflict with the main thread
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                transaction.setCustomAnimations(R.anim.acitivity_open,
//                        R.anim.activity_close);
//                transaction.replace(R.id.content_main_container,
//                        fragment, CURRENT_TAG);
//
//                transaction.commitAllowingStateLoss();
//            }
//        });
//
//
//        //refresh toolbar
//        invalidateOptionsMenu();
//    }
//
//    private void setTitle() {
//        mBaseToolbar.removeView(titleCustomText);
//        // creating a text view that will be shown in the action bar
//        titleCustomText = new TextView(getApplicationContext());
//
//        //text view parameters/ attributes
//        Toolbar.LayoutParams layoutParams =
//                new Toolbar.LayoutParams(
//                        Toolbar.LayoutParams.MATCH_PARENT,
//                        Toolbar.LayoutParams.WRAP_CONTENT);
//
//        titleCustomText.setLayoutParams(layoutParams);
//
//        //customizing the font from the assets/fonts folder
//
//        int i = (int) (Math.random() * 10);
////        Toast.makeText(getApplicationContext(), "i: " + i, Toast.LENGTH_SHORT).show();
//        String fontPath;
//        if (i % 2 == 0) {
//            fontPath = "fonts/DiplomataSC-Regular.ttf";
//            titleCustomText.setTextSize(18f);
//        } else {
//            fontPath = "fonts/SonsieOne-Regular.ttf";
//            titleCustomText.setTextSize(20f);
//        }
//        Typeface typeface = Typeface.createFromAsset(
//                getAssets(), fontPath);
//
//        //adding font to text view
//        titleCustomText.setTypeface(typeface);
////        titleCustomText.setTextColor(Color.BLACK);
//
//        titleCustomText.setText(MainActivity.titles[CURRENT_INDEX]);
//        //add the custom text view in toolbar
//        mBaseToolbar.addView(titleCustomText);
//
//    }
//
//
//    //getting student details
//    public static void getStudentYearAndSemester(Context context) {
//        SharedPreferences preferences =
//                PreferenceManager.getDefaultSharedPreferences(context);
//        String year = preferences.getString(context.getString(R.string.std_year), "0");
//        String semester = preferences.getString(context.getString(R.string.std_sem), "0");
//
//        SplashActivity.STD_YEAR = Integer.parseInt(year);
//        SplashActivity.STD_SEMESTER = Integer.parseInt(semester);
//    }
//
//
//    ///for submenues
//    public void setSubMenuChecked(int subMenuIndex) {
//
//        MenuItem item = mBaseNavigationView.getMenu().getItem(5);
//        SubMenu subMenu = item.getSubMenu();
//
//        MenuItem subMenuItem = subMenu.getItem(subMenuIndex).setCheckable(true);
//        subMenuItem.setChecked(true);
//    }
//
//    public void removeSubMenuChecked(int subMenuIndex) {
//
//        MenuItem item = mBaseNavigationView.getMenu().getItem(5);
//        SubMenu subMenu = item.getSubMenu();
//
//        MenuItem subMenuItem = subMenu.getItem(subMenuIndex).setCheckable(true);
//        subMenuItem.setChecked(true);
//    }
}
