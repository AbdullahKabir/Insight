package com.apphousebd.austhub.mainUi.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.mainUi.fragments.result.LabResultFragment;
import com.apphousebd.austhub.mainUi.fragments.result.SavedResultFragment;
import com.apphousebd.austhub.mainUi.fragments.result.TheoryResultFragment;
import com.apphousebd.austhub.utilities.ZoomOutTransform;

import java.util.ArrayList;
import java.util.List;


public class ResultFragment extends Fragment {

    private SavedResultFragment mSavedResultFragment;
    private TabLayout tabLayout;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ///Toast.makeText(getContext(), TAG + " in onCreateView", Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        final ViewPager pager = (ViewPager) view.findViewById(R.id.result_tab_viewer);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        mSavedResultFragment = new SavedResultFragment();

        setViewPager(pager);
        tabLayout.setupWithViewPager(pager);

        return view;
    }


    private void setViewPager(ViewPager viewPager) {

        ///Toast.makeText(getContext(), TAG + " in setViewPager", Toast.LENGTH_SHORT).show();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(mSavedResultFragment, "Saved Results");
        adapter.addFragment(new TheoryResultFragment(), "Theory Results");
        adapter.addFragment(new LabResultFragment(), "Lab Results");

        viewPager.setAdapter(adapter);

        ///animating the view pager
        viewPager.setPageTransformer(true, new ZoomOutTransform());
    }

    /*
        a inner class for creating and managing the view pager
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            ///Log.i(TAG, "getItem: " + position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
